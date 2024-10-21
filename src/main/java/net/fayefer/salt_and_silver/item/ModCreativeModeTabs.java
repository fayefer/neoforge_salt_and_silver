package net.fayefer.salt_and_silver.item;

import net.fayefer.salt_and_silver.SaltAndSilver;
import net.fayefer.salt_and_silver.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SaltAndSilver.MOD_ID);

    public static final Supplier<CreativeModeTab> SALT_AND_SILVER_TAB = CREATIVE_MODE_TAB.register("salt_and_silver_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.IMPURE_SILVER_INGOT.get()))
                    .title(Component.translatable("creativetab.salt_and_silver.salt_and_silver"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.RAW_IMPURE_SILVER);
                        output.accept(ModItems.IMPURE_SILVER_INGOT);
                        output.accept(ModItems.SILVER_INGOT);
                        output.accept(ModItems.SILVER_NUGGET);

                        output.accept(ModItems.ECTOPLASM);

                        output.accept(ModBlocks.RAW_IMPURE_SILVER_BLOCK);
                        output.accept(ModBlocks.IMPURE_SILVER_BLOCK);
                        output.accept(ModBlocks.IMPURE_SILVER_ORE);
                        output.accept(ModBlocks.DEEPSLATE_IMPURE_SILVER_ORE);
                        output.accept(ModBlocks.SILVER_BLOCK);
                        output.accept(ModBlocks.SALT_BLOCK);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
