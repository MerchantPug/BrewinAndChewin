package umpaz.brewinandchewin.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import umpaz.brewinandchewin.client.gui.KegTooltip;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;

import java.util.Optional;

public class KegItem extends BlockItem {
   private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

   public KegItem( Block block, Properties properties ) {
      super(block, properties);
   }

   @Override
   public boolean isBarVisible( ItemStack stack ) {
      return getServingCount(stack) > 0;
   }

   @Override
   public int getBarWidth( ItemStack stack ) {
      return Math.min(1 + getServingCount(stack) / 77, 13);
   }

   @Override
   public int getBarColor( ItemStack stack ) {
      return BAR_COLOR;
   }

   @Override
   public Optional<TooltipComponent> getTooltipImage( ItemStack stack ) {
      FluidStack mealStack = KegBlockEntity.getMealFromItem(stack);
      return Optional.of(new KegTooltip.KegTooltipComponent(mealStack));
   }

   private static int getServingCount( ItemStack stack ) {
      CompoundTag nbt = stack.getTagElement("BlockEntityTag");
      if ( nbt == null ) {
         return 0;
      }
      else {
         FluidStack mealStack = KegBlockEntity.getMealFromItem(stack);
         return mealStack.getAmount();
      }
   }
}