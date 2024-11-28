package umpaz.brewinandchewin.common.block;

import com.simibubi.create.foundation.block.CopperBlockSet;
import com.simibubi.create.foundation.block.CopperRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperSlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import umpaz.brewinandchewin.common.registry.BnCParticleTypes;

import java.util.function.Supplier;

public class IceCrateBlock extends Block {
    private static final VoxelShape FOG_AREA = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);

    public IceCrateBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(2) == 0 && isSpaceAbove(level, pos)) {
            Supplier<Vec3> supplier = () -> new Vec3(Mth.nextDouble(random, -0.005F, 0.005F), Mth.nextDouble(random, -0.025F, -0.01F), Mth.nextDouble(random, -0.005F, 0.005F));
            for(Direction direction : Direction.values()) {
                ParticleUtils.spawnParticlesOnBlockFace(level, pos, BnCParticleTypes.FOG.get(), (UniformInt.of(1, 1)), direction, supplier, (direction == Direction.UP ? 0.25D : 0.55D));
            }
        }
    }

    public boolean isSpaceAbove(Level level, BlockPos pos) {
        if (level != null) {
            BlockState above = level.getBlockState(pos.above());
            return !Shapes.joinIsNotEmpty(FOG_AREA, above.getShape(level, pos.above()), BooleanOp.AND);
        } else {
            return true;
        }
    }
}
