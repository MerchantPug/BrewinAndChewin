package umpaz.brewinandchewin.common.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.crafting.CreatePotionPouringRecipe;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;

import java.util.function.Supplier;

public class BnCRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BrewinAndChewin.MODID);

    public static final RegistryObject<RecipeSerializer<?>> FERMENTING = RECIPE_SERIALIZERS.register("fermenting", KegFermentingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> KEG_POURING = RECIPE_SERIALIZERS.register("keg_pouring", KegPouringRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> CREATE_POTION_POURING = createCreatePotionPouringRecipe();

    private static RegistryObject<RecipeSerializer<?>> createCreatePotionPouringRecipe() {
        if (ModList.get().isLoaded("create"))
            return RECIPE_SERIALIZERS.register("create_potion_pouring", CreatePotionPouringRecipe.Serializer::new);
        return null;
    }

}
