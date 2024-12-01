package umpaz.brewinandchewin.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.model.CoasterWrappedModel;
import umpaz.brewinandchewin.client.renderer.texture.BnCTextureModifiers;
import umpaz.brewinandchewin.client.renderer.texture.modifier.TextureModifier;
import umpaz.brewinandchewin.common.block.CoasterBlock;
import umpaz.brewinandchewin.common.block.entity.CoasterBlockEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class CoasterBlockEntityRenderer implements BlockEntityRenderer<CoasterBlockEntity> {
    private static final Map<ResourceLocation, List<ModelEntry>> ITEM_TO_MODELS = new HashMap<>();
    private static final Set<ResourceLocation> ERRONEOUS_ENTRIES  = new HashSet<>();

    public static void resetCache() {
        ITEM_TO_MODELS.clear();
        ERRONEOUS_ENTRIES.clear();
    }

    public static List<ModelEntry> getModelEntries(ResourceLocation itemId) {
        return ITEM_TO_MODELS.get(itemId);
    }

    public static void addToModelMap(ResourceLocation itemId, List<ModelEntry> models) {
        ITEM_TO_MODELS.put(itemId, models);
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

        int tintIndex = -1;
        for (int i = 0; i < count; i++) {
            ItemStack stack = entity.getItems().get(i);
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
            List<ModelEntry> modelEntries = getModelEntries(itemId);
            poseStack.pushPose();

            poseUtil(poseStack, count, i, random, entity.getBlockState().getValue(CoasterBlock.INVISIBLE));

            if (modelEntries != null) {
                for (ModelEntry modelEntry : modelEntries) {
                    BakedModel coasterModel = getCoasterModel(itemId, modelEntry);
                    int color = 0XFFFFFFFF;
                    RenderType renderType = RenderType.cutout();
                    for (int j = 0; j < modelEntry.modifiers().size(); ++j) {
                        for (TextureModifier modifier : modelEntry.modifiers()) {
                            color = modifier.color(entity.getLevel(), entity.getBlockState(), entity.getBlockPos(), stack, color);
                            renderType = modifier.renderType(entity.getLevel(), entity.getBlockState(), entity.getBlockPos(), stack, renderType);
                        }
                    }
                    ModelData data = ModelData.EMPTY;
                    if (color != -1) {
                        ++tintIndex;
                        data = ModelData.builder()
                                .with(CoasterWrappedModel.TINT_INDEX, tintIndex)
                                .build();
                    }
                    VertexConsumer consumer = buffer.getBuffer(renderType);
                    Minecraft.getInstance().getBlockRenderer().getModelRenderer().tesselateBlock(entity.getLevel(), coasterModel, entity.getBlockState(), entity.getBlockPos(), poseStack, consumer, false, random, entity.getBlockPos().asLong(), combinedOverlay, data, renderType);
                }
            } else {
                poseStack.translate(0.51, 0.05, 0.5);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.scale(0.5F, 0.5F, 0.5F);
                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, entity.getLevel(), (int) entity.getBlockPos().asLong());
            }
            poseStack.popPose();
        }
    }

    public static BakedModel getCoasterModel(ResourceLocation itemId, ModelEntry modelEntry) {
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelEntry.model());
        if (model == Minecraft.getInstance().getModelManager().getMissingModel() && !ERRONEOUS_ENTRIES.contains(modelEntry.model())) {
            BrewinAndChewin.LOG.error("Failed to get model '{}'", modelEntry.model());
            ERRONEOUS_ENTRIES.add(modelEntry.model());
        }
        return model;
    }

    public record ModelEntry(ResourceLocation model, List<? extends TextureModifier> modifiers) {
        private static final Codec<ModelEntry> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
                ResourceLocation.CODEC.fieldOf("model").forGetter(ModelEntry::model),
                BnCTextureModifiers.CODEC.listOf().optionalFieldOf("texture_modifiers", List.of()).forGetter(modelEntry -> (List)modelEntry.modifiers())
        ).apply(inst, ModelEntry::new));
        public static final Codec<List<ModelEntry>> LIST_CODEC = Codec.either(ResourceLocation.CODEC, DIRECT_CODEC.listOf())
                .xmap(either -> either.map(resourceLocation -> List.of(new ModelEntry(resourceLocation, List.of())), Function.identity()), modelEntry -> {
                    if (modelEntry.size() == 1 && modelEntry.get(0).modifiers().isEmpty())
                        return Either.left(modelEntry.get(0).model());
                    return Either.right(modelEntry);
                });
    }
}