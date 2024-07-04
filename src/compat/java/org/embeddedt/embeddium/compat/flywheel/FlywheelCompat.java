package org.embeddedt.embeddium.compat.flywheel;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod.EventBusSubscriber(modid = SodiumClientMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlywheelCompat {
    private static final boolean flywheelLoaded = FMLLoader.getLoadingModList().getModFileById("flywheel") != null;
}
