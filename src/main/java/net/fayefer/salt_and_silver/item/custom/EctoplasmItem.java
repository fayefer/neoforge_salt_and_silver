package net.fayefer.salt_and_silver.item.custom;

import net.minecraft.client.particle.SplashParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class EctoplasmItem extends Item {
    public EctoplasmItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();
        if(!level.isClientSide()) {
            Block aboveBlock = level.getBlockState(context.getClickedPos().above()).getBlock();
            if(aboveBlock == Blocks.AIR && clickedBlock != Blocks.BEDROCK && clickedBlock != Blocks.REINFORCED_DEEPSLATE) {
                level.setBlockAndUpdate(context.getClickedPos().above(), level.getBlockState(context.getClickedPos()));
                level.setBlockAndUpdate(context.getClickedPos(), Blocks.AIR.defaultBlockState());

                ServerLevel serverLevel = (ServerLevel) context.getLevel();
                serverLevel.sendParticles(ParticleTypes.SOUL, context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(),
                        16, 0, 0, 0, 0.1);
                level.playSound((Player)null, context.getClickedPos(),
                        SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

                context.getItemInHand().consume(1, context.getPlayer());
            }
        }
        return InteractionResult.SUCCESS;
    }
}
