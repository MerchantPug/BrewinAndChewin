package umpaz.brewinandchewin.common.mixin.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.SignText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.registry.BnCEffects;

import java.util.Arrays;
import java.util.Random;


@Mixin(SignRenderer.class)
public class TipsySignRendererMixin {


   @ModifyVariable(method = "renderSignText(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/SignText;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IIIZ)V", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
   private SignText brewinandchewin$renderSignText(SignText signText) {

      if ( BnCConfiguration.SIGN_SCRAMBLE.get() )
         if ( Minecraft.getInstance().player != null )
            if ( Minecraft.getInstance().player.hasEffect(BnCEffects.TIPSY.get()) && Minecraft.getInstance().player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() >= BnCConfiguration.LEVEL_SIGN_SCRAMBLE.get() ) {
               Random random = new Random(0);
               for ( int i = 0; i < 4; i++ ) {
                  Component line = signText.getMessage(i, false);
                  if ( line.getString().length() <= 1 ) continue;
                  StringBuilder text = new StringBuilder(line.getString());

                  int amplifier = Minecraft.getInstance().player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() - BnCConfiguration.LEVEL_SIGN_SCRAMBLE.get();
                  int amnt = (int) ( ( amplifier + 1 ) * ( text.length() / ( 10f - BnCConfiguration.LEVEL_SIGN_SCRAMBLE.get() ) ) ) + random.nextInt(5);

                  for ( int j = 0; j < amnt; j++ ) {
                      // pick a random word
                      String[] words = text.toString().split(" ");
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
                      char temp = text.charAt(index);
                      text.setCharAt(index, text.charAt(newIndex));
                      text.setCharAt(newIndex, temp);
                  }

                  signText = signText.setMessage(i, Component.literal(text.toString()));
               }

            }

      return signText;
   }
}
