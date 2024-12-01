package umpaz.brewinandchewin.client.renderer.texture.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import umpaz.brewinandchewin.BrewinAndChewin;

public class GrassTintTextureModifier implements TextureModifier {
    public static final ResourceLocation ID = BrewinAndChewin.asResource("grass_tint");
    public static final Codec<GrassTintTextureModifier> CODEC = Codec.unit(GrassTintTextureModifier::new);

    @Override
    public int color(BlockAndTintGetter level, BlockState state, BlockPos pos, ItemStack stack, int previous) {
        return BiomeColors.getAverageGrassColor(level, pos);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Codec<? extends TextureModifier> codec() {
        return CODEC;
    }
}
