package umpaz.brewinandchewin.client.integration.appleskin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.event.HUDOverlayEvent;
import squeek.appleskin.api.food.FoodValues;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class TipsyAppleSkinCompat {
    public static final ResourceLocation MINECRAFT_ICONS = new ResourceLocation("textures/gui/icons.png");
    public static final ResourceLocation APPLESKIN_ICONS = new ResourceLocation("appleskin", "textures/icons.png");

    public TipsyAppleSkinCompat() {
        MinecraftForge.EVENT_BUS.addListener(TipsyAppleSkinCompat::preventSaturationInAppleSkin);
        MinecraftForge.EVENT_BUS.addListener(TipsyAppleSkinCompat::renderAppleSkinOverlay);
    }

    public static void renderAppleSkinOverlay(HUDOverlayEvent.Saturation event) {
        if (Minecraft.getInstance().player.getEffect(BnCEffects.INTOXICATED.get()) == null || Minecraft.getInstance().player.hasEffect(ModEffects.NOURISHMENT.get()))
            return;

        if (event.saturationLevel < 0)
            return;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float modifiedSaturation = Math.max(0, Math.min(event.saturationLevel, 20));

        int startSaturationBar = 0;
        int endSaturationBar = (int) Math.ceil(modifiedSaturation / 2.0F);

        int iconSize = 9;

        int left = event.x;

        int ticks = Minecraft.getInstance().gui.getGuiTicks();
        for (int i = startSaturationBar; i < endSaturationBar; ++i)
        {
            int x = ( left - i * 8 - 9 ) + (int) ( Mth.cos(( ticks + i * 2 ) * 0.20F) * 2f );
            int y = ( event.y ) + (int) ( Mth.sin(( ticks + i * 2 ) * 0.25F) * 2f );

            int v = 0;
            int u = 0;

            float effectiveSaturationOfBar = (modifiedSaturation / 2.0F) - i;

            if (effectiveSaturationOfBar >= 1)
                u = 3 * iconSize;
            else if (effectiveSaturationOfBar > .5)
                u = 2 * iconSize;
            else if (effectiveSaturationOfBar > .25)
                u = iconSize;

            event.guiGraphics.blit(APPLESKIN_ICONS, x, y, u, v, iconSize, iconSize);
        }

        RenderSystem.setShaderTexture(0, MINECRAFT_ICONS);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        event.setCanceled(true);
    }

    public static void preventSaturationInAppleSkin(FoodValuesEvent event) {
        Player entity = event.player;
        if ( entity.hasEffect(BnCEffects.TIPSY.get())) {
            event.modifiedFoodValues = new FoodValues(event.modifiedFoodValues.hunger, 0.0F);
        }
    }
}
