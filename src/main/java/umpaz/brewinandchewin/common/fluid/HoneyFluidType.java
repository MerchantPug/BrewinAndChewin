package umpaz.brewinandchewin.common.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class HoneyFluidType extends FluidType {

    public static final ResourceLocation HONEY_FLUID_STILL_TEXTURE = new  ResourceLocation("block/honey_block_top"); //ResourceLocation(BrewinAndChewin.MODID, "block/honey_fluid_still");
    public static final ResourceLocation HONEY_FLUID_FLOWING_TEXTURE = new ResourceLocation("block/honey_block_top"); //ResourceLocation(BrewinAndChewin.MODID, "block/honey_fluid_flow");

    public HoneyFluidType() {
        super(FluidType.Properties.create()
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
        );
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture()
            {
                return HONEY_FLUID_STILL_TEXTURE;
            }

            @Override
            public ResourceLocation getFlowingTexture()
            {
                return HONEY_FLUID_FLOWING_TEXTURE;
            }
        });
    }
}