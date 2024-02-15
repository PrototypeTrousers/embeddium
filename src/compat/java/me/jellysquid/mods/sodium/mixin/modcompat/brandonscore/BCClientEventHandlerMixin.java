package me.jellysquid.mods.sodium.mixin.modcompat.brandonscore;

import com.brandon3055.brandonscore.client.BCClientEventHandler;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.Set;

@Mixin(BCClientEventHandler.class)
public class BCClientEventHandlerMixin {
    @Redirect(method = "renderLevelStage", at = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;", ordinal = 0))
    private Iterator<BlockEntity> useEmbeddiumBEIterator(Set instance) {
        return SodiumWorldRenderer.instance().streamBlockEntities().iterator();
    }
}