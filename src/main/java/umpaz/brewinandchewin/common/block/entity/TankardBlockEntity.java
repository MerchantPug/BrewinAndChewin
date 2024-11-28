package umpaz.brewinandchewin.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.BlockHitResult;
import umpaz.brewinandchewin.common.block.TankardBlock;
import umpaz.brewinandchewin.common.registry.BnCBlockEntityTypes;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import static umpaz.brewinandchewin.common.block.TankardBlock.ROTATION;

public class TankardBlockEntity extends SyncedBlockEntity {

   public final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);

   public TankardBlockEntity( BlockPos pos, BlockState state ) {
      super(BnCBlockEntityTypes.TANKARD.get(), pos, state);
   }

   public InteractionResult onUse( Level level, BlockState state, BlockPos pos, Player player, InteractionHand hand ) {
      if ( player.getMainHandItem().isEmpty() && hand == InteractionHand.MAIN_HAND ) { //Pickup Logic
         int count = (int) getItems().stream().filter(i -> !i.isEmpty()).count();
         if ( !player.getInventory().add(player.getInventory().selected, inventory.get(count - 1)) ) {
            player.drop(inventory.get(count - 1), false);
            inventory.set(count - 1, ItemStack.EMPTY);
         }

         if ( count == 1 ) {
            level.removeBlock(pos, false);
         }
         else {
            level.setBlockAndUpdate(pos, state
                    .setValue(TankardBlock.SIZE, state.getValue(TankardBlock.SIZE) - 1)
                    .setValue(ROTATION, RotationSegment.convertToSegment(player.getYRot()))
            );
         }
         level.playSound(player, pos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
         return InteractionResult.SUCCESS;
      }
      inventoryChanged();
      return InteractionResult.PASS;
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