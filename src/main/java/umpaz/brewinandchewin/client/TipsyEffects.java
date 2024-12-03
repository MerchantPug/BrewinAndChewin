package umpaz.brewinandchewin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.integration.appleskin.TipsyAppleSkinCompat;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.registry.BnCEffects;

import java.util.Arrays;
import java.util.Random;

@Mod.EventBusSubscriber(modid = BrewinAndChewin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TipsyEffects {
   public static void init() {
       if (ModList.get().isLoaded("appleskin"))
           MinecraftForge.EVENT_BUS.register(new TipsyAppleSkinCompat());
   }

   @SubscribeEvent
   public static void whatsYourName(RenderNameTagEvent event) {
      if ( BnCConfiguration.NAME_SCRAMBLE.get() )
         if ( Minecraft.getInstance().player != null ) {
            if ( Minecraft.getInstance().player.hasEffect(BnCEffects.TIPSY.get()) && Minecraft.getInstance().player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() >= BnCConfiguration.LEVEL_NAME_SCRAMBLE.get() ) {
               int amplifier = Minecraft.getInstance().player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() - BnCConfiguration.LEVEL_NAME_SCRAMBLE.get();
               StringBuilder textBuilder = new StringBuilder(event.getContent().getString());
               RandomSource random = Minecraft.getInstance().player.getRandom();
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
   public static void screwWithBreakSpeed( PlayerEvent.BreakSpeed event ) {
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
   public static void iCanSpeak( ClientChatEvent event ) {
      if ( BnCConfiguration.CHAT_SCRAMBLE.get() ) {
         Player player = Minecraft.getInstance().player;
         if ( player != null )
            if ( player.hasEffect(BnCEffects.TIPSY.get()) && player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() >= BnCConfiguration.LEVEL_CHAT_SCRAMBLE.get() ) {
               int amplifier = player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() - BnCConfiguration.LEVEL_CHAT_SCRAMBLE.get();
               StringBuilder textBuilder = new StringBuilder(event.getMessage());
               RandomSource random = Minecraft.getInstance().player.getRandom();
               int amnt = (int) ( ( amplifier + 1 ) * ( ( textBuilder.length() ) / 10f ) ) + random.nextInt(5);
               for ( int i = 0; i < amnt; i++ ) {
                   // pick a random word
                   String[] words = textBuilder.toString().split(" ");
                   int wordIndex = random.nextInt(words.length);
                   String word = words[wordIndex];

                   if (word.length() < 4)
                       continue;

                   int wordStart = Arrays.asList(words).subList(0, wordIndex).stream().mapToInt(String::length).sum() + wordIndex;

                   // pick a random character in the word, excluding the first and last letters
                   int index = wordStart + random.nextInt(1, Math.max(word.length() - 2, 2));
                   // pick an index within range
                   int newIndex = Mth.clamp(index + random.nextInt(Math.max(word.length() - 2, 2)), wordStart + 1, wordStart + word.length() - 2);

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
   public static void iCanHear( ClientChatReceivedEvent event ) {
       if (BnCConfiguration.CHAT_SCRAMBLE.get()) {
           Player player = Minecraft.getInstance().player;
           if (player != null && !event.isSystem() && event.getSender() != player.getUUID())
               if (player.hasEffect(BnCEffects.TIPSY.get()) && player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() >= BnCConfiguration.LEVEL_CHAT_SCRAMBLE.get()) {
                   StringBuilder textBuilder = new StringBuilder(event.getMessage().getString());
                   Random random = new Random(0);
                   int firstSpace = (textBuilder.indexOf("[") == 0 || textBuilder.indexOf("<") == 0) ? textBuilder.indexOf(" ") + 1 : 0;

                   int amplifier = player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() - BnCConfiguration.LEVEL_CHAT_SCRAMBLE.get();
                   int amnt = (int) ((amplifier + 1) * ((textBuilder.length() - firstSpace) / 10f)) + random.nextInt(5);
                   for (int i = 0; i < amnt; i++) {
                       // pick a random word
                       String[] words = textBuilder.toString().split(" ");
                       int wordIndex = random.nextInt(words.length);
                       String word = words[wordIndex];

                       if (word.length() < 4)
                           continue;

                       int wordStart = Arrays.asList(words).subList(0, wordIndex).stream().mapToInt(String::length).sum() + wordIndex;

                       // pick a random character in the word, excluding the first and last letters
                       int index = wordStart + random.nextInt(1, Math.max(word.length() - 2, 2));
                       // pick an index within range
                       int newIndex = Mth.clamp(index + random.nextInt(Math.max(word.length() - 2, 2)), wordStart + 1, wordStart + word.length() - 2);

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