package net.fayefer.salt_and_silver.fluid;

import net.fayefer.salt_and_silver.SaltAndSilver;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector3f;

public class FluidTypes {
    public static final DeferredRegister<FluidType> TYPES = DeferredRegister
            .create(NeoForgeRegistries.FLUID_TYPES, SaltAndSilver.MOD_ID);

    public static final DeferredHolder<FluidType, SaltAndSilverFluidType> ECTOPLASM_FLUID =
            SaltAndSilverFluidType.Builder.create("ectoplasm_fluid")
                    .setFlowingTexture(ResourceLocation.parse("salt_and_silver:block/ectoplasm_fluid_flowing"))
                    .setStillTexture(ResourceLocation.parse("salt_and_silver:block/ectoplasm_fluid_still"))
                    .setOverlayTexture(ResourceLocation.parse("salt_and_silver:block/ectoplasm_fluid_overlay"))
                    .setFogColor(new Vector3f(0f / 255f, 255f / 255f, 255f / 255f))
                    .buildHolder(TYPES);

    public static void register(IEventBus bus) {
        TYPES.register(bus);
    }
}
