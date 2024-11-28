package umpaz.brewinandchewin.common.mixin.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import vectorwing.farmersdelight.client.renderer.CanvasSignRenderer;

import java.util.Random;


@Mixin(SignRenderer.class)
public class TipsySignRendererMixin {


   @ModifyVariable(method = "Lnet/minecraft/client/renderer/blockentity/SignRenderer;renderSignText(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/SignText;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IIIZ)V", at = @At(value = "HEAD"), ordinal = 0)
   private SignText renderSignText( SignText signText ) {

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
                  int range = amplifier + 1;

                  for ( int j = 0; j < amnt; j++ ) {

                     // pick a random character
                     int index = random.nextInt(0, text.length() - 1);
                     // pick an index within range
                     int newIndex = Math.min(Math.max(0, index + random.nextInt(-range, range)), text.length() - 1);
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
