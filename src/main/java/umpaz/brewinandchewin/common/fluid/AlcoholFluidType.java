package umpaz.brewinandchewin.common.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class AlcoholFluidType extends FluidType {

    public static final ResourceLocation FLUID_STILL_TEXTURE = new  ResourceLocation("block/water_still"); //ResourceLocation(BrewinAndChewin.MODID, "block/honey_fluid_still");
    public static final ResourceLocation FLUID_FLOWING_TEXTURE = new ResourceLocation("block/water_flow"); //ResourceLocation(BrewinAndChewin.MODID, "block/honey_fluid_flow");
    private final int tintColor;

    public AlcoholFluidType(int tintColor) {
        super(FluidType.Properties.create()
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
        );
        this.tintColor = tintColor;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture()
            {
                return FLUID_STILL_TEXTURE;
            }

            @Override
            public ResourceLocation getFlowingTexture()
            {
                return FLUID_FLOWING_TEXTURE;
            }

            @Override
            public int getTintColor() {
                return tintColor;
            }
        });
    }
}