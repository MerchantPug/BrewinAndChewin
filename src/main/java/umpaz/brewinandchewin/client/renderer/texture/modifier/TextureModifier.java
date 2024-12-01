package umpaz.brewinandchewin.client.renderer.texture.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface TextureModifier {
    default int color(BlockAndTintGetter level, BlockState state, BlockPos pos, ItemStack stack, int previous) {
        return previous;
    }

    default RenderType renderType(BlockAndTintGetter level, BlockState state, BlockPos pos, ItemStack stack, RenderType previous) {
        return previous;
    }

    ResourceLocation getId();

    Codec<? extends TextureModifier> codec();
}