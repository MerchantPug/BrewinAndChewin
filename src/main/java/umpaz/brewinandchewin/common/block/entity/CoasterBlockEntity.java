package umpaz.brewinandchewin.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import umpaz.brewinandchewin.common.block.CoasterBlock;
import umpaz.brewinandchewin.common.registry.BnCBlockEntityTypes;
import umpaz.brewinandchewin.common.tag.BnCTags;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import static umpaz.brewinandchewin.common.block.CoasterBlock.SIZE;

public class CoasterBlockEntity extends SyncedBlockEntity {

    public final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);

    public CoasterBlockEntity(BlockPos pos, BlockState state ) {
      super(BnCBlockEntityTypes.COASTER.get(), pos, state);
   }

    public InteractionResult onUse( Level level, BlockState state, BlockPos pos, Player player, InteractionHand hand ) {
        ItemStack heldStack = player.getItemInHand(hand);
        ItemStack offhandStack = player.getOffhandItem();
        if (state.getValue(CoasterBlock.SIZE) < 4 && (addItem(level, pos, state, heldStack, player.getAbilities().instabuild, state.getValue(CoasterBlock.SIZE)) || addItem(level, pos, state, offhandStack, player.getAbilities().instabuild, state.getValue(CoasterBlock.SIZE)))) {
            return InteractionResult.SUCCESS;
        } else if (!heldStack.isEmpty()) {
            return InteractionResult.CONSUME;
        } else if (state.getValue(CoasterBlock.SIZE) > 0 && player.getMainHandItem().isEmpty() && hand == InteractionHand.MAIN_HAND) { //Pickup Logic
            int count = state.getValue(CoasterBlock.SIZE);
            if (!player.getAbilities().instabuild && !player.addItem(inventory.get(count - 1))) {
                player.drop(inventory.get(count - 1), false);
            }
            inventory.set(count - 1, ItemStack.EMPTY);

            level.setBlockAndUpdate(pos, state
                    .setValue(CoasterBlock.SIZE, state.getValue(CoasterBlock.SIZE) - 1)
            );
            inventoryChanged();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean addItem(Level level, BlockPos pos, BlockState state, ItemStack stack, boolean instabuild, int index) {
        if (!stack.is(BnCTags.PLACEABLE_ON_COASTER))
            return false;
        level.setBlock(pos, state.setValue(SIZE, index + 1), 3);
        inventory.set(index, stack.copyWithCount(1));
        inventoryChanged();
        if (!instabuild)
            stack.shrink(1);
        return true;
    }

   @Override
   public void load( CompoundTag nbt ) {
       super.load(nbt);
       ContainerHelper.loadAllItems(nbt, this.inventory);
   }

   @Override
   protected void saveAdditional( CompoundTag nbt ) {
       super.saveAdditional(nbt);
       ContainerHelper.saveAllItems(nbt, this.inventory);
   }

   public NonNullList<ItemStack> getItems() {
       return inventory;
   }
}