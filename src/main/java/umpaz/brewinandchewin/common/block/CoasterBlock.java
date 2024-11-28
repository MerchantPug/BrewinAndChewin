package umpaz.brewinandchewin.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import umpaz.brewinandchewin.common.block.entity.CoasterBlockEntity;

public class CoasterBlock extends BaseEntityBlock {

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    public static final IntegerProperty SIZE = IntegerProperty.create("size", 0, 4);
    public static final BooleanProperty INVISIBLE = BooleanProperty.create("invisible");

    protected static final VoxelShape COASTER_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D);
    protected static final VoxelShape[] SHAPES_WITHOUT_COASTER = {
            Block.box(6.0D, 0.0D, 6.0D, 11.0D, 6.0D, 11.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 6.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 6.0D, 15.0D)
    };
    protected static final VoxelShape[] SHAPES_WITH_COASTER = {
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D)
    };

    public CoasterBlock() {
        super(Properties.copy(Blocks.BROWN_CARPET).sound(SoundType.WOOD).instabreak());
        this.registerDefaultState(this.getStateDefinition().any().setValue(ROTATION, 0).setValue(SIZE, 0).setValue(INVISIBLE, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        int size = state.getValue(SIZE);
        if (size > 0)
            return state.getValue(INVISIBLE) ? SHAPES_WITHOUT_COASTER[size - 1] : SHAPES_WITH_COASTER[size - 1];
        return COASTER_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return COASTER_SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof CoasterBlockEntity coasterBlockEntity) {
            return coasterBlockEntity.onUse(worldIn, state, pos, player, handIn);
        }

        return InteractionResult.PASS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ROTATION, RotationSegment.convertToSegment(context.getRotation() + 180.0F));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos floorPos = pos.below();
        return canSupportRigidBlock(level, floorPos) || canSupportCenter(level, floorPos, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ROTATION, SIZE, INVISIBLE);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(ROTATION, pRot.rotate(pState.getValue(ROTATION), 16));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.setValue(ROTATION, pMirror.mirror(pState.getValue(ROTATION), 16));
    }

    @Override
    public void onRemove( BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved ) {
        if ( !state.is(newState.getBlock()) ) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if ( blockEntity instanceof CoasterBlockEntity be ) {
                Containers.dropContents(level, pos, be.getItems());
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return Math.max(blockState.getValue(SIZE) * 4, 15);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CoasterBlockEntity(pPos, pState);
    }
}