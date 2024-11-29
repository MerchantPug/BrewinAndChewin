package umpaz.brewinandchewin.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.gui.IntoxicatedHealthOverlay;
import umpaz.brewinandchewin.client.gui.KegScreen;
import umpaz.brewinandchewin.client.gui.KegTooltip;
import umpaz.brewinandchewin.client.renderer.CoasterBlockEntityRenderer;
import umpaz.brewinandchewin.common.registry.BnCMenuTypes;
import umpaz.brewinandchewin.client.utility.BnCFluidItemDisplays;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(modid = BrewinAndChewin.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BnCClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(BnCMenuTypes.KEG.get(), KegScreen::new));

        IntoxicatedHealthOverlay.init();
        TipsyEffects.init();
    }

    @SubscribeEvent
    public static void registerCustomTooltipRenderers(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(KegTooltip.KegTooltipComponent.class, KegTooltip::new);
    }

    @SubscribeEvent
    public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(BnCFluidItemDisplays.Loader.INSTANCE);
    }

    @SubscribeEvent
    public static void bakeModels(ModelEvent.RegisterAdditional event) {
        List<ResourceLocation> models = getModels(Minecraft.getInstance().getResourceManager(), Runnable::run);
        event.register(new ResourceLocation(BrewinAndChewin.MODID, "block/coaster"));
        event.register(new ResourceLocation(BrewinAndChewin.MODID, "block/coaster_tray"));
        for (ResourceLocation entry : models) {
            event.register(entry);
        }
    }

    public static List<ResourceLocation> getModels(ResourceManager manager, Executor executor) {
        ArrayList<ResourceLocation> models = new ArrayList<>();

        for (Map.Entry<ResourceLocation, Resource> resourceEntry : manager.listResources("brewinandchewin/coaster", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            models.add(CompletableFuture.supplyAsync(() -> {
                try {
                    Reader reader = resourceEntry.getValue().openAsReader();
                    JsonElement json = JsonParser.parseReader(reader);
                    reader.close();
                    if (json instanceof JsonObject jsonObject) {
                        ResourceLocation itemId = ResourceLocation.CODEC.decode(JsonOps.INSTANCE, jsonObject.get("item")).getOrThrow(false, BrewinAndChewin.LOG::error).getFirst();
                        ResourceLocation modelId = ResourceLocation.CODEC.decode(JsonOps.INSTANCE, jsonObject.get("model")).getOrThrow(false, BrewinAndChewin.LOG::error).getFirst();
                        CoasterBlockEntityRenderer.addToModelMap(itemId, modelId);
                        return modelId;
                    }
                } catch (Exception ex) {
                    BrewinAndChewin.LOG.error("Unexpected error in Brewin' And Chewin' coaster model JSON \"{}\". {}", resourceEntry.getKey(), ex);
                    return null;
                }
                BrewinAndChewin.LOG.error("Unexpected error in Brewin' And Chewin' coaster model JSON: {}.", resourceEntry.getKey());
                return null;
            }, executor).join());
        }
        return models.stream().filter(Objects::nonNull).toList();
    }
}
