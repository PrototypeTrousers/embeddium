package me.jellysquid.mods.sodium.client.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.level.ColorResolver;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Wrapper object used to defeat identity comparisons in mods. Since vanilla provides a unique object to them for each
 * subchunk, we do the same.
 */
public class WorldSliceLocal implements BlockRenderView {
    private final BlockRenderView view;

    public WorldSliceLocal(BlockRenderView view) {
        this.view = view;
    }

    @Override
    public float getBrightness(Direction direction, boolean shaded) {
        return view.getBrightness(direction, shaded);
    }

    @Override
    public LightingProvider getLightingProvider() {
        return view.getLightingProvider();
    }

    @Override
    public int getColor(BlockPos pos, ColorResolver colorResolver) {
        return view.getColor(pos, colorResolver);
    }

    @Override
    public int getLightLevel(LightType type, BlockPos pos) {
        return view.getLightLevel(type, pos);
    }

    @Override
    public int getBaseLightLevel(BlockPos pos, int ambientDarkness) {
        return view.getBaseLightLevel(pos, ambientDarkness);
    }

    @Override
    public boolean isSkyVisible(BlockPos pos) {
        return view.isSkyVisible(pos);
    }

    @Override
    @Nullable
    public BlockEntity getBlockEntity(BlockPos pos) {
        return view.getBlockEntity(pos);
    }

    @Override
    public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
        return view.getBlockEntity(pos, type);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return view.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return view.getFluidState(pos);
    }

    @Override
    public int getLuminance(BlockPos pos) {
        return view.getLuminance(pos);
    }

    @Override
    public int getMaxLightLevel() {
        return view.getMaxLightLevel();
    }

    @Override
    public Stream<BlockState> getStatesInBox(Box box) {
        return view.getStatesInBox(box);
    }

    @Override
    public BlockHitResult raycast(BlockStateRaycastContext context) {
        return view.raycast(context);
    }

    @Override
    public BlockHitResult raycast(RaycastContext context) {
        return view.raycast(context);
    }

    @Override
    @Nullable
    public BlockHitResult raycastBlock(Vec3d start, Vec3d end, BlockPos pos, VoxelShape shape, BlockState state) {
        return view.raycastBlock(start, end, pos, shape, state);
    }

    @Override
    public double getDismountHeight(VoxelShape blockCollisionShape, Supplier<VoxelShape> belowBlockCollisionShapeGetter) {
        return view.getDismountHeight(blockCollisionShape, belowBlockCollisionShapeGetter);
    }

    @Override
    public double getDismountHeight(BlockPos pos) {
        return view.getDismountHeight(pos);
    }

    public static <T, C> T raycast(Vec3d start, Vec3d end, C context, BiFunction<C, BlockPos, T> blockHitFactory, Function<C, T> missFactory) {
        return BlockView.raycast(start, end, context, blockHitFactory, missFactory);
    }

    @Override
    public int getHeight() {
        return view.getHeight();
    }

    @Override
    public int getBottomY() {
        return view.getBottomY();
    }

    @Override
    public int getTopY() {
        return view.getTopY();
    }

    @Override
    public int countVerticalSections() {
        return view.countVerticalSections();
    }

    @Override
    public int getBottomSectionCoord() {
        return view.getBottomSectionCoord();
    }

    @Override
    public int getTopSectionCoord() {
        return view.getTopSectionCoord();
    }

    @Override
    public boolean isOutOfHeightLimit(BlockPos pos) {
        return view.isOutOfHeightLimit(pos);
    }

    @Override
    public boolean isOutOfHeightLimit(int y) {
        return view.isOutOfHeightLimit(y);
    }

    @Override
    public int getSectionIndex(int y) {
        return view.getSectionIndex(y);
    }

    @Override
    public int sectionCoordToIndex(int coord) {
        return view.sectionCoordToIndex(coord);
    }

    @Override
    public int sectionIndexToCoord(int index) {
        return view.sectionIndexToCoord(index);
    }

    public static HeightLimitView create(int bottomY, int height) {
        return HeightLimitView.create(bottomY, height);
    }

    @Override
    @org.jetbrains.annotations.Nullable
    public BlockEntity getExistingBlockEntity(BlockPos pos) {
        return view.getExistingBlockEntity(pos);
    }
}