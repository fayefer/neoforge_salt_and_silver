package net.fayefer.salt_and_silver.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.NotNull;

public class MilkFluidBlock extends LiquidBlock {
    public MilkFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            if (!livingEntity.getActiveEffects().isEmpty()) {
                livingEntity.removeAllEffects();
            }
        }
    }
}