package umpaz.brewinandchewin.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.extensions.IForgeBlockAndTintGetter;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.gui.IntoxicatedHealthOverlay;
import umpaz.brewinandchewin.client.gui.KegScreen;
import umpaz.brewinandchewin.client.gui.KegTooltip;
import umpaz.brewinandchewin.client.model.CoasterWrappedModel;
import umpaz.brewinandchewin.client.renderer.CoasterBlockEntityRenderer;
import umpaz.brewinandchewin.client.renderer.texture.BnCTextureModifiers;
import umpaz.brewinandchewin.client.renderer.texture.modifier.TextureModifier;
import umpaz.brewinandchewin.common.block.CoasterBlock;
import umpaz.brewinandchewin.common.block.entity.CoasterBlockEntity;
import umpaz.brewinandchewin.common.registry.BnCBlocks;
import umpaz.brewinandchewin.common.registry.BnCMenuTypes;
import umpaz.brewinandchewin.client.utility.BnCFluidItemDisplays;

import javax.annotation.Syntax;
import javax.swing.plaf.PanelUI;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(modid = BrewinAndChewin.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BnCClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(BnCMenuTypes.KEG.get(), KegScreen::new));

        IntoxicatedHealthOverlay.init();
        TipsyEffects.init();
        BnCTextureModifiers.init();
    }

    @SubscribeEvent
    public static void registerCustomTooltipRenderers(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(KegTooltip.KegTooltipComponent.class, KegTooltip::new);
    }

    @SubscribeEvent
    public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(BnCFluidItemDisplays.Loader.INSTANCE);
    }

    private static final Set<ResourceLocation> MODELS = new HashSet<>();

    @SubscribeEvent
    public static void registerColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, pTintIndex) -> {
            if (level.getBlockEntity(pos) instanceof CoasterBlockEntity blockEntity) {
                int tintIndex = -1;
                int count = (int) blockEntity.getItems().stream().filter(i -> !i.isEmpty()).count();
                for (int i = 0; i < count; i++) {
                    ItemStack stack = blockEntity.getItems().get(i);
                    ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
                    List<CoasterBlockEntityRenderer.ModelEntry> modelEntries = CoasterBlockEntityRenderer.getModelEntries(itemId);

                    if (modelEntries != null) {
                        for (CoasterBlockEntityRenderer.ModelEntry modelEntry : modelEntries) {
                            int color = 0XFFFFFFFF;
                            for (int j = 0; j < modelEntry.modifiers().size(); ++j) {
                                for (TextureModifier modifier : modelEntry.modifiers()) {
                                    color = modifier.color(level, state, pos, stack, color);
                                }
                            }
                            if (color != -1) {
                                ++tintIndex;
                                if (tintIndex == pTintIndex)
                                    return color;
                            }
                        }
                    }
                }
            }
            return -1;
        }, BnCBlocks.COASTER.get());
    }

    @SubscribeEvent
    public static void bakeModels(ModelEvent.RegisterAdditional event) {
        CoasterBlockEntityRenderer.resetCache();
        MODELS.addAll(getModels(Minecraft.getInstance().getResourceManager(), Runnable::run));
        event.register(new ResourceLocation(BrewinAndChewin.MODID, "block/coaster"));
        event.register(new ResourceLocation(BrewinAndChewin.MODID, "block/coaster_tray"));
        for (ResourceLocation entry : MODELS) {
            event.register(entry);
        }
    }

    @SubscribeEvent
    public static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
        for (ResourceLocation entry : MODELS) {
            event.getModels().put(entry, new CoasterWrappedModel(event.getModels().get(entry)));
        }
        MODELS.clear();
    }

    public static List<ResourceLocation> getModels(ResourceManager manager, Executor executor) {
        ArrayList<ResourceLocation> models = new ArrayList<>();

        for (Map.Entry<ResourceLocation, Resource> resourceEntry : manager.listResources("brewinandchewin/coaster", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            models.addAll(CompletableFuture.supplyAsync(() -> {
                try {
                    Reader reader = resourceEntry.getValue().openAsReader();
                    JsonElement json = JsonParser.parseReader(reader);
                    reader.close();
                    if (json instanceof JsonObject jsonObject) {
                        ResourceLocation itemId = ResourceLocation.CODEC.decode(JsonOps.INSTANCE, jsonObject.get("item")).getOrThrow(false, BrewinAndChewin.LOG::error).getFirst();
                        List<CoasterBlockEntityRenderer.ModelEntry> modelEntries = CoasterBlockEntityRenderer.ModelEntry.LIST_CODEC.decode(JsonOps.INSTANCE, jsonObject.get("models")).getOrThrow(false, BrewinAndChewin.LOG::error).getFirst();
                        CoasterBlockEntityRenderer.addToModelMap(itemId, modelEntries);
                        return modelEntries.stream().map(CoasterBlockEntityRenderer.ModelEntry::model).toList();
                    }
                } catch (Exception ex) {
                    BrewinAndChewin.LOG.error("Unexpected error in Brewin' And Chewin' coaster model JSON \"{}\". {}", resourceEntry.getKey(), ex);
                    return List.<ResourceLocation>of();
                }
                BrewinAndChewin.LOG.error("Unexpected error in Brewin' And Chewin' coaster model JSON: {}.", resourceEntry.getKey());
                return List.<ResourceLocation>of();
            }, executor).join());
        }
        return models.stream().filter(Objects::nonNull).toList();
    }
}
