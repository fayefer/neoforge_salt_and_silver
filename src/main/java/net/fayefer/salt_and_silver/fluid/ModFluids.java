package net.fayefer.salt_and_silver.fluid;

import net.fayefer.salt_and_silver.SaltAndSilver;
import net.fayefer.salt_and_silver.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid.Flowing;
import net.neoforged.neoforge.fluids.BaseFlowingFluid.Source;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModFluids {

    public static final DeferredRegister<Block> VANILLA_BLOCKS = DeferredRegister.createBlocks("minecraft");
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, SaltAndSilver.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks("salt_and_silver");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("salt_and_silver");

    public static final Supplier<BaseFlowingFluid> MILK_FLUID_STILL = FLUIDS.register(NeoForgeMod.MILK.getId().getPath(), () -> new Source(createMilkProperties()));
    public static final Supplier<BaseFlowingFluid> MILK_FLUID_FLOWING = FLUIDS.register(NeoForgeMod.FLOWING_MILK.getId().getPath(), () -> new Flowing(createMilkProperties()));

    public static final Supplier<LiquidBlock> MILK_FLUID_BLOCK = VANILLA_BLOCKS.register(NeoForgeMod.MILK.getId().getPath(), () ->
            new MilkFluidBlock(MILK_FLUID_STILL.get(), BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).replaceable().liquid().noCollission().pushReaction(PushReaction.DESTROY).strength(100.0F).noLootTable()));

    public static net.neoforged.neoforge.fluids.BaseFlowingFluid.Properties createMilkProperties() {
        return new net.neoforged.neoforge.fluids.BaseFlowingFluid.Properties(NeoForgeMod.MILK_TYPE, MILK_FLUID_STILL, MILK_FLUID_FLOWING).bucket(() -> Items.MILK_BUCKET).block(MILK_FLUID_BLOCK);
    }

    public static final DeferredHolder<Fluid, Fluid> ECTOPLASM_FLUID_FLOWING = FLUIDS.register("ectoplasm_fluid_flowing",
            () -> new BaseFlowingFluid.Flowing(ModFluids.ECTOPLASM_FLUID_PROPERTIES));

    public static final DeferredHolder<Fluid, FlowingFluid> ECTOPLASM_FLUID_STILL = FLUIDS.register("ectoplasm_fluid_still",
            () -> new BaseFlowingFluid.Source(ModFluids.ECTOPLASM_FLUID_PROPERTIES));

    public static final Supplier<LiquidBlock> ECTOPLASM_FLUID_BLOCK = BLOCKS.register("ectoplasm_fluid_still",
            () -> new EctoplasmFluidBlock(ECTOPLASM_FLUID_STILL.get(), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).replaceable().liquid().noCollission().pushReaction(PushReaction.DESTROY).strength(100.0F).noLootTable().lightLevel(state -> 14)));

    public static final Supplier<BucketItem> ECTOPLASM_FLUID = ITEMS.register("ectoplasm_fluid_bucket",
            () -> new BucketItem(ModFluids.ECTOPLASM_FLUID_STILL.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    private static final BaseFlowingFluid.Properties ECTOPLASM_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(
            FluidTypes.ECTOPLASM_FLUID, ECTOPLASM_FLUID_STILL, ECTOPLASM_FLUID_FLOWING).bucket(ECTOPLASM_FLUID).block(ECTOPLASM_FLUID_BLOCK);

    public static void register(IEventBus bus) {
        FLUIDS.register(bus);
    }

}