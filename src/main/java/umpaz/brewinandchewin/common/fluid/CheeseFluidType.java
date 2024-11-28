package umpaz.brewinandchewin.common.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import umpaz.brewinandchewin.BrewinAndChewin;

import java.util.function.Consumer;

public class CheeseFluidType extends FluidType {

    public static final ResourceLocation FLAXEN_STILL_TEXTURE = new ResourceLocation(BrewinAndChewin.MODID, "block/flaxen_cheese_still");
    public static final ResourceLocation FLAXEN_FLOWING_TEXTURE = new ResourceLocation(BrewinAndChewin.MODID, "block/flaxen_cheese_flow");
    public static final ResourceLocation SCARLET_STILL_TEXTURE = new ResourceLocation(BrewinAndChewin.MODID, "block/scarlet_cheese_still");
    public static final ResourceLocation SCARLET_FLOWING_TEXTURE = new ResourceLocation(BrewinAndChewin.MODID, "block/scarlet_cheese_flow");
    boolean type;
    public CheeseFluidType(boolean type) {
        super(FluidType.Properties.create()
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
        );
        this.type = type;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture()
            {
                return type ? FLAXEN_STILL_TEXTURE : SCARLET_STILL_TEXTURE;
            }

            @Override
            public ResourceLocation getFlowingTexture()
            {
                return type ? FLAXEN_FLOWING_TEXTURE : SCARLET_FLOWING_TEXTURE;
            }
        });
    }
}