package net.fayefer.salt_and_silver.block.custom;

import com.mojang.serialization.MapCodec;
import net.fayefer.salt_and_silver.fluid.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

import static net.fayefer.salt_and_silver.block.ModBlocks.MILK_CAULDRON;
import static net.minecraft.world.item.Items.MILK_BUCKET;
import static net.minecraft.world.level.block.Blocks.CAULDRON;


public class MilkCauldron extends AbstractCauldronBlock {
    static final CauldronInteraction.InteractionMap MILK_CAULDRON_INTERACTION = CauldronInteraction.newInteractionMap("milk_cauldron_interaction");
    static final CauldronInteraction FILL_FROM_BUCKET = (state, world, pos, player, hand, stack) ->
            CauldronInteraction.emptyBucket(world, pos, player, hand, stack, MILK_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY);
    static final CauldronInteraction EMPTY_TO_BUCKET = (state, world, pos, player, hand, stack) ->
            CauldronInteraction.fillBucket(state, world, pos, player, hand, stack, new ItemStack(MILK_BUCKET), statex -> true, SoundEvents.BUCKET_FILL);
    public static final MapCodec<MilkCauldron> CODEC = simpleCodec(MilkCauldron::new);

    public static void init() {
        CauldronInteraction.EMPTY.map().put(MILK_BUCKET, MilkCauldron.FILL_FROM_BUCKET);
    }

    public MilkCauldron(Properties properties) {
        super(properties, getMilkCauldronInteraction());
    }

    private static CauldronInteraction.InteractionMap getMilkCauldronInteraction() {
        MILK_CAULDRON_INTERACTION.map().put(MILK_BUCKET, FILL_FROM_BUCKET);
        MILK_CAULDRON_INTERACTION.map().put(Items.BUCKET, EMPTY_TO_BUCKET);
        return MILK_CAULDRON_INTERACTION;
    }

    @Override
    public @NotNull MapCodec<MilkCauldron> codec() {
        return CODEC;
    }

    @Override
    public boolean isFull(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected double getContentHeight(@NotNull BlockState state) {
        return 0.9375;
    }

    @Override
    protected boolean canReceiveStalactiteDrip(@NotNull Fluid fluid) {
        return fluid == ModFluids.MILK_FLUID_STILL.get().getSource();
    }

    @Override
    public void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!level.isClientSide && isEntityInsideContent(state, pos, entity) && entity.mayInteract(level, pos)) {
            if (entity.isOnFire()) {
                entity.clearFire();
                BlockState blockState = CAULDRON.defaultBlockState();
                level.setBlockAndUpdate(pos, blockState);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockState));
            }
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.removeAllEffects();
            }
        }
    }
}
