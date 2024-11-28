package umpaz.brewinandchewin.common.effects;

import com.google.common.eventbus.Subscribe;
import mezz.jei.api.helpers.IColorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;
import org.lwjgl.stb.STBPerlin;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.particle.DrunkBubbleParticleOptions;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import umpaz.brewinandchewin.common.registry.BnCParticleTypes;
import vectorwing.farmersdelight.common.effect.ComfortEffect;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = BrewinAndChewin.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TipsyEffect extends MobEffect {

   public TipsyEffect() {
      super(MobEffectCategory.NEUTRAL, 13208334);
   }

   @Override
   public void applyEffectTick( LivingEntity entity, int amplifier ) {
      MobEffectInstance effect = entity.getEffect(BnCEffects.TIPSY.get());

      // if tipsy and has hangover, reduce hangover
      if ( !entity.level().isClientSide() )
         if ( entity.hasEffect(BnCEffects.HANGOVER.get()) ) {
            MobEffectInstance hangover = entity.getEffect(BnCEffects.HANGOVER.get());
            if ( entity.level().random.nextInt(Math.max(1500 - effect.getAmplifier() * 100, 100)) == 0 ) {
               if ( hangover.getAmplifier() > 0 ) {
                  int prevAmp = hangover != null ? hangover.getAmplifier() : 0;
                  int prevDur = hangover != null ? hangover.getDuration() : 0;
                  entity.forceAddEffect(new MobEffectInstance(BnCEffects.HANGOVER.get(), prevDur, prevAmp - 1, false, false, true), entity);
               }
               else {
                  entity.removeEffect(BnCEffects.HANGOVER.get());
               }

            }

         }
         else {
            // give them intoxication
            if ( entity.getRandom().nextInt(600) == 0 ) {
               int duration = ( !entity.hasEffect(BnCEffects.INTOXICATED.get()) ) ? 600 : Math.min(entity.getEffect(BnCEffects.INTOXICATED.get()).getDuration() + 600, 6000);
               int amp = ( !entity.hasEffect(BnCEffects.INTOXICATED.get()) ) ? 0 : Math.max(entity.getEffect(BnCEffects.INTOXICATED.get()).getAmplifier(), effect.getAmplifier());
               entity.forceAddEffect(new MobEffectInstance(BnCEffects.INTOXICATED.get(), duration, amp, false, false, true), entity);
            }
         }

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

   public static int durationFromAmplifier( int amplifier ) {
      return switch ( amplifier ) {
         case 1 -> 200;
         case 2 -> 300;
         case 3 -> 600;
         case 4 -> 1200;
         case 5 -> 2400;
         case 6 -> 3600;
         case 7 -> 4800;
         case 8 -> 6000;
         case 9 -> 8000;

         default -> 600;
      };
   }



   @Override
   public void addAttributeModifiers( LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier ) {
      this.addAttributeModifier(Attributes.ATTACK_DAMAGE, pLivingEntity.getStringUUID(), pAmplifier, AttributeModifier.Operation.ADDITION);
      this.addAttributeModifier(Attributes.ATTACK_SPEED, pLivingEntity.getStringUUID(), -( pAmplifier ) / 150f, AttributeModifier.Operation.ADDITION);
      this.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, pLivingEntity.getStringUUID(), ( pAmplifier ) / 20f, AttributeModifier.Operation.ADDITION);

      super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
   }
   public static void addTipsyEffect( LivingEntity entity, int duration, int amplifier ) {
      if ( !entity.hasEffect(BnCEffects.TIPSY.get()) ) {
         entity.forceAddEffect(new MobEffectInstance(BnCEffects.TIPSY.get(), duration * 1200, amplifier - 1, false, false, true), entity);
      }
      else if ( entity.hasEffect(BnCEffects.TIPSY.get()) ) {
         MobEffectInstance effect = entity.getEffect(BnCEffects.TIPSY.get());
         entity.forceAddEffect(new MobEffectInstance(BnCEffects.TIPSY.get(), effect.getDuration() + ( duration * 600 ), Math.min(effect.getAmplifier() + amplifier, 9), false, false, true), entity);

      }
   }
}