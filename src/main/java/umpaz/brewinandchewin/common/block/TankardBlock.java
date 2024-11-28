package umpaz.brewinandchewin.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import umpaz.brewinandchewin.common.block.entity.TankardBlockEntity;
import umpaz.brewinandchewin.common.item.BoozeItem;
import umpaz.brewinandchewin.common.registry.BnCBlockEntityTypes;
import umpaz.brewinandchewin.common.registry.BnCItems;

public class TankardBlock extends BaseEntityBlock {
   public static final int MAX = RotationSegment.getMaxSegmentIndex();
   private static final int ROTATIONS = MAX + 1;
   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
   public static final IntegerProperty SIZE = IntegerProperty.create("size", 0, 3);

   public TankardBlock( Properties properties ) {
      super(properties.noOcclusion());
      this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, Integer.valueOf(0)).setValue(SIZE, 0));
   }

   @Override
   public InteractionResult use( BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit ) {
      if ( level.getBlockEntity(pos) instanceof TankardBlockEntity tankardEntity ) {
         return tankardEntity.onUse(level, state, pos, player, hand);
      }
      return super.use(state, level, pos, player, hand, hit);
   }

   @Override
   public BlockState getStateForPlacement( BlockPlaceContext context ) {
      BlockState state = context.getLevel().getBlockState(context.getClickedPos());
      if ( state.is(this) ) {
         return state.setValue(SIZE, Math.min(4, state.getValue(SIZE) + 1));
      }
      else {
         return super.getStateForPlacement(context).setValue(ROTATION, RotationSegment.convertToSegment(context.getRotation()));
      }
   }

   @Override
   public boolean canBeReplaced( BlockState state, BlockPlaceContext context ) {
      return !context.isSecondaryUseActive() &&
              ( context.getItemInHand().getItem() instanceof BoozeItem || context.getItemInHand().is(BnCItems.TANKARD.get()) ) &&
              state.getValue(SIZE) < 3 || super.canBeReplaced(state, context);
   }

   @Override
   public ItemStack getCloneItemStack( BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player ) {
      if ( level.getBlockEntity(pos) instanceof TankardBlockEntity tankardEntity ) {
         // find first that isnt air
         ItemStack stack = tankardEntity.getItems().stream().filter(i -> !i.isEmpty()).findFirst().orElse(ItemStack.EMPTY);
         return stack.copy();
      }
      return super.getCloneItemStack(state, target, level, pos, player);
   }

   @Override
   public void setPlacedBy( Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack ) {
      if ( level.getBlockEntity(pos) instanceof TankardBlockEntity tankardEntity ) {
         int count = (int) tankardEntity.inventory.stream().filter(i -> !i.isEmpty()).count();
         if ( count == 0 ) {
            tankardEntity.getItems().set(count, new ItemStack(itemStack.getItem(), 1));
         }
         else {
            tankardEntity.getItems().set(count, new ItemStack(itemStack.getItem(), 1));
         }
         if ( placer != null )
            level.setBlockAndUpdate(pos, state
                    .setValue(ROTATION, RotationSegment.convertToSegment(placer.getYRot())));
      }
      super.setPlacedBy(level, pos, state, placer, itemStack);
   }

   @Override
   public VoxelShape getShape( BlockState state, BlockGetter level, BlockPos pos, CollisionContext context ) {
      VoxelShape shape = Block.box(5.5, 0, 5.5, 10.5, 7, 10.5);
      switch ( state.getValue(SIZE) ) {
         case ( 0 ) -> shape = Block.box(5.5, 0, 5.5, 10.5, 7, 10.5);
         case ( 1 ), ( 2 ), ( 3 ) -> shape = Block.box(1.5, 0, 1.5, 14.5, 7, 14.5);
      }
      return shape;
   }


   @Override
   public boolean canSurvive( BlockState state, LevelReader level, BlockPos pos ) {
      BlockPos floorPos = pos.below();
      BlockState floorState = level.getBlockState(floorPos);
      return floorState.isFaceSturdy(level, floorPos, Direction.UP);
   }

   @Override
   public void onRemove( BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved ) {
      if ( !state.is(newState.getBlock()) ) {
         BlockEntity blockEntity = level.getBlockEntity(pos);
         if ( blockEntity instanceof TankardBlockEntity be ) {
            Containers.dropContents(level, pos, be.getItems());
            level.updateNeighbourForOutputSignal(pos, this);
         }
         super.onRemove(state, level, pos, newState, moved);
      }
   }

   @javax.annotation.Nullable
   @Override
   public BlockEntity newBlockEntity( BlockPos pos, BlockState state ) {
      return BnCBlockEntityTypes.TANKARD.get().create(pos, state);
   }

   @Override
   protected void createBlockStateDefinition( StateDefinition.Builder<Block, BlockState> builder ) {
      builder.add(ROTATION, SIZE);
   }
}
