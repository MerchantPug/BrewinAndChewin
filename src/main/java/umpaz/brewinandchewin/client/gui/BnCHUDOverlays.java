package umpaz.brewinandchewin.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.capability.TipsyNumbedHeartsCapability;
import umpaz.brewinandchewin.common.mixin.client.GuiAccessor;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Random;

public class BnCHUDOverlays {
    protected static int foodIconsOffset;
    public static final ResourceLocation MOD_ICONS_TEXTURE = new ResourceLocation(BrewinAndChewin.MODID, "textures/gui/bnc_icons.png");
    private static final ResourceLocation NOURISHMENT_ICONS_TEXTURE = new ResourceLocation("farmersdelight", "textures/gui/fd_icons.png");

    public static void init() {
        for (int i = 0; i < 10; ++i) {
            heartOffset[i] = 0;
        }
        MinecraftForge.EVENT_BUS.register(new BnCHUDOverlays());
    }


    static ResourceLocation PLAYER_HEALTH_ELEMENT = new ResourceLocation("minecraft", "player_health");
    static ResourceLocation FOOD_LEVEL_ELEMENT = new ResourceLocation("minecraft", "food_level");

    @SubscribeEvent
    public void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == GuiOverlayManager.findOverlay(PLAYER_HEALTH_ELEMENT)) {
            Minecraft mc = Minecraft.getInstance();
            ForgeGui gui = (ForgeGui) mc.gui;
            if (!mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
                if (mc.player.getCapability(TipsyNumbedHeartsCapability.INSTANCE).map(cap -> cap.getNumbedHealth() > 0.0F).orElse(false)) {
                    renderNumbedOverlay(gui, event.getGuiGraphics());
                }
            }
        } else if (event.getOverlay() == GuiOverlayManager.findOverlay(FOOD_LEVEL_ELEMENT)) {
            Minecraft mc = Minecraft.getInstance();
            ForgeGui gui = (ForgeGui) mc.gui;
            if (!mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
                if (mc.player.hasEffect(BnCEffects.INTOXICATION.get())) {
                    renderIntoxicationOverlay(gui, event.getGuiGraphics());
                }
            }
        }
    }

    public static void renderNumbedOverlay(ForgeGui gui, GuiGraphics guiGraphics) {
        // This one is less-so visual and moreso gameplay related, so no config for this.
        int healthIconsOffset = gui.leftHeight;
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null) {
            return;
        }
        int top = minecraft.getWindow().getGuiScaledHeight() - healthIconsOffset + 10;
        int right = minecraft.getWindow().getGuiScaledWidth() / 2 - 91;

        drawNumbedOverlay(player, minecraft, guiGraphics, right, top);
    }

    private static float numbedAlpha = 1.0F;
    private static boolean increaseNumbedAlpha = false;
    public static int[] heartOffset = new int[10];

    public static void drawNumbedOverlay(Player player, Minecraft minecraft, GuiGraphics graphics, int right, int top) {
        int ticks = minecraft.gui.getGuiTicks();
        Random rand = new Random();
        rand.setSeed(ticks * 312871L);
        TipsyNumbedHeartsCapability cap = player.getCapability(TipsyNumbedHeartsCapability.INSTANCE).orElseThrow(NullPointerException::new);

        boolean shouldDrawHighlighted = Gui.HeartType.forPlayer(player).canBlink && ((GuiAccessor)Minecraft.getInstance().gui).brewinandchewin$getHealthBlinkTime() > (long)ticks && (((GuiAccessor)Minecraft.getInstance().gui).brewinandchewin$getHealthBlinkTime() - (long)ticks) / 3L % 2L == 1L;

        RenderSystem.enableBlend();

        int healthStart = Mth.ceil(player.getHealth() / 2);
        int healthEnd = Math.max(healthStart - Mth.ceil((cap.getNumbedHealth() + 1) / 2), 0);
        boolean healthOverflow = healthStart - Mth.ceil((cap.getNumbedHealth() + 1) / 2) < 0;
        if (!healthOverflow && (int)player.getHealth() % 2 == 1)
            ++healthEnd;
        boolean splitHeartRight = false;

        if (BnCConfiguration.NUMBED_HEART_FLICKERING.get() && !player.hasEffect(BnCEffects.TIPSY.get())) {
            float increase = 0.05F * ((float) cap.getTicksUntilDamage() / TipsyNumbedHeartsCapability.MAX_TICKS_UNTIL_DAMAGE);
            numbedAlpha = Mth.clamp(numbedAlpha + (increaseNumbedAlpha ? increase : -increase), -0.01F, 1.01F);
            if (numbedAlpha < 0.0F)
                increaseNumbedAlpha = true;
            if (numbedAlpha > 1.0F)
                increaseNumbedAlpha = false;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, numbedAlpha);
        } else
            numbedAlpha = 1.0F;

        for (int i = healthStart; i > healthEnd; --i) {
            int x = (right + i * 8 - 8);
            int y = top;
            if (player.getHealth() <= 4) {
                y += heartOffset[i - 1];
            }

            boolean splitHeartLeft = !healthOverflow && i == healthStart && i * 2 > Mth.ceil(player.getHealth());

            if (splitHeartLeft || healthStart - Mth.floor(cap.getNumbedHealth() / 2) > i - 1 && i * 2 > Mth.ceil(player.getHealth())) {
                graphics.blit(MOD_ICONS_TEXTURE, x, y, (shouldDrawHighlighted ? 9 : 0), 9, 9, 9);
                if (splitHeartLeft)
                    splitHeartRight = true;
            } else if (splitHeartRight && (i == healthEnd + 1) || healthStart - Mth.floor(cap.getNumbedHealth() / 2) > i - 1 && i * 2 < Mth.ceil(player.getHealth())) {
                graphics.blit(MOD_ICONS_TEXTURE, x, y, 36 + (shouldDrawHighlighted ? 9 : 0), 9, 9, 9);
            } else {
                graphics.blit(MOD_ICONS_TEXTURE, x, y, 18 + (shouldDrawHighlighted ? 9 : 0), 9, 9, 9);
            }
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    @SubscribeEvent
    public static void onLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
        // Done to make re-logs consistent with quitting the game.
        numbedAlpha = 1.0F;
        increaseNumbedAlpha = false;
    }

    public static void renderIntoxicationOverlay(ForgeGui gui, GuiGraphics guiGraphics) {
        if (!BnCConfiguration.INTOXICATION_FOOD_OVERLAY.get()) {
            return;
        }

        foodIconsOffset = gui.rightHeight;
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null) {
            return;
        }
        int top = minecraft.getWindow().getGuiScaledHeight() - foodIconsOffset;
        int right = minecraft.getWindow().getGuiScaledWidth() / 2 + 91;

        drawIntoxicationOverlay(player, minecraft, guiGraphics, right, top);
    }

    public static void drawIntoxicationOverlay(Player player, Minecraft minecraft, GuiGraphics graphics, int right, int top) {
        int ticks = minecraft.gui.getGuiTicks();
        Random rand = new Random();
        rand.setSeed(ticks * 312871L);

        RenderSystem.enableBlend();

        ResourceLocation texture = player.hasEffect(ModEffects.NOURISHMENT.get()) ? NOURISHMENT_ICONS_TEXTURE : MOD_ICONS_TEXTURE;

        for (int i = 0; i < 10; ++i) {
            int x = (right - i * 8 - 9) + (int) (Mth.cos((ticks + i * 2) * 0.20F) * 2f);
            int y = (top) + (int) (Mth.sin((ticks + i * 2) * 0.25F) * 2f);

            float effectiveHungerOfBar = (float) player.getFoodData().getFoodLevel() / 2.0F - (float) i;
            boolean isPlayerHealingWithSaturationAndNourishment =
                    player.hasEffect(ModEffects.NOURISHMENT.get()) &&
                            player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
                            && player.isHurt()
                            && player.getFoodData().getFoodLevel() >= 18;
            int naturalHealingOffset = isPlayerHealingWithSaturationAndNourishment ? 18 : 0;

            graphics.blit(texture, x, y, 0, 0, 9, 9);

            if (effectiveHungerOfBar >= 1.0F) {
                graphics.blit(texture, x, y, 18 + naturalHealingOffset, 0, 9, 9);
            } else if ((double) effectiveHungerOfBar >= (double) 0.5F) {
                graphics.blit(texture, x, y, 9 + naturalHealingOffset, 0, 9, 9);
            }
        }

        RenderSystem.disableBlend();
    }
}