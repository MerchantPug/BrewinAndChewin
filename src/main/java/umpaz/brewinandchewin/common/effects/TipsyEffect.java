package umpaz.brewinandchewin.common.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.particle.DrunkBubbleParticleOptions;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import umpaz.brewinandchewin.common.tag.BnCTags;

@Mod.EventBusSubscriber(modid = BrewinAndChewin.MODID)
public class TipsyEffect extends MobEffect {

   public TipsyEffect() {
      super(MobEffectCategory.NEUTRAL, 13208334);
   }

   @SubscribeEvent
   public static void lowerTipsiness(MobEffectEvent.Remove event) {
       LivingEntity entity = event.getEntity();
       MobEffectInstance effectInstance = event.getEffectInstance();

       if (event.getEffect() != BnCEffects.TIPSY.get() || !entity.isUsingItem() || !entity.getItemInHand(entity.getUsedItemHand()).is(BnCTags.TIPSY_REDUCER))
           return;

       entity.forceAddEffect(new MobEffectInstance(event.getEffect(), effectInstance.getDuration() == -1 ? -1 : effectInstance.getDuration() / 2, effectInstance.getAmplifier() / 2, effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon()), null);
       event.setCanceled(true);
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
}