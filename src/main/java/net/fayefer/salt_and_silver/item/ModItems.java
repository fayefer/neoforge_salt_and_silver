package net.fayefer.salt_and_silver.item;

import net.fayefer.salt_and_silver.SaltAndSilver;
import net.fayefer.salt_and_silver.item.custom.EctoplasmItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SaltAndSilver.MOD_ID);

    public static final DeferredItem<Item> RAW_IMPURE_SILVER = ITEMS.register("raw_impure_silver",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> IMPURE_SILVER_INGOT = ITEMS.register("impure_silver_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SILVER_INGOT = ITEMS.register("silver_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SILVER_NUGGET = ITEMS.register("silver_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ECTOPLASM = ITEMS.register("ectoplasm",
            () -> new EctoplasmItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

