package umpaz.brewinandchewin.client.gui;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.registry.BnCEffects;

import java.util.Random;

public class IntoxicatedHealthOverlay {
   protected static int healthIconsOffset;
   private static final ResourceLocation MOD_ICONS_TEXTURE = new ResourceLocation(BrewinAndChewin.MODID, "textures/gui/bnc_icons.png");

   public static void init() {
      MinecraftForge.EVENT_BUS.register(new IntoxicatedHealthOverlay());
   }


   static ResourceLocation PLAYER_HEALTH_ELEMENT = new ResourceLocation("minecraft", "player_health");

   @SubscribeEvent
   public void onRenderGuiOverlayPost( RenderGuiOverlayEvent.Pre event ) {
      if ( event.getOverlay() == GuiOverlayManager.findOverlay(PLAYER_HEALTH_ELEMENT) ) {
         Minecraft mc = Minecraft.getInstance();
         ForgeGui gui = (ForgeGui) mc.gui;
         if ( !mc.options.hideGui && gui.shouldDrawSurvivalElements() ) {
            if ( Minecraft.getInstance().player.getEffect(BnCEffects.INTOXICATED.get()) != null ) {
               renderIntoxicatedOverlay(gui, event.getGuiGraphics());
               event.setCanceled(true);
            }
         }
      }
   }

   public static void renderIntoxicatedOverlay( ForgeGui gui, GuiGraphics guiGraphics ) {
      if (!BnCConfiguration.INTOXICATED_HEALTH_OVERLAY.get() ) {
         return;
      }

      healthIconsOffset = gui.leftHeight + 10;
      Minecraft minecraft = Minecraft.getInstance();
      Player player = minecraft.player;

      if ( player == null ) {
         return;
      }
      int top = minecraft.getWindow().getGuiScaledHeight() - healthIconsOffset + 10;
      int left = minecraft.getWindow().getGuiScaledWidth() / 2 - 91;

      drawIntoxicatedOverlay(player, minecraft, guiGraphics, left, top);

   }

   public static void drawIntoxicatedOverlay( Player player, Minecraft minecraft, GuiGraphics graphics, int left, int top ) {
      int ticks = minecraft.gui.getGuiTicks();
      Random rand = new Random();
      rand.setSeed(ticks * 312871L);

      int health = Mth.ceil(player.getHealth());
      float absorb = Mth.ceil(player.getAbsorptionAmount());
      AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
      float healthMax = (float) attrMaxHealth.getValue();

      int hearts = Mth.ceil(( healthMax + absorb ) / 2.0f);
      int healthRows = Mth.ceil(hearts / 10.0F);

      RenderSystem.setShaderTexture(0, MOD_ICONS_TEXTURE);
      RenderSystem.enableBlend();


      // render hearts normally
      for ( int i = 0; i < hearts; ++i ) {
         int rowHeight = Math.max(10 - ( healthRows - 2 ), 3);

         int leftHeightOffset = 0; // This keeps the overlay on the topmost row of hearts

         int row = i / 10;
         int x = ( left + i % 10 * 8 ) + (int) ( Mth.cos(( ticks + i * 2 ) * 0.20F) * 2f );
         int y = ( top - row * rowHeight ) + (int) ( Mth.sin(( ticks + i * 2 ) * 0.25F) * 2f );

         int hInd = 0;
         if (i * 2 + 1 == health ) {
            hInd = 1;
         }
         else if (i * 2 + 1 > health) {
            hInd = 2;
         }
         graphics.blit(MOD_ICONS_TEXTURE, x, y - leftHeightOffset, hInd * 9, 0, 9, 9);
      }


//      for (int i = 0; i < healthMaxSingleRow; ++i) {
//         int column = i % 10;
//         int x = left + column * 8;
//         int y = top + leftHeightOffset;
////
//         if (health <= 4) y += rand.nextInt(2);
//         if (i == regen) y -= 2;
//
//         if (column == comfortSheen / 2) {
//            graphics.blit(MOD_ICONS_TEXTURE, x, y, 0, 0, textureWidth[comfortHeartFrame], 9);
//         }
//         if (column == (comfortSheen / 2) - 1 && comfortHeartFrame == 0) {
//            graphics.blit(MOD_ICONS_TEXTURE, x + 5, y, 5, 0, 4, 9);
//         }
//      }
//
      RenderSystem.disableBlend();
   }
}