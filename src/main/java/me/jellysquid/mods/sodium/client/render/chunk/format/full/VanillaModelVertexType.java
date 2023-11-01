package me.jellysquid.mods.sodium.client.render.chunk.format.full;

import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexAttributeFormat;
import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexFormat;
import me.jellysquid.mods.sodium.client.model.vertex.buffer.VertexBufferView;
import me.jellysquid.mods.sodium.client.model.vertex.type.BlittableVertexType;
import me.jellysquid.mods.sodium.client.model.vertex.type.ChunkVertexType;
import me.jellysquid.mods.sodium.client.render.chunk.format.ChunkMeshAttribute;
import me.jellysquid.mods.sodium.client.render.chunk.format.ModelVertexSink;
import me.jellysquid.mods.sodium.client.render.chunk.format.VanillaLikeChunkMeshAttribute;
import net.minecraft.client.render.VertexConsumer;

/**
 * Simple vertex format which uses single-precision floating point numbers to represent position and texture
 * coordinates.
 */
public class VanillaModelVertexType implements ChunkVertexType<VanillaLikeChunkMeshAttribute> {
    public static final GlVertexFormat<VanillaLikeChunkMeshAttribute> VERTEX_FORMAT = GlVertexFormat.builder(VanillaLikeChunkMeshAttribute.class, 28)
            .addElement(VanillaLikeChunkMeshAttribute.POSITION, 0, GlVertexAttributeFormat.FLOAT, 3, false, false)
            .addElement(VanillaLikeChunkMeshAttribute.COLOR, 12, GlVertexAttributeFormat.UNSIGNED_BYTE, 4, true, false)
            .addElement(VanillaLikeChunkMeshAttribute.BLOCK_TEX_ID, 16, GlVertexAttributeFormat.UNSIGNED_SHORT, 4, false, false)
            .addElement(VanillaLikeChunkMeshAttribute.LIGHT, 24, GlVertexAttributeFormat.UNSIGNED_SHORT, 2, true, true)
            .build();

    private static final int TEXTURE_MAX_VALUE = 65536;

    private static final float TEXTURE_SCALE = (1.0f / TEXTURE_MAX_VALUE);

    @Override
    public ModelVertexSink createFallbackWriter(VertexConsumer consumer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ModelVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
        return direct ? new VanillaModelVertexBufferWriterUnsafe(buffer) : new VanillaModelVertexBufferWriterNio(buffer);
    }

    @Override
    public BlittableVertexType<ModelVertexSink> asBlittable() {
        return this;
    }

    @Override
    public GlVertexFormat<VanillaLikeChunkMeshAttribute> getCustomVertexFormat() {
        return VERTEX_FORMAT;
    }

    @Override
    public float getTextureScale() {
        return TEXTURE_SCALE;
    }

    static short encodeBlockTexture(float value) {
        return (short) (Math.min(0.99999997F, value) * TEXTURE_MAX_VALUE);
    }

    @Override
    public float getPositionOffset() {
        return 0;
    }

    @Override
    public float getPositionScale() {
        return 1;
    }
}