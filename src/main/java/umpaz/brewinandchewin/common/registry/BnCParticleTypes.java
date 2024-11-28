package umpaz.brewinandchewin.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.particle.DrunkBubbleParticleOptions;

public class BnCParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, BrewinAndChewin.MODID);

    public static final RegistryObject<SimpleParticleType> FOG = PARTICLE_TYPES.register("fog",
            () -> new SimpleParticleType(true));


   public static final RegistryObject<ParticleType<DrunkBubbleParticleOptions>> DRUNK_BUBBLE = PARTICLE_TYPES.register("drunk_bubble", () -> new ParticleType<>(false, DrunkBubbleParticleOptions.DESERIALIZER) {
      @Override
      public Codec<DrunkBubbleParticleOptions> codec() {
         return DrunkBubbleParticleOptions.CODEC;
      }
   });
}
