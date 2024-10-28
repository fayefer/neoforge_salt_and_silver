package net.fayefer.salt_and_silver;

import net.fayefer.salt_and_silver.block.ModBlocks;
import net.fayefer.salt_and_silver.block.custom.EctoplasmCauldron;
import net.fayefer.salt_and_silver.block.custom.MilkCauldron;
import net.fayefer.salt_and_silver.fluid.FluidTypes;
import net.fayefer.salt_and_silver.fluid.MilkPlacement;
import net.fayefer.salt_and_silver.fluid.ModFluids;
import net.fayefer.salt_and_silver.fluid.SaltAndSilverFluidType;
import net.fayefer.salt_and_silver.item.ModCreativeModeTabs;
import net.fayefer.salt_and_silver.item.ModItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(SaltAndSilver.MOD_ID)
public class SaltAndSilver {
    public static final String MOD_ID = "salt_and_silver";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SaltAndSilver(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        NeoForgeMod.enableMilkFluid();
        NeoForge.EVENT_BUS.addListener(MilkPlacement::onRightClick);

        ModCreativeModeTabs.register(modEventBus);

        FluidTypes.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);
        ModFluids.VANILLA_BLOCKS.register(modEventBus);
        ModFluids.BLOCKS.register(modEventBus);
        ModFluids.ITEMS.register(modEventBus);

        MilkCauldron.init();
        EctoplasmCauldron.init();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.RAW_IMPURE_SILVER);
            event.accept(ModItems.IMPURE_SILVER_INGOT);
            event.accept(ModItems.SILVER_INGOT);
            event.accept(ModItems.SILVER_NUGGET);

            event.accept(ModItems.ECTOPLASM);

            event.accept(ModFluids.ECTOPLASM_FLUID.get());
        }
        if(event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.RAW_IMPURE_SILVER_BLOCK);
            event.accept(ModBlocks.IMPURE_SILVER_ORE);
            event.accept(ModBlocks.DEEPSLATE_IMPURE_SILVER_ORE);
            event.accept(ModBlocks.SALT_BLOCK);
        }
        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.IMPURE_SILVER_BLOCK);
            event.accept(ModBlocks.SILVER_BLOCK);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
            for (DeferredHolder<FluidType, SaltAndSilverFluidType> fluid : SaltAndSilverFluidType.registeredFluids) {
                event.registerFluidType(fluid.get().register(), fluid.get());
            }
        }
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.MILK_FLUID_STILL.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.MILK_FLUID_FLOWING.get(), RenderType.translucent());

            event.enqueueWork(() -> {
                ItemBlockRenderTypes.setRenderLayer(ModFluids.ECTOPLASM_FLUID_FLOWING.get(), RenderType.TRANSLUCENT);
                ItemBlockRenderTypes.setRenderLayer(ModFluids.ECTOPLASM_FLUID_STILL.get(), RenderType.TRANSLUCENT);
            });
        }
    }
}
