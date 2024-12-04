package umpaz.brewinandchewin.client.gui;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.NourishmentHungerOverlay;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Random;

public class IntoxicatedFoodOverlay {
   protected static int foodIconsOffset;
   public static final ResourceLocation MOD_ICONS_TEXTURE = new ResourceLocation(BrewinAndChewin.MODID, "textures/gui/bnc_icons.png");
    private static final ResourceLocation NOURISHMENT_ICONS_TEXTURE = new ResourceLocation("farmersdelight", "textures/gui/fd_icons.png");

   public static void init() {
      MinecraftForge.EVENT_BUS.register(new IntoxicatedFoodOverlay());
   }


   static ResourceLocation FOOD_LEVEL_ELEMENT = new ResourceLocation("minecraft", "food_level");

   @SubscribeEvent
   public void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
      if ( event.getOverlay() == GuiOverlayManager.findOverlay(FOOD_LEVEL_ELEMENT) ) {
         Minecraft mc = Minecraft.getInstance();
         ForgeGui gui = (ForgeGui) mc.gui;
         if ( !mc.options.hideGui && gui.shouldDrawSurvivalElements() ) {
            if (mc.player.hasEffect(BnCEffects.INTOXICATED.get())) {
               renderIntoxicatedOverlay(gui, event.getGuiGraphics());
            }
         }
      }
   }

   public static void renderIntoxicatedOverlay( ForgeGui gui, GuiGraphics guiGraphics ) {
      if (!BnCConfiguration.INTOXICATED_FOOD_OVERLAY.get() ) {
         return;
      }

      foodIconsOffset = gui.rightHeight;
      Minecraft minecraft = Minecraft.getInstance();
      Player player = minecraft.player;

      if ( player == null ) {
         return;
      }
      int top = minecraft.getWindow().getGuiScaledHeight() - foodIconsOffset;
      int right = minecraft.getWindow().getGuiScaledWidth() / 2 + 91;

      drawIntoxicatedOverlay(player, minecraft, guiGraphics, right, top);
   }

   public static void drawIntoxicatedOverlay( Player player, Minecraft minecraft, GuiGraphics graphics, int right, int top ) {
      int ticks = minecraft.gui.getGuiTicks();
      Random rand = new Random();
      rand.setSeed(ticks * 312871L);

      RenderSystem.setShaderTexture(0, MOD_ICONS_TEXTURE);
      RenderSystem.enableBlend();

      ResourceLocation texture = player.hasEffect(ModEffects.NOURISHMENT.get()) ? NOURISHMENT_ICONS_TEXTURE : MOD_ICONS_TEXTURE;

      for ( int i = 0; i < 10; ++i ) {
         int x = ( right - i * 8 - 9 ) + (int) ( Mth.cos(( ticks + i * 2 ) * 0.20F) * 2f );
         int y = ( top ) + (int) ( Mth.sin(( ticks + i * 2 ) * 0.25F) * 2f );

         float effectiveHungerOfBar = (float)player.getFoodData().getFoodLevel() / 2.0F - (float)i;
          boolean isPlayerHealingWithSaturationAndNourishment =
                  player.hasEffect(ModEffects.NOURISHMENT.get()) &&
                  player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
                          && player.isHurt()
                          && player.getFoodData().getFoodLevel() >= 18;
         int naturalHealingOffset = isPlayerHealingWithSaturationAndNourishment ? 18 : 0;

         graphics.blit(texture, x, y, 0, 0, 9, 9);

         if (effectiveHungerOfBar >= 1.0F) {
             graphics.blit(texture, x, y, 18 + naturalHealingOffset, 0, 9, 9);
         } else if ((double)effectiveHungerOfBar >= (double)0.5F) {
             graphics.blit(texture, x, y, 9 + naturalHealingOffset, 0, 9, 9);
         }
      }

      RenderSystem.disableBlend();
   }
}