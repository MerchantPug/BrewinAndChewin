package umpaz.brewinandchewin.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.*;
import org.joml.Vector3f;
import umpaz.brewinandchewin.common.block.entity.ItemCoasterBlockEntity;

public class ItemCoasterRenderer implements BlockEntityRenderer<ItemCoasterBlockEntity> {

    public ItemCoasterRenderer(BlockEntityRendererProvider.Context pContext) {

    }

    @Override
    public void render(ItemCoasterBlockEntity coasterBlockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack boardStack = coasterBlockEntity.getStoredItem();
        int posLong = (int) coasterBlockEntity.getBlockPos().asLong();
        if (!boardStack.isEmpty()) {
            poseStack.pushPose();

            poseStack.translate(0.5D, 0.3D + (Mth.sin(coasterBlockEntity.getLevel().getGameTime() / 50.0F) / 40.0F), 0.5D);
            float f3 = 3.2F * (coasterBlockEntity.getLevel().getGameTime() + 1.0F) / 5.0F;
            poseStack.mulPose(Axis.YP.rotationDegrees(f3));
            poseStack.scale(0.5F, 0.5F, 0.5F);
            Minecraft.getInstance().getItemRenderer().renderStatic(boardStack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, coasterBlockEntity.getLevel(), posLong);
            poseStack.popPose();
        }
    }
}