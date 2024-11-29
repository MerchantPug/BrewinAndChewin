package umpaz.brewinandchewin.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.common.registry.BnCRecipeSerializers;
import umpaz.brewinandchewin.common.registry.BnCRecipeTypes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KegFermentingRecipe implements Recipe<RecipeWrapper> {
   public static final int INPUT_SLOTS = 4;

   private final ResourceLocation id;
   private final NonNullList<Ingredient> inputItems;
   @Nullable
   private FluidStack fluidIngredient = null;
   @Nullable
   private Fluid resultFluid = null;
   @Nullable
   private Item resultItem = null;

   private final float experience;
   private final int fermentTime;
   private final int temperature;

   private final int amount;

   public KegFermentingRecipe(ResourceLocation id, NonNullList<Ingredient> inputItems, @Nullable FluidStack fluidIngredient, @Nullable Fluid resultFluid, @Nullable Item resultItem, int amount, float experience, int fermentTime, int temperature ) {
      this.id = id;
      this.inputItems = inputItems;
      this.fluidIngredient = fluidIngredient;
      this.resultFluid = resultFluid;
      this.resultItem = resultItem;
      this.amount = amount;
      this.experience = experience;
      this.fermentTime = fermentTime;
      this.temperature = temperature;
   }


   @Override
   public ResourceLocation getId() {
      return this.id;
   }

   @Override
   public NonNullList<Ingredient> getIngredients() {
      return this.inputItems;
   }

   @Nullable
   public FluidStack getFluidIngredient() {
      return this.fluidIngredient;
   }

   @Nullable
   public Fluid getResultFluid() {
      return this.resultFluid;
   }

   @Nullable
   public Item getResultItem() {return this.resultItem;}

   public int getAmount() {
      return this.amount;
   }

   @Override
   public ItemStack assemble( RecipeWrapper inv, RegistryAccess access ) {
      return null;
   }

   public float getExperience() {
      return this.experience;
   }

   public int getFermentTime() {
      return this.fermentTime;
   }

   public int getTemperature() {
      return this.temperature;
   }

   @Override
   public boolean matches( RecipeWrapper inv, Level level ) {
      List<ItemStack> inputs = new ArrayList<>();
      int i = 0;

      for ( int j = 0; j < INPUT_SLOTS; ++j ) {
         ItemStack itemstack = inv.getItem(j);
         if ( !itemstack.isEmpty() ) {
            ++i;
            inputs.add(itemstack);
         }
      }
      return i == this.inputItems.size() && RecipeMatcher.findMatches(inputs, this.inputItems) != null;
   }

   @Override
   public boolean canCraftInDimensions( int width, int height ) {
      return width * height >= this.inputItems.size();
   }

   @Override
   public ItemStack getResultItem( RegistryAccess registryAccess ) {
      return ItemStack.EMPTY;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return BnCRecipeSerializers.FERMENTING.get();
   }

   @Override
   public RecipeType<?> getType() {
      return BnCRecipeTypes.FERMENTING.get();
   }

   @Override
   public ItemStack getToastSymbol() {
      return new ItemStack(BnCItems.KEG.get());
   }


   @Override
   public boolean equals( Object o ) {
      if ( this == o ) return true;
      if ( o == null || getClass() != o.getClass() ) return false;

      KegFermentingRecipe that = (KegFermentingRecipe) o;

      if ( Float.compare(that.getExperience(), getExperience()) != 0 ) return false;
      if ( getFermentTime() != that.getFermentTime() ) return false;
      if ( getTemperature() != that.getTemperature() ) return false;
      if ( !getId().equals(that.getId()) ) return false;
      if ( getResultFluid() != ( that.getResultFluid() ) ) return false;
      if ( getResultItem() != ( that.getResultItem() ) ) return false;
      if ( getFluidIngredient() != ( that.getFluidIngredient() ) ) return false;
      if ( getAmount() != ( that.getAmount() ) ) return false;

      return inputItems.equals(that.inputItems);
   }

   @Override
   public int hashCode() {
      int result = getId().hashCode();
      result = 31 * result + inputItems.hashCode();
      result = 31 * result + ( ( fluidIngredient != null ) ? fluidIngredient.hashCode() : 0 );
      result = 31 * result + ( ( resultItem != null ) ? resultItem.hashCode() : 0 );
      result = 31 * result + ( ( resultFluid != null ) ? resultFluid.hashCode() : 0 );
      result = 31 * result + ( getExperience() != +0.0f ? Float.floatToIntBits(getExperience()) : 0 );
      result = 31 * result + getFermentTime();
      result = 31 * result + getTemperature();
      return result;
   }

   public static class Serializer implements RecipeSerializer<KegFermentingRecipe> {
      public Serializer() {
      }

      @Override
      public KegFermentingRecipe fromJson( ResourceLocation recipeId, JsonObject json ) {
         final NonNullList<Ingredient> inputItemsIn = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
         if ( inputItemsIn.isEmpty() ) {
            throw new JsonParseException("No ingredients for cooking recipe");
         }
         else if ( inputItemsIn.size() > KegFermentingRecipe.INPUT_SLOTS ) {
            throw new JsonParseException("Too many ingredients for cooking recipe! The max is " + KegFermentingRecipe.INPUT_SLOTS);
         }
         else {

            FluidStack baseFluidStackIn = null;
            if ( json.has("basefluid") ) {
               JsonObject baseFluid = json.getAsJsonObject("basefluid");

               baseFluidStackIn = new FluidStack(
                       ForgeRegistries.FLUIDS.getValue(new ResourceLocation(GsonHelper.getAsString(baseFluid, "fluid"))),
                       GsonHelper.getAsInt(baseFluid, "count", 200)
               );
            }


            float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
            int fermentingTime = GsonHelper.getAsInt(json, "fermentingtime", 200);
            int temperature = GsonHelper.getAsInt(json, "temperature", 3);

            JsonObject result = GsonHelper.getAsJsonObject(json, "result");
            int count = GsonHelper.getAsInt(result, "count");

            Fluid resultFluid = null;
            if ( result.has("fluid") ) {
               resultFluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(GsonHelper.getAsString(result, "fluid")));
            }

            Item resultItem = null;
            if ( result.has("item") ) {
               resultItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(result, "item")));
            }

            return new KegFermentingRecipe(recipeId, inputItemsIn, baseFluidStackIn, resultFluid, resultItem, count, experience, fermentingTime, temperature);
         }
      }

      private static NonNullList<Ingredient> readIngredients( JsonArray ingredientArray ) {
         NonNullList<Ingredient> nonnulllist = NonNullList.create();

         for ( int i = 0; i < ingredientArray.size(); ++i ) {
            Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
            if ( !ingredient.isEmpty() ) {
               nonnulllist.add(ingredient);
            }
         }

         return nonnulllist;
      }

      @Nullable
      @Override
      public KegFermentingRecipe fromNetwork( ResourceLocation recipeId, FriendlyByteBuf buffer ) {
         int i = buffer.readVarInt();
         NonNullList<Ingredient> inputItemsIn = NonNullList.withSize(i, Ingredient.EMPTY);

         for ( int j = 0; j < inputItemsIn.size(); ++j ) {
            inputItemsIn.set(j, Ingredient.fromNetwork(buffer));
         }


         Optional<FluidStack> baseFluidStackIn = buffer.readOptional(FriendlyByteBuf::readFluidStack);
         Optional<Item> itemResult = buffer.readOptional(FriendlyByteBuf::readItem).map(ItemStack::getItem);
         Optional<Fluid> fluidResult = buffer.readOptional(FriendlyByteBuf::readFluidStack).map(FluidStack::getFluid);
         int amount = buffer.readInt();
         float experienceIn = buffer.readFloat();
         int fermentTimeIn = buffer.readVarInt();
         int temperatureIn = buffer.readVarInt();
         return new KegFermentingRecipe(recipeId, inputItemsIn, baseFluidStackIn.orElse(null), fluidResult.orElse(null), itemResult.orElse(null), amount, experienceIn, fermentTimeIn, temperatureIn);
      }

      @Override
      public void toNetwork( FriendlyByteBuf buffer, KegFermentingRecipe recipe ) {
         buffer.writeVarInt(recipe.inputItems.size());

         for ( Ingredient ingredient : recipe.inputItems ) {
            ingredient.toNetwork(buffer);
         }

         buffer.writeOptional(( recipe.fluidIngredient != null ) ? Optional.of(recipe.fluidIngredient) : Optional.empty(), FriendlyByteBuf::writeFluidStack);
         buffer.writeOptional(( recipe.resultItem != null ) ? Optional.of(recipe.resultItem.getDefaultInstance()) : Optional.empty(), FriendlyByteBuf::writeItem);
         buffer.writeOptional(( recipe.resultFluid != null ) ? Optional.of(new FluidStack(recipe.resultFluid, 1)) : Optional.empty(), FriendlyByteBuf::writeFluidStack);
         buffer.writeInt(recipe.amount);
         buffer.writeFloat(recipe.experience);
         buffer.writeVarInt(recipe.fermentTime);
         buffer.writeVarInt(recipe.temperature);
      }
   }
}