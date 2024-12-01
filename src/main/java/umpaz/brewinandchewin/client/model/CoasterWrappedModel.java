package umpaz.brewinandchewin.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoasterWrappedModel extends BakedModelWrapper<BakedModel> {
    public static final ModelProperty<Integer> TINT_INDEX = new ModelProperty<>();

    public CoasterWrappedModel(BakedModel originalModel) {
        super(originalModel);
    }

    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        List<BakedQuad> quads = super.getQuads(state, side, rand);
        if (data.has(TINT_INDEX)) {
            for (BakedQuad quad : quads)
                quad.tintIndex = data.get(TINT_INDEX);
        }
        return quads;
    }
}
