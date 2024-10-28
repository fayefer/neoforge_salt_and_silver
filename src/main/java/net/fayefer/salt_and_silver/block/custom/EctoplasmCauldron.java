package net.fayefer.salt_and_silver.block.custom;

import com.mojang.serialization.MapCodec;
import net.fayefer.salt_and_silver.fluid.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import static net.fayefer.salt_and_silver.block.ModBlocks.ECTOPLASM_CAULDRON;
import static net.fayefer.salt_and_silver.fluid.ModFluids.ECTOPLASM_FLUID;
import static net.minecraft.world.level.block.Blocks.CAULDRON;


public class EctoplasmCauldron extends AbstractCauldronBlock {

    public static final CauldronInteraction.InteractionMap ECTOPLASM_CAULDRON_INTERACTION = CauldronInteraction.newInteractionMap("ectoplasm_cauldron_interaction");
    public static final CauldronInteraction FILL_FROM_BUCKET = (state, world, pos, player, hand, stack) ->
            CauldronInteraction.emptyBucket(world, pos, player, hand, stack, ECTOPLASM_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY);
    public static final CauldronInteraction EMPTY_TO_BUCKET = (state, world, pos, player, hand, stack) ->
            CauldronInteraction.fillBucket(state, world, pos, player, hand, stack, new ItemStack(ECTOPLASM_FLUID.get()), statex -> true, SoundEvents.BUCKET_FILL);
    public static final MapCodec<EctoplasmCauldron> CODEC = simpleCodec(EctoplasmCauldron::new);

    public static void init() {
        CauldronInteraction.EMPTY.map().put(ECTOPLASM_FLUID.get(), EctoplasmCauldron.FILL_FROM_BUCKET);
    }

    public EctoplasmCauldron(Properties properties) {
        super(properties, getEctoplasmCauldronInteraction());
    }

    private static CauldronInteraction.InteractionMap getEctoplasmCauldronInteraction() {
        ECTOPLASM_CAULDRON_INTERACTION.map().put(ECTOPLASM_FLUID.get(), FILL_FROM_BUCKET);
        ECTOPLASM_CAULDRON_INTERACTION.map().put(Items.BUCKET, EMPTY_TO_BUCKET);
        return ECTOPLASM_CAULDRON_INTERACTION;
    }

    @Override
    public @NotNull MapCodec<EctoplasmCauldron> codec() {
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
        return fluid == ModFluids.ECTOPLASM_FLUID_STILL.get().getSource();
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
                livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40, 1, true, false));
            }
        }
    }
}