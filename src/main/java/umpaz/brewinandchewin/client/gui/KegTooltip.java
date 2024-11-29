package umpaz.brewinandchewin.client.gui;


import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix4f;
import umpaz.brewinandchewin.client.utility.BnCFluidItemDisplayUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class KegTooltip implements ClientTooltipComponent {
   private static final int ITEM_SIZE = 16;
   private static final int MARGIN = 4;

   private final int textSpacing = Minecraft.getInstance().font.lineHeight + 1;
   private final FluidStack mealStack;

   public KegTooltip( KegTooltipComponent tooltip ) {
      this.mealStack = tooltip.mealStack;
   }

   @Override
   public int getHeight() {
      return mealStack.isEmpty() ? textSpacing : textSpacing + ITEM_SIZE;
   }

   @Override
   public int getWidth( Font font ) {
      if ( !mealStack.isEmpty() ) {
         MutableComponent textServingsOf = mealStack.getAmount() == 250
                 ? TextUtils.getTranslation("tooltip.cooking_pot.single_serving")
                 : TextUtils.getTranslation("tooltip.cooking_pot.many_servings", mealStack.getAmount() / 250);
         return Math.max(font.width(textServingsOf), font.width(mealStack.getDisplayName()) + 20);
      }
      else {
         return font.width(TextUtils.getTranslation("tooltip.cooking_pot.empty"));
      }
   }

   @Override
   public void renderImage( Font font, int mouseX, int mouseY, GuiGraphics gui ) {
      if ( mealStack.isEmpty() ) return;

      ItemStack itemDisplay = BnCFluidItemDisplayUtils.getFluidItemDisplay(mealStack.getFluid());
      if (itemDisplay.isEmpty()) {
          IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(mealStack.getFluid());
          ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(mealStack);
          if ( stillTexture == null )
              return;

          TextureAtlasSprite sprite =
                  Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
          int tintColor = fluidTypeExtensions.getTintColor(mealStack);

          float alpha = ( ( tintColor >> 24 ) & 0xFF ) / 255f;
          float red = ( ( tintColor >> 16 ) & 0xFF ) / 255f;
          float green = ( ( tintColor >> 8 ) & 0xFF ) / 255f;
          float blue = ( tintColor & 0xFF ) / 255f;
          gui.blit(mouseX, mouseY + 9, 0, 16, 16, sprite, red, green, blue, alpha);
          return;
      }
      gui.renderItem(itemDisplay, mouseX, mouseY + 9);
   }

   @Override
   public void renderText( Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource ) {
      Integer color = ChatFormatting.GRAY.getColor();
      int gray = color == null ? -1 : color;

      if ( !mealStack.isEmpty() ) {
         MutableComponent textServingsOf = mealStack.getAmount() == 250
                 ? TextUtils.getTranslation("tooltip.cooking_pot.single_serving")
                 : TextUtils.getTranslation("tooltip.cooking_pot.many_servings", mealStack.getAmount() / 250);

         font.drawInBatch(textServingsOf, (float) x, (float) y, gray, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
         font.drawInBatch(mealStack.getDisplayName(), x + ITEM_SIZE + MARGIN, y + textSpacing + MARGIN, -1, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
      }
      else {
         MutableComponent textEmpty = TextUtils.getTranslation("tooltip.cooking_pot.empty");
         font.drawInBatch(textEmpty, x, y, gray, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
      }
   }

   public static record KegTooltipComponent( FluidStack mealStack ) implements TooltipComponent {
   }
}