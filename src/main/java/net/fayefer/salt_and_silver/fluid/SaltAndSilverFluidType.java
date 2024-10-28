package net.fayefer.salt_and_silver.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SaltAndSilverFluidType extends FluidType {
    public static final List<DeferredHolder<FluidType, SaltAndSilverFluidType>> registeredFluids = new ArrayList<>();
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final ResourceLocation overlayTexture;
    private final int tintColor;
    private final Vector3f fogColor;
    private final float fogStart;
    private final float fogEnd;

    private SaltAndSilverFluidType(final ResourceLocation stillTexture, final ResourceLocation flowingTexture, final ResourceLocation overlayTexture,
                                   final int tintColor, final Vector3f fogColor, float fogStart, float fogEnd, final Properties properties) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.overlayTexture = overlayTexture;
        this.tintColor = tintColor;
        this.fogColor = fogColor;
        this.fogStart = fogStart;
        this.fogEnd = fogEnd;
    }

    public ResourceLocation getStillTexture() {
        return stillTexture;
    }

    public ResourceLocation getFlowingTexture() {
        return flowingTexture;
    }

    public int getTintColor() {
        return tintColor;
    }

    public ResourceLocation getOverlayTexture() {
        return overlayTexture;
    }

    public Vector3f getFogColor() {
        return fogColor;
    }

    public float getFogStart() {
        return fogStart;
    }

    public float getFogEnd() {
        return fogEnd;
    }

    public IClientFluidTypeExtensions register() {
        return new IClientFluidTypeExtensions() {
            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return overlayTexture;
            }

            @Override
            public int getTintColor() {
                return tintColor;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(@NotNull Camera camera, float partialTick, @NotNull ClientLevel level, int renderDistance, float darkenWorldAmount, @NotNull Vector3f fluidFogColor) {
                return fogColor;
            }

            @Override
            public void modifyFogRender(@NotNull Camera camera, FogRenderer.@NotNull FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, @NotNull FogShape shape) {
                RenderSystem.setShaderFogStart(fogStart);
                RenderSystem.setShaderFogEnd(fogEnd);
            }
        };
    }

    public static class Builder {
        private final String name;
        private ResourceLocation stillTexture;
        private ResourceLocation flowingTexture;
        private ResourceLocation overlayTexture;
        private int tintColor;
        private Vector3f fogColor;
        private Properties properties;
        private float fogStart;
        private float fogEnd;

        public Builder(String name) {
            this.name = name;
            this.stillTexture = ResourceLocation.parse("block/water_still");
            this.flowingTexture = ResourceLocation.parse("block/water_flow");
            this.overlayTexture = ResourceLocation.parse("block/water_overlay");
            this.tintColor = 0xCC00FFFF;
            this.fogColor = new Vector3f(0, 1,1);
            this.fogStart = 1f;
            this.fogEnd = 3f;
            this.properties = Properties.create()
                    .canSwim(true)
                    .canDrown(true)
                    .canExtinguish(true)
                    .supportsBoating(true)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH);
        }

        public static Builder create(String name) {
            return new Builder(name);
        }

        public Builder setStillTexture(ResourceLocation texture) {
            this.stillTexture = texture;
            return this;
        }

        public Builder setFlowingTexture(ResourceLocation texture) {
            this.flowingTexture = texture;
            return this;
        }

        public Builder setOverlayTexture(ResourceLocation texture) {
            this.overlayTexture = texture;
            return this;
        }

        public Builder setTintColor(int color) {
            this.tintColor = color;
            return this;
        }

        public Builder setFogColor(Vector3f color) {
            this.fogColor = color;
            return this;
        }

        public Builder setProperties(Properties properties) {
            this.properties = properties;
            return this;
        }

        public Builder setFogStart(float distance) {
            this.fogStart = distance;
            return this;
        }

        public Builder setFogEnd(float distance) {
            this.fogEnd = distance;
            return this;
        }


        public DeferredHolder<FluidType, SaltAndSilverFluidType> buildHolder(DeferredRegister<FluidType> deferredRegister) {
            DeferredHolder<FluidType, SaltAndSilverFluidType> toReturn = deferredRegister.register(name, this::build);
            registeredFluids.add(toReturn);
            return toReturn;
        }

        public SaltAndSilverFluidType build() {
            return new SaltAndSilverFluidType(
                    stillTexture,
                    flowingTexture,
                    overlayTexture,
                    tintColor,
                    fogColor,
                    fogStart,
                    fogEnd,
                    properties);
        }
    }
}
