package umpaz.brewinandchewin.client;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import static umpaz.brewinandchewin.common.effects.TipsyEffect.durationFromAmplifier;

public class TipsyEffects {
   public static void init() {
      MinecraftForge.EVENT_BUS.register(new TipsyEffects());
   }
   @SubscribeEvent
   public void whatsyourname(RenderNameTagEvent event) {
      if ( BnCConfiguration.NAME_SCRAMBLE.get() )
         if ( Minecraft.getInstance().player != null ) {
            if ( Minecraft.getInstance().player.hasEffect(BnCEffects.TIPSY.get()) && Minecraft.getInstance().player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() >= BnCConfiguration.LEVEL_NAME_SCRAMBLE.get() ) {
               int amplifier = Minecraft.getInstance().player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() - BnCConfiguration.LEVEL_NAME_SCRAMBLE.get();
               StringBuilder textBuilder = new StringBuilder(event.getContent().getString());
               Random random = new Random(event.getEntity().getUUID().hashCode());
               int amount = (int) ( ( amplifier + 1 ) * ( ( textBuilder.length() ) / 10f ) ) + random.nextInt(5);
               int range = amplifier + 1;
               for ( int i = 0; i < amount; i++ ) {

                  // pick a random character
                  int index = random.nextInt(0, textBuilder.length() - 1);
                  // pick an index within range
                  int newIndex = Math.min(Math.max(0, index + random.nextInt(-range, range)), textBuilder.length() - 1);
                  // swap the characters

                  char temp = textBuilder.charAt(index);
                  textBuilder.setCharAt(index, textBuilder.charAt(newIndex));
                  textBuilder.setCharAt(newIndex, temp);
               }
               event.setContent(Component.literal(textBuilder.toString()));
            }
         }
   }


   @SubscribeEvent
   public void tipsySleepCure( SleepFinishedTimeEvent event ) {
      List<? extends Player> players = event.getLevel().players();
      for ( Player player : players ) {
         if ( player.hasEffect(BnCEffects.TIPSY.get()) ) {
            int amplifier = player.getEffect(BnCEffects.TIPSY.get()).getAmplifier();
            int duration = player.getEffect(BnCEffects.TIPSY.get()).getDuration();
            player.removeEffect(BnCEffects.TIPSY.get());
            if ( amplifier > 0 ) {
               int newAmp = ( ( amplifier + 1 ) / 2 ) - 1;
               int newDur = duration / 2;
               if ( newDur > 0 && newAmp > 0 )
                  player.forceAddEffect(new MobEffectInstance(BnCEffects.TIPSY.get(), newDur, newAmp, false, false, true), player);

               if ( player.hasEffect(BnCEffects.HANGOVER.get()) )
                  duration += player.getEffect(BnCEffects.HANGOVER.get()).getDuration();
               player.forceAddEffect(new MobEffectInstance(BnCEffects.HANGOVER.get(), duration, amplifier, false, false, true), player);
            }
         }
      }
   }


   @SubscribeEvent
   public void stopNaturalRegen( LivingHealEvent event ) {
      LivingEntity entity = event.getEntity();
      if ( entity.hasEffect(BnCEffects.TIPSY.get()) && !entity.hasEffect(ModEffects.COMFORT.get()) ) {
         MobEffectInstance effect = entity.getEffect(BnCEffects.TIPSY.get());
         if ( effect.getAmplifier() >= 2 ) {
            event.setCanceled(true);
         }
      }
   }

   @SubscribeEvent
   public void reduceTipsy( MobEffectEvent.Expired event ) {
      LivingEntity player = event.getEntity();
      if ( event.getEffectInstance().getEffect().equals(BnCEffects.TIPSY.get()) ) {
         MobEffectInstance tipsy = event.getEffectInstance();
         MobEffectInstance hangover = player.getEffect(BnCEffects.HANGOVER.get());
         int amplifier = tipsy.getAmplifier();

         event.getEntity().removeEffect(BnCEffects.TIPSY.get());
         if ( tipsy.getAmplifier() - 1 > 1 ) {
            int prevAmp = hangover != null ? hangover.getAmplifier() : 0;
            int prevDur = hangover != null ? hangover.getDuration() : 0;
            player.forceAddEffect(new MobEffectInstance(BnCEffects.HANGOVER.get(), durationFromAmplifier(amplifier) + prevDur, Math.max(amplifier, prevAmp), false, false, true), player);
//            player.forceAddEffect(new MobEffectInstance(BnCEffects.TIPSY.get(), durationFromAmplifier(amplifier) / 2, ( amplifier / 2 ) - 1, false, false, true), player);
         }
      }
   }

//   @SubscribeEvent
//   public void reduceTipsy( LivingEntityUseItemEvent.Finish event ) {
//      LivingEntity player = event.getEntity();
//      if ( event.getItem().getItem() == Items.MILK_BUCKET ) {
//         if ( player.hasEffect(BnCEffects.TIPSY.get()) ) {
//            MobEffectInstance tipsy = player.getEffect(BnCEffects.TIPSY.get());
//            MobEffectInstance hangover = player.getEffect(BnCEffects.HANGOVER.get());
//            int amplifier = tipsy.getAmplifier();
//            player.forceAddEffect(new MobEffectInstance(BnCEffects.TIPSY.get(), tipsy.getDuration(), amplifier - 1, false, false, true), player);
//            int prevAmp = hangover != null ? hangover.getAmplifier() : 0;
//            int prevDur = hangover != null ? hangover.getDuration() : 0;
//            player.forceAddEffect(new MobEffectInstance(BnCEffects.HANGOVER.get(), durationFromAmplifier(amplifier) + prevDur, Math.max(amplifier, prevAmp), false, false, true), player);
//
//            if ( amplifier == 0 ) {
//               player.removeEffect(BnCEffects.TIPSY.get());
//            }
//         }
//      }
//   }

