import org.w3c.dom.Element

plugins {
    id("idea")
    id("net.neoforged.gradle.userdev") version("7.0.145")
    id("maven-publish")

    // This dependency is only used to determine the state of the Git working tree so that build artifacts can be
    // more easily identified. TODO: Lazily load GrGit via a service only when builds are performed.
    id("org.ajoberstar.grgit") version("5.0.0")

    id("me.modmuss50.mod-publish-plugin") version("0.3.4")

    id("embeddium-fabric-remapper")
}

operator fun String.invoke(): String {
    return (rootProject.properties[this] as String?)!!
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.majorVersion
    targetCompatibility = JavaVersion.VERSION_17.majorVersion
    options.encoding = "UTF-8"
}

version = getModVersion()
group = "maven_group"()
println("Embeddium: $version")

base {
    archivesName = "archives_base_name"()
}

// Mojang ships Java 17 to end users in 1.18+, so your mod should target Java 17.
java.toolchain.languageVersion = JavaLanguageVersion.of(17)

val extraSourceSets = arrayOf("legacy", "compat")

sourceSets {
    val main = getByName("main")

    extraSourceSets.forEach {
        val sourceset = create(it)
        sourceset.apply {
            java {
                compileClasspath += main.compileClasspath
                compileClasspath += main.output
            }
        }
    }
}

repositories {
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.fabricmc.net")
    maven("https://maven.tterrag.com/")
    maven("https://maven.blamejared.com")
    maven("https://api.modrinth.com/maven") {
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    maven("https://maven.covers1624.net/")
}

minecraft {
    accessTransformers {
        file(rootProject.file("src/main/resources/META-INF/accesstransformer.cfg"))
    }
}

runs {
    configureEach {

        systemProperty("forge.logging.console.level", "info")

        modSource(sourceSets["main"])
        extraSourceSets.forEach {
            modSource(sourceSets[it])
        }
    }

    create("client")
}

configurations {
    val runtimeOnlyNonPublishable = create("runtimeOnlyNonPublishable") {
        description = "Runtime only dependencies that are not published alongside the jar"
        isCanBeConsumed = false
        isCanBeResolved = false
    }
    runtimeClasspath.get().extendsFrom(runtimeOnlyNonPublishable)
}

val extraModsDir = "extra-mods-${"minecraft_version"()}"

repositories {
    flatDir {
        name = "extra-mods"
        dirs(file(extraModsDir))
    }
}

fun DependencyHandlerScope.compatCompileOnly(dependency: String) {
    "compatCompileOnly"(dependency)
}

dependencies {
    implementation("net.neoforged:neoforge:${"forge_version"()}")

    // Mods
    compatCompileOnly("curse.maven:codechickenlib-242818:${"codechicken_fileid"()}")

    // Fabric API
    compileOnly("net.fabricmc.fabric-api:fabric-api:${"fabric_version"()}")
    compileOnly("net.fabricmc:fabric-loader:${"fabric_loader_version"()}")
}

tasks.processResources {
    inputs.property("version", "version"())

    filesMatching("META-INF/mods.toml") {
        expand("version" to "version"())
    }
}

tasks.withType<JavaCompile> {
    options.release = 17
}


java {
    withSourcesJar()
}

tasks.jar {
    from("COPYING", "COPYING.LESSER", "README.md")

    extraSourceSets.forEach {
        from(sourceSets[it].output.classesDirs)
        from(sourceSets[it].output.resourcesDir)
    }
}

tasks.named<Jar>("sourcesJar").configure {
    extraSourceSets.forEach {
        from(sourceSets[it].allJava)
    }
}

publishing {
    tasks.publish {
        dependsOn(tasks.build)
    }
    publications {
        this.create<MavenPublication>("mavenJava") {
            artifact(tasks.named("jar"))
            artifact(tasks.named("sourcesJar"))
        }
    }

    repositories {
        maven("file://${System.getenv("local_maven")}")
    }
}

publishMods {
    file = tasks.jar.get().archiveFile
    changelog = "https://github.com/embeddedt/embeddium/wiki/Changelog"
    type = STABLE
    modLoaders.add("neoforge")

    curseforge {
        projectId = "908741"
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.add("minecraft_version"())

        incompatible {
            slug = "rubidium"
        }

        incompatible {
            slug = "textrues-embeddium-options"
        }
    }
    modrinth {
        projectId = "sk9rgfiA"
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.add("minecraft_version"())

        incompatible {
            slug = "rubidium"
        }

        incompatible {
            slug = "textrues-embeddium-options"
        }
    }

    displayName = "[${"minecraft_version"()}] Embeddium ${"mod_version"()}"
}

fun getModVersion(): String {
    var baseVersion: String = project.properties["mod_version"].toString()
    val mcMetadata: String = "+mc" + project.properties["minecraft_version"]

    if (project.hasProperty("build.release")) {
        return baseVersion + mcMetadata // no tag whatsoever
    }

    // Increment patch version
    baseVersion = baseVersion.split(".").mapIndexed {
        index, s -> if(index == 2) (s.toInt() + 1) else s
    }.joinToString(separator = ".")

    val head = grgit.head()
    var id = head.abbreviatedId

    // Flag the build if the build tree is not clean
    if (!grgit.status().isClean) {
        id += "-dirty"
    }

    return baseVersion + "-git-${id}" + mcMetadata
}
