package umpaz.brewinandchewin.data.loot;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.simpleBuilder;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BnCCopyMealFunction extends LootItemConditionalFunction {
   public static final ResourceLocation ID = new ResourceLocation(BrewinAndChewin.MODID, "copy_meal");

   private BnCCopyMealFunction( LootItemCondition[] conditions ) {
      super(conditions);
   }

   public static LootItemConditionalFunction.Builder<?> builder() {
      return simpleBuilder(BnCCopyMealFunction::new);
   }

   @Override
   protected ItemStack run( ItemStack stack, LootContext context ) {
      BlockEntity tile = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
      if ( tile instanceof KegBlockEntity ) {
         CompoundTag tag = ( (KegBlockEntity) tile ).writeMeal(new CompoundTag());
         if ( !tag.isEmpty() ) {
            stack.addTagElement("BlockEntityTag", tag);
         }
      }
      return stack;
   }

   @Override
   @Nullable
   public LootItemFunctionType getType() {
      return null;
   }

   public static class Serializer extends LootItemConditionalFunction.Serializer<BnCCopyMealFunction> {
      @Override
      public BnCCopyMealFunction deserialize( JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions ) {
         return new BnCCopyMealFunction(conditions);
      }
   }
}