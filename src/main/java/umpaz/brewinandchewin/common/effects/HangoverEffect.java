package umpaz.brewinandchewin.common.effects;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.particle.DrunkBubbleParticleOptions;
import umpaz.brewinandchewin.common.registry.BnCEffects;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = BrewinAndChewin.MODID)
public class HangoverEffect extends MobEffect {

   public HangoverEffect() {
      super(MobEffectCategory.HARMFUL, 0x58d045);
   }

   @SubscribeEvent
   public static void tipsySleepCure( SleepFinishedTimeEvent event ) {
      List<? extends Player> players = event.getLevel().players();
      for ( Player player : players ) {
         if ( player.hasEffect(BnCEffects.HANGOVER.get()) && !player.hasEffect(BnCEffects.TIPSY.get()) ) {
            int amplifier = player.getEffect(BnCEffects.HANGOVER.get()).getAmplifier();
            int duration = player.getEffect(BnCEffects.HANGOVER.get()).getDuration();
            if ( amplifier > 0 ) {
               int newAmp = ( ( amplifier + 1 ) / 2 ) - 1;
               int newDur = (int) ( duration / 1.5 );
               if ( newDur > 0 || newAmp > 0 ) {
                  // Insomnia lol
                  if ( player instanceof ServerPlayer playerMP ) {
                     StatsCounter statisticsManager = playerMP.getStats();
                     statisticsManager.increment(playerMP, Stats.CUSTOM.get(Stats.TIME_SINCE_REST), ( 24000 * ( newAmp + 1 ) ));
                  }

                  player.forceAddEffect(new MobEffectInstance(BnCEffects.HANGOVER.get(), newDur, newAmp, false, false, true), player);
               }
            }
         }
      }
   }

   @Override
   public void addAttributeModifiers( LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier ) {
      if ( pAmplifier > 10 )
         pAmplifier = 10;
      this.addAttributeModifier(Attributes.ATTACK_DAMAGE, pLivingEntity.getStringUUID(), -( pAmplifier + 1 ) / 5f, AttributeModifier.Operation.ADDITION);
      this.addAttributeModifier(Attributes.ATTACK_SPEED, pLivingEntity.getStringUUID(), -( pAmplifier + 1 ) / 125f, AttributeModifier.Operation.ADDITION);
      this.addAttributeModifier(Attributes.MOVEMENT_SPEED, pLivingEntity.getStringUUID(), -( pAmplifier + 1 ) / 2000f, AttributeModifier.Operation.ADDITION);
      this.addAttributeModifier(Attributes.LUCK, pLivingEntity.getStringUUID(), (double) 1 / pAmplifier, AttributeModifier.Operation.ADDITION);

      super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
   }


   @Override
   public void applyEffectTick( LivingEntity entity, int amplifier ) {
      MobEffectInstance effect = entity.getEffect(BnCEffects.HANGOVER.get());
      if ( amplifier > 10 )
         amplifier = 10;

      if ( entity.level().isClientSide && entity.level().random.nextInt(13 - amplifier) == 0 ) {
         entity.level().addParticle(getParticle(), entity.getRandomX(1.0D), entity.getEyeY() - entity.getRandom().nextDouble() + .25d, entity.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
      }
   }


//   @SubscribeEvent
//   public static void reduceHangover( LivingEntityUseItemEvent.Finish event ) {
//      LivingEntity player = event.getEntity();
//      if ( event.getItem().getItem() == Items.MILK_BUCKET ) {
//         if ( player.hasEffect(BnCEffects.HANGOVER.get()) ) {
//            int amplifier = player.getEffect(BnCEffects.HANGOVER.get()).getAmplifier();
//            int duration = player.getEffect(BnCEffects.HANGOVER.get()).getDuration();
//
//            player.forceAddEffect(new MobEffectInstance(BnCEffects.HANGOVER.get(), (int) ( duration / 1.5 ), amplifier - 1, false, false, true), player);
//            if ( amplifier == 0 ) {
//               player.removeEffect(BnCEffects.HANGOVER.get());
//            }
//         }
//      }
//   }

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
