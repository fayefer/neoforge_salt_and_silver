package net.fayefer.salt_and_silver.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EctoplasmItem extends Item {
    private static final Map<Block, Block> ECTOPLASM_BLOCK_MAP =
            Map.of(
                    Blocks.STONE, Blocks.NETHERRACK,
                    Blocks.STONE_BRICKS, Blocks.NETHER_BRICKS,
                    Blocks.COBBLESTONE, Blocks.BLACKSTONE,
                    Blocks.DIRT, Blocks.SOUL_SOIL,
                    Blocks.SAND, Blocks.SOUL_SAND
            );

    public EctoplasmItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);

        if(!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;

            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80));
            player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 80));
            level.playSound((Player) null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, SoundSource.PLAYERS, 0.5F, 0.4F);
            serverLevel.sendParticles(ParticleTypes.SOUL, player.getX() + 0.5, player.getY() + 0.5, player.getZ() + 0.5,
                    16, 0.1, 0.1, 0.1, 0.1);
            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX() + 0.5, player.getY() + 1.5, player.getZ() + 0.5,
                    16, 0.1, 0.1, 0.1, 0.1);
            itemStack.consume(1, player);
        }
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack itemStack, @NotNull Player player, LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {
        Level level = interactionTarget.level();
        LivingEntity mob = interactionTarget;

        if(!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            if(mob.getType() == EntityType.SLIME) {
                MagmaCube magmaCube = EntityType.MAGMA_CUBE.create(level);
                magmaCube.setSize(((Slime) mob).getSize(), false);
                magmaCube.setPos(mob.getPosition(0));
                level.addFreshEntity(magmaCube);
                mob.discard();
                mob = magmaCube;
            }
            else if(mob.getType() == EntityType.SKELETON) {
                WitherSkeleton witherSkeleton = EntityType.WITHER_SKELETON.create(level);
                witherSkeleton.setPos(mob.getPosition(0));
                level.addFreshEntity(witherSkeleton);
                mob.discard();
                mob = witherSkeleton;
            }
            else if(mob.getType() == EntityType.BREEZE) {
                Blaze blaze = EntityType.BLAZE.create(level);
                blaze.setPos(mob.getPosition(0));
                level.addFreshEntity(blaze);
                mob.discard();
                mob = blaze;
            }
            else if(mob.getType() == EntityType.PHANTOM) {
                Ghast ghast = EntityType.GHAST.create(level);
                ghast.setPos(mob.getPosition(0));
                level.addFreshEntity(ghast);
                mob.discard();
                mob = ghast;
            }
            mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80));
            mob.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 80));
            mob.playSound(SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, 0.5F, 0.4F);
            serverLevel.sendParticles(ParticleTypes.SOUL, mob.getX() + 0.5, mob.getY() + 0.5, mob.getZ() + 0.5,
                    16, 0.1, 0.1, 0.1, 0.1);
            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, mob.getX() + 0.5, mob.getY() + 1.5, mob.getZ() + 0.5,
                    16, 0.1, 0.1, 0.1, 0.1);
            itemStack.consume(1, player);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Block clickedBlock = level.getBlockState(clickedPos).getBlock();
        Block aboveBlock = level.getBlockState(clickedPos.above()).getBlock();
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();

        if(!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            if (player != null) {
                if (!player.isCrouching()) {
                    if (clickedBlock != Blocks.BEDROCK && clickedBlock != Blocks.REINFORCED_DEEPSLATE) {
                        if (aboveBlock == Blocks.AIR || aboveBlock == Blocks.WATER || aboveBlock == Blocks.LAVA) {
                            if (ECTOPLASM_BLOCK_MAP.containsKey(clickedBlock)) {
                                level.setBlockAndUpdate(clickedPos, ECTOPLASM_BLOCK_MAP.get(clickedBlock).defaultBlockState());
                            }
                            level.setBlockAndUpdate(clickedPos.above(), level.getBlockState(clickedPos));
                            level.setBlockAndUpdate(clickedPos, aboveBlock.defaultBlockState());
                            serverLevel.sendParticles(ParticleTypes.SOUL, clickedPos.getX() + 0.5, clickedPos.getY() + 0.5, clickedPos.getZ() + 0.5,
                                    16, 0.1, 0.1, 0.1, 0.1);
                            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, clickedPos.getX() + 0.5, clickedPos.getY() + 1.5, clickedPos.getZ() + 0.5,
                                    16, 0.1, 0.1, 0.1, 0.1);
                            level.playSound((Player) null, clickedPos,
                                    SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, SoundSource.PLAYERS, 0.5F, 0.4F);
                            itemStack.consume(1, player);
                        }
                    }
                } else {
                    player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80));
                    player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 80));
                    serverLevel.sendParticles(ParticleTypes.SOUL, player.getX() + 0.5, player.getY() + 0.5, player.getZ() + 0.5,
                            16, 0.1, 0.1, 0.1, 0.1);
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX() + 0.5, player.getY() + 1.5, player.getZ() + 0.5,
                            16, 0.1, 0.1, 0.1, 0.1);
                    level.playSound((Player) null, clickedPos,
                            SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, SoundSource.PLAYERS, 0.5F, 0.4F);
                    itemStack.consume(1, player);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
