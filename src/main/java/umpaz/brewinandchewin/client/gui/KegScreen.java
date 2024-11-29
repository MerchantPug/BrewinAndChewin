package umpaz.brewinandchewin.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.block.entity.container.KegMenu;
import umpaz.brewinandchewin.client.utility.BnCFluidItemDisplays;
import umpaz.brewinandchewin.common.utility.BnCTextUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class KegScreen extends AbstractContainerScreen<KegMenu>
{
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(BrewinAndChewin.MODID, "textures/gui/keg.png");
    private static final Rectangle PROGRESS_ARROW = new Rectangle(74, 25, 0, 18);
    private static final Rectangle FRIGID_BAR = new Rectangle(32, 55, 8, 4);
    private static final Rectangle COLD_BAR = new Rectangle(40, 55, 9, 4);
    private static final Rectangle WARM_BAR = new Rectangle(57, 55, 9, 4);
    private static final Rectangle HOT_BAR = new Rectangle(66, 55, 8, 4);
    private static final Rectangle LEFT_BUBBLE = new Rectangle(97, 43, 9, 24);
    private static final Rectangle RIGHT_BUBBLE = new Rectangle(134, 43, 9, 24);

    public KegScreen(KegMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = 28;
    }

    @Override
    public void render(GuiGraphics gui, final int mouseX, final int mouseY, float partialTicks) {
        this.renderBackground(gui);
        super.render(gui, mouseX, mouseY, partialTicks);
        this.renderTankTooltip(gui, mouseX, mouseY);
        this.renderTemperatureTooltip(gui, mouseX, mouseY);
        this.renderTooltip(gui, mouseX, mouseY);
    }

    private void renderTankTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        if ( isHovering(108, 19, 24, 28, mouseX, mouseY) && !this.menu.kegTank.isEmpty() ) {
            Component component = MutableComponent.create(this.menu.kegTank.getFluid().getDisplayName().getContents())
                    .append(" (%s/%s mB)".formatted(this.menu.kegTank.getFluidAmount(), this.menu.kegTank.getCapacity()));
            gui.renderTooltip(this.font, component, mouseX, mouseY);
        }
    }

    private void renderTemperatureTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        if ( this.isHovering(34, 54, 43, 5, mouseX, mouseY) ) {
            List<Component> tooltip = new ArrayList<>();
            MutableComponent key = null;
            switch (this.menu.getKegTemperature()) {
                case 1 -> key = BnCTextUtils.getTranslation("container.keg.frigid");
                case 2 -> key = BnCTextUtils.getTranslation("container.keg.cold");
                case 3 -> key = BnCTextUtils.getTranslation("container.keg.normal");
                case 4 -> key = BnCTextUtils.getTranslation("container.keg.warm");
                case 5 -> key = BnCTextUtils.getTranslation("container.keg.hot");
            }
            tooltip.add(key);
            gui.renderComponentTooltip(font, tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
        super.renderLabels(gui, mouseX, mouseY);
        gui.drawString(this.font, this.playerInventoryTitle, 8, (this.imageHeight - 96 + 2), 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
        // Render UI background
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.minecraft == null)
            return;

        gui.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Render progress arrow
        int l = this.menu.getFermentProgressionScaled();
        gui.blit(BACKGROUND_TEXTURE, this.leftPos + PROGRESS_ARROW.x, this.topPos + PROGRESS_ARROW.y, 176, 4, l + 1, PROGRESS_ARROW.height);


        int bubScale = (int) ( ( ( this.menu.getProgression() / 80 ) ) * LEFT_BUBBLE.height ) % ( LEFT_BUBBLE.height + 1 );
        // render bubbles
        gui.blit(BACKGROUND_TEXTURE, this.leftPos + LEFT_BUBBLE.x, this.topPos + LEFT_BUBBLE.y - bubScale, 176, 79 - bubScale, LEFT_BUBBLE.width, (int) ( bubScale + 1 ));
        gui.blit(BACKGROUND_TEXTURE, this.leftPos + RIGHT_BUBBLE.x, this.topPos + RIGHT_BUBBLE.y - bubScale, 186, 79 - bubScale, RIGHT_BUBBLE.width, (int) ( bubScale + 1 ));


        // Render temperature bars
        int temp = this.menu.getKegTemperature();
        if (temp == 1) {
            gui.blit(BACKGROUND_TEXTURE, this.leftPos + FRIGID_BAR.x, this.topPos + FRIGID_BAR.y, 176, 0, FRIGID_BAR.width, FRIGID_BAR.height);
        }
        if (temp < 3) {
            gui.blit(BACKGROUND_TEXTURE, this.leftPos + COLD_BAR.x, this.topPos + COLD_BAR.y, 184, 0, COLD_BAR.width, COLD_BAR.height);
        }
        if (temp > 3) {
            gui.blit(BACKGROUND_TEXTURE, this.leftPos + WARM_BAR.x, this.topPos + WARM_BAR.y, 201, 0, WARM_BAR.width, WARM_BAR.height);
        }
        if (temp == 5) {
            gui.blit(BACKGROUND_TEXTURE, this.leftPos + HOT_BAR.x, this.topPos + HOT_BAR.y, 210, 0, HOT_BAR.width, HOT_BAR.height);
        }

        FluidStack fluidStack = this.menu.kegTank.getFluid();
        if(fluidStack.isEmpty())
            return;


        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluidStack);
        if(stillTexture == null)
            return;


        // Fluid
        TextureAtlasSprite sprite =
                this.minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int tintColor = fluidTypeExtensions.getTintColor(fluidStack);

        float alpha = ((tintColor >> 24) & 0xFF) / 255f;
        float red = ((tintColor >> 16) & 0xFF) / 255f;
        float green = ((tintColor >> 8) & 0xFF) / 255f;
        float blue = (tintColor & 0xFF) / 255f;

        float capacity = this.menu.kegTank.getFluidAmount() / (float) this.menu.kegTank.getCapacity();
        if (capacity > 0.57) {
            int y1 = this.topPos + 19 + (int) (12 * (1 - ((capacity - 0.57F) / .43F)));
            int y2 = this.topPos + 19 + 12;
            float topCapacity = (capacity - 0.57F) / 0.43F;
            float vDistance = sprite.getV1() - sprite.getV0();
            float v0 = sprite.getV0() + (0.25F * vDistance) + (0.75F * vDistance * (1 - topCapacity));
            gui.innerBlit(sprite.atlasLocation(), this.leftPos + 108, this.leftPos + 108 + 16, y1, y2, 0, sprite.getU0(), sprite.getU1(), v0, sprite.getV1(), red, green, blue, alpha);
            gui.innerBlit(sprite.atlasLocation(), this.leftPos + 124, this.leftPos + 124 + 8, y1, y2, 0, sprite.getU0(), sprite.getU0() + 0.5F * ( sprite.getU1() - sprite.getU0() ), v0, sprite.getV1(), red, green, blue, alpha);

        }
        int y1 = this.topPos + 31 + (int) (16 * (1 - Math.min(1, (capacity / .57F))));
        int y2 = this.topPos + 31 + 16;
        float vDistance = sprite.getV1() - sprite.getV0();
        float v0 = sprite.getV0() + (vDistance * (1 - Math.min(1, (capacity / .57F))));
        gui.innerBlit(sprite.atlasLocation(), this.leftPos + 108, this.leftPos + 108 + 16, y1, y2, 0, sprite.getU0(), sprite.getU1(), v0, sprite.getV1(), red, green, blue, alpha);
        gui.innerBlit(sprite.atlasLocation(), this.leftPos + 124, this.leftPos + 124 + 8, y1, y2, 0, sprite.getU0(), sprite.getU0() + 0.5F * ( sprite.getU1() - sprite.getU0() ), v0, sprite.getV1(), red, green, blue, alpha);

        if (!menu.kegTank.getFluid().isEmpty()) {
            ItemStack itemDisplay = BnCFluidItemDisplays.getFluidItemDisplay(Minecraft.getInstance().level.registryAccess(), menu.kegTank.getFluid());
            if (!itemDisplay.isEmpty())
                gui.renderItem(itemDisplay, this.leftPos + 112, this.topPos + 21);
        }
        gui.blit(BACKGROUND_TEXTURE, this.leftPos + 107, this.topPos + 15, 176, 22, 27, 33);

    }
}