   @SubscribeEvent
   public void screwWithBreakSpeed( PlayerEvent.BreakSpeed event ) {
      if ( event.getEntity().hasEffect(BnCEffects.TIPSY.get()) ) {
         float speed = event.getNewSpeed();
         speed *= ( ( event.getEntity().getRandom().nextFloat() ) ) * ( event.getEntity().getEffect(BnCEffects.TIPSY.get()).getAmplifier() / 10f );
         event.setNewSpeed(speed);

         if ( event.getEntity().getRandom().nextInt(51 - Math.min(event.getEntity().getEffect(BnCEffects.TIPSY.get()).getAmplifier(), 10) * 5) == 0 ) {
            event.setCanceled(true);
         }
      }
   }

   // make the text you put in chat garbled
   @SubscribeEvent
   public void icanspeak( ClientChatEvent event ) {
      if ( BnCConfiguration.CHAT_SCRAMBLE.get() ) {

         Player player = Minecraft.getInstance().player;
         if ( player != null )
            if ( player.hasEffect(BnCEffects.TIPSY.get()) && player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() >= BnCConfiguration.LEVEL_CHAT_SCRAMBLE.get() ) {
               int amplifier = player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() - BnCConfiguration.LEVEL_CHAT_SCRAMBLE.get();
               StringBuilder textBuilder = new StringBuilder(event.getOriginalMessage());
               Random random = new Random(0);
               int amnt = (int) ( ( amplifier + 1 ) * ( ( textBuilder.length() ) / 10f ) ) + random.nextInt(5);
               int range = amplifier + 1;
               for ( int i = 0; i < amnt; i++ ) {

                  // pick a random character
                  int index = random.nextInt(0, textBuilder.length() - 1);
                  // pick an index within range
                  int newIndex = Math.min(Math.max(0, index + random.nextInt(-range, range)), textBuilder.length() - 1);
                  // swap the characters

                  char temp = textBuilder.charAt(index);
                  textBuilder.setCharAt(index, textBuilder.charAt(newIndex));
                  textBuilder.setCharAt(newIndex, temp);
               }
               event.setMessage(textBuilder.toString());
            }
      }
   }

   @SubscribeEvent
   public void icanspeak( ServerChatEvent event ) {
      if ( BnCConfiguration.CHAT_SCRAMBLE.get() ) {
      Player player = Minecraft.getInstance().player;
      if ( player != null )
         if ( player.hasEffect(BnCEffects.TIPSY.get()) && player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() >= BnCConfiguration.LEVEL_CHAT_SCRAMBLE.get() ) {
            StringBuilder textBuilder = new StringBuilder(event.getRawText());
            Random random = new Random(0);
            int firstSpace = ( textBuilder.indexOf("[") == 0 || textBuilder.indexOf("<") == 0 ) ? textBuilder.indexOf(" ") + 1 : 0;

            int amplifier = player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() - BnCConfiguration.LEVEL_CHAT_SCRAMBLE.get();
            int amnt = (int) ( ( amplifier + 1 ) * ( ( textBuilder.length() - firstSpace ) / 10f ) ) + random.nextInt(5);
            int range = amplifier + 1;
            for ( int i = 0; i < amnt; i++ ) {

               // pick a random character
               int index = random.nextInt(firstSpace, textBuilder.length() - 1);
               // pick an index within range
               int newIndex = Math.min(Math.max(firstSpace, index + random.nextInt(-range, range)), textBuilder.length() - 1);
               // swap the characters

               char temp = textBuilder.charAt(index);
               textBuilder.setCharAt(index, textBuilder.charAt(newIndex));
               textBuilder.setCharAt(newIndex, temp);
            }
            event.setMessage(Component.literal(textBuilder.toString()));
         }
      }
   }

}