package umpaz.brewinandchewin.common.mixin.client;


import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.registry.BnCEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(ChatComponent.class)
public class TipsyChatComponentMixin {

   @Shadow
   @Final
   private Minecraft minecraft;

   @Redirect(method = "Lnet/minecraft/client/gui/components/ChatComponent;render(Lnet/minecraft/client/gui/GuiGraphics;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;III)I"))
   private int renderMixin( GuiGraphics guiGraphics, Font font, FormattedCharSequence string, int x, int y, int color ) {
      if ( BnCConfiguration.CHAT_SCRAMBLE.get() )
         if ( this.minecraft.player.hasEffect(BnCEffects.TIPSY.get()) && this.minecraft.player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() >= BnCConfiguration.LEVEL_CHAT_SCRAMBLE.get() ) {
            FormattedCharSequence text = FormattedCharSequence.EMPTY;
            // create FormattedCharSink to append text
            StringBuilder textBuilder = new StringBuilder();
            List<Style> styles = new ArrayList<>();
            FormattedCharSink builder = ( positionInCurrentSequence, style, codePoint ) -> {
               textBuilder.appendCodePoint(codePoint);
               styles.add(style);
               return true;
            };
            string.accept(builder);

            Random random = new Random(0);

            int firstSpace = ( textBuilder.indexOf("[") == 0 || textBuilder.indexOf("<") == 0 ) ? textBuilder.indexOf(" ") + 1 : 0;

            int amplifier = this.minecraft.player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() - BnCConfiguration.LEVEL_NAME_SCRAMBLE.get();
            int amnt = (int) ( ( amplifier + 1 ) * ( ( textBuilder.length() - firstSpace ) / ( 10f - BnCConfiguration.LEVEL_NAME_SCRAMBLE.get() ) ) ) + random.nextInt(5);
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

            for ( int i = 0; i < styles.size(); i++ ) {
               text = FormattedCharSequence.composite(text, FormattedCharSequence.forward(String.valueOf(textBuilder.charAt(i)), styles.get(i)));
            }
            string = text;
         }

      return guiGraphics.drawString(font, string, x, y, color);
   }


}
