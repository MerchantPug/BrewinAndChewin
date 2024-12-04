package umpaz.brewinandchewin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import umpaz.brewinandchewin.client.BnCClientSetup;
import umpaz.brewinandchewin.common.BnCCommonSetup;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.network.BnCNetworkHandler;
import umpaz.brewinandchewin.common.registry.*;
import umpaz.brewinandchewin.data.loot.BnCCopyMealFunction;
import vectorwing.farmersdelight.common.CommonSetup;
import vectorwing.farmersdelight.common.registry.ModBiomeModifiers;
import vectorwing.farmersdelight.common.registry.ModLootFunctions;

@Mod(BrewinAndChewin.MODID)
public class BrewinAndChewin
{
   public static final String MODID = "brewinandchewin";
   public static final Logger LOG = LoggerFactory.getLogger("Brewin' And Chewin'");
    public BrewinAndChewin() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(BnCCommonSetup::init);
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(BnCClientSetup::init);
        }

       ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BnCConfiguration.COMMON_CONFIG);
       ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BnCConfiguration.CLIENT_CONFIG);

        BnCBlocks.BLOCKS.register(modEventBus);
        BnCFluids.FLUIDS.register(modEventBus);
        BnCFluids.FLUID_TYPES.register(modEventBus);
        BnCEffects.EFFECTS.register(modEventBus);
        BnCParticleTypes.PARTICLE_TYPES.register(modEventBus);
        BnCItems.ITEMS.register(modEventBus);
        BnCBlockEntityTypes.TILES.register(modEventBus);
        BnCMenuTypes.MENU_TYPES.register(modEventBus);
        BnCRecipeTypes.RECIPE_TYPES.register(modEventBus);
        BnCRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        BnCCreativeTabs.CREATIVE_TABS.register(modEventBus);
        BnCLootFunctions.LOOT_FUNCTIONS.register(modEventBus);
        BnCLootModifiers.LOOT_MODIFIERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        ForgeMod.enableMilkFluid();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

}
