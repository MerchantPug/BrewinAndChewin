package umpaz.brewinandchewin.common.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import umpaz.brewinandchewin.client.particle.DrunkBubbleParticleOptions;

import java.util.ArrayList;
import java.util.List;

public class TipsyEffect extends MobEffect {

   public TipsyEffect() {
      super(MobEffectCategory.NEUTRAL, 13208334);
   }

   @Override
   public void applyEffectTick( LivingEntity entity, int amplifier ) {
      if ( entity.level().isClientSide && entity.level().random.nextInt(13 - amplifier) == 0 ) {
         entity.level().addParticle(getParticle(), entity.getRandomX(1.0D), entity.getEyeY() - entity.getRandom().nextDouble() + .25d, entity.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
      }
   }

   @Override
   public double getAttributeModifierValue( int pAmplifier, AttributeModifier pModifier ) {
      return super.getAttributeModifierValue(pAmplifier, pModifier);
   }

   public ParticleOptions getParticle() {
      return new DrunkBubbleParticleOptions(new Vector3f(( ( getColor() >> 16 ) & 0xFF ) / 255f, ( ( getColor() >> 8 ) & 0xFF ) / 255f, ( ( getColor() ) & 0xFF ) / 255f), 0.25f);
   }


   @Override
   public boolean isDurationEffectTick( int duration, int amplifier ) {
      return true;
   }

   @Override
   public List<ItemStack> getCurativeItems() {
      return new ArrayList<>();
   }
}