package umpaz.brewinandchewin.common.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import umpaz.brewinandchewin.client.particle.DrunkBubbleParticleOptions;

import java.util.ArrayList;
import java.util.List;

public class TipsyEffect extends MobEffect {

   public TipsyEffect() {
      super(MobEffectCategory.HARMFUL, 13208334);
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

   private static final String TIPSY_ATTACK_DAMAGE = "6b4c6410-c7ec-4985-9e5b-6543ecf7cdf1";
   private static final String TIPSY_ATTACK_SPEED = "19781790-2ad0-448e-bef9-22def6114aa3";

   @Override
   public void addAttributeModifiers( LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier ) {
      this.addAttributeModifier(Attributes.ATTACK_DAMAGE, TIPSY_ATTACK_DAMAGE, Mth.clamp(pAmplifier / 3, 0, 3), AttributeModifier.Operation.ADDITION);
      this.addAttributeModifier(Attributes.ATTACK_SPEED, TIPSY_ATTACK_SPEED, Mth.clamp(pAmplifier / 3, 0, 3) * -0.01, AttributeModifier.Operation.ADDITION);

      super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
   }
}