package umpaz.brewinandchewin.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.math.Axis;
import com.sun.jna.platform.win32.COM.COMBindingBaseObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.injection.selectors.ElementNode;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.block.CoasterBlock;
import umpaz.brewinandchewin.common.block.entity.CoasterBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CoasterBlockEntityRenderer implements BlockEntityRenderer<CoasterBlockEntity> {
    private static final Map<ResourceLocation, ResourceLocation> ITEM_TO_MODELS = new HashMap<>();

    public static void addToModelMap(ResourceLocation itemId, ResourceLocation modelId) {
        ITEM_TO_MODELS.put(itemId, modelId);
    }

    public CoasterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }


    // there is definitely a better way to do this, but it felt better to do this than what was there
    private void poseUtil(PoseStack poseStack, int fullCount, int curCount, RandomSource seededRandom, boolean invisible) {
        Vector2f translateVec = switch (fullCount) {
            default:
            case 1:
                yield new Vector2f();
            case 2:
                if (curCount == 0) {
                    yield new Vector2f(-0.20F, -0.15F);
                } else {
                    yield new Vector2f(0.20F, 0.15F);
                }
            case 3:
                if (curCount == 0) {
                    yield new Vector2f(0.05F, 0.25F);
                } else if (curCount == 1) {
                    yield new Vector2f(-0.25F, -0.15F);
                } else {
                    yield new Vector2f(0.25F, -0.25F);
                }
            case 4:
                if (curCount == 0) {
                    yield new Vector2f(0.20F, 0.25F);
                } else if (curCount == 1) {
                    yield new Vector2f(-0.25F, 0.20F);
                } else if (curCount == 2) {
                    yield new Vector2f(0.25F, -0.20F);
                } else {
                    yield new Vector2f(-0.20F, -0.25F);
                }
        };

        float rotation = switch (fullCount) {
            default:
            case 1:
                yield 0;
            case 2:
                if (curCount == 0) {
                    yield 190;
                } else {
                    yield 10;
                }
            case 3:
                if (curCount == 0) {
                    yield -20;
                } else if (curCount == 1) {
                    yield 220;
                } else {
                    yield 100;
                }
            case 4:
                if (curCount == 0) {
                    yield -5;
                } else if (curCount == 1) {
                    yield 265;
                } else if (curCount == 2) {
                    yield 85;
                } else {
                    yield 175;
                }
        };

        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 1, 0, rotation + seededRandom.nextFloat() * 20.0f - 10.0f), .5f + translateVec.x(), 0f, .5f + translateVec.y());

        poseStack.translate(translateVec.x(),  invisible ? 0 : 1.0 / 16f, translateVec.y());
    }

    @Override
    public void render(CoasterBlockEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        int count = (int) entity.getItems().stream().filter(i -> !i.isEmpty()).count();
        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 1, 0, -(360f / 16f) * entity.getBlockState().getValue(CoasterBlock.ROTATION)), 0.5f, 0, 0.5f);

        RandomSource random = new LegacyRandomSource(entity.getBlockPos().asLong());

        if (!entity.getBlockState().getValue(CoasterBlock.INVISIBLE) || count == 0) {
            poseStack.pushPose();
            ResourceLocation modelId = entity.getBlockState().getValue(CoasterBlock.SIZE) > 1 ? new ResourceLocation(BrewinAndChewin.MODID, "block/coaster_tray") : new ResourceLocation(BrewinAndChewin.MODID, "block/coaster");
            Minecraft.getInstance().getBlockRenderer().getModelRenderer().tesselateBlock(entity.getLevel(), Minecraft.getInstance().getModelManager().getModel(modelId), entity.getBlockState(), entity.getBlockPos(), poseStack, buffer.getBuffer(RenderType.cutout()), false, random, entity.getBlockPos().asLong(), combinedOverlay, ModelData.EMPTY, null);
            poseStack.popPose();
        }

        for (int i = 0; i < count; i++) {
            BakedModel model = getCoasterModel(entity.getItems().get(i).getItem());
            poseStack.pushPose();

            poseUtil(poseStack, count, i, random, entity.getBlockState().getValue(CoasterBlock.INVISIBLE));

            if (model == Minecraft.getInstance().getModelManager().getMissingModel()) {
                poseStack.translate(0.51, 0.05, 0.5);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.scale(0.5F, 0.5F ,0.5F);
                Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItems().get(i), ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, entity.getLevel(), (int) entity.getBlockPos().asLong());
            } else
                Minecraft.getInstance().getBlockRenderer().getModelRenderer().tesselateBlock(entity.getLevel(), model, entity.getBlockState(), entity.getBlockPos(), poseStack, buffer.getBuffer(RenderType.cutout()), false, random, entity.getBlockPos().asLong(), combinedOverlay, ModelData.EMPTY, RenderType.cutout());

            poseStack.popPose();
        }
    }

    public static BakedModel getCoasterModel(Item item) {
        ResourceLocation modelLocation = ITEM_TO_MODELS.get(ForgeRegistries.ITEMS.getKey(item));
        return Minecraft.getInstance().getModelManager().getModel(modelLocation);
    }
}