package umpaz.brewinandchewin.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;
import umpaz.brewinandchewin.common.registry.BnCParticleTypes;

public class DrunkBubbleParticleOptions extends DustParticleOptionsBase {
   public static final Codec<DrunkBubbleParticleOptions> CODEC;
   public static final ParticleOptions.Deserializer<DrunkBubbleParticleOptions> DESERIALIZER;


   public DrunkBubbleParticleOptions( Vector3f color, float size ) {
      super(color, size);
   }

   @Override
   public ParticleType<DrunkBubbleParticleOptions> getType() {
      return BnCParticleTypes.DRUNK_BUBBLE.get();
   }

   static {
      CODEC = RecordCodecBuilder.create(( p_253370_ ) -> {
         return p_253370_.group(ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(( p_253371_ ) -> {
            return p_253371_.color;
         }), Codec.FLOAT.fieldOf("scale").forGetter(( p_175795_ ) -> {
            return p_175795_.scale;
         })).apply(p_253370_, DrunkBubbleParticleOptions::new);
      });
      DESERIALIZER = new ParticleOptions.Deserializer<>() {
         public DrunkBubbleParticleOptions fromCommand( ParticleType<DrunkBubbleParticleOptions> p_123689_, StringReader p_123690_ ) throws CommandSyntaxException {
            Vector3f $$2 = DrunkBubbleParticleOptions.readVector3f(p_123690_);
            p_123690_.expect(' ');
            float $$3 = p_123690_.readFloat();
            return new DrunkBubbleParticleOptions($$2, $$3);
         }

         public DrunkBubbleParticleOptions fromNetwork( ParticleType<DrunkBubbleParticleOptions> p_123692_, FriendlyByteBuf p_123693_ ) {
            return new DrunkBubbleParticleOptions(DrunkBubbleParticleOptions.readVector3f(p_123693_), p_123693_.readFloat());
         }
      };
   }


}
