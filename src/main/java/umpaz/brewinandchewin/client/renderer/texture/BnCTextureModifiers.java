package umpaz.brewinandchewin.client.renderer.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import umpaz.brewinandchewin.client.renderer.texture.modifier.GlintTextureModifier;
import umpaz.brewinandchewin.client.renderer.texture.modifier.GrassTintTextureModifier;
import umpaz.brewinandchewin.client.renderer.texture.modifier.PotionTintTextureModifier;
import umpaz.brewinandchewin.client.renderer.texture.modifier.TextureModifier;

import java.util.HashMap;
import java.util.Map;

public class BnCTextureModifiers {
    private static final Map<ResourceLocation, Codec<? extends TextureModifier>> FACTORIES = new HashMap<>();
    private static final Map<Codec<? extends TextureModifier>, ResourceLocation> FACTORIES_TO_KEY = new HashMap<>();

    public static final Codec<? extends TextureModifier> CODEC =  ResourceLocation.CODEC.comapFlatMap(key -> {
        if (!FACTORIES.containsKey(key))
            return DataResult.error(() -> "Could not find texture modifier type " + key);
        return DataResult.success(FACTORIES.get(key));
    }, FACTORIES_TO_KEY::get).dispatch(textureModifier -> (Codec) textureModifier.codec(), mapCodec -> mapCodec);

    public static void register(ResourceLocation id, Codec<? extends TextureModifier> codec) {
        if (FACTORIES.containsKey(id)) {
            throw new UnsupportedOperationException("Attempted to register duplicate ID texture modifiers. " + id);
        }
        FACTORIES.put(id, codec);
        FACTORIES_TO_KEY.put(codec, id);
    }

    public static void init() {
        register(GlintTextureModifier.ID, GlintTextureModifier.CODEC);
        register(GrassTintTextureModifier.ID, GrassTintTextureModifier.CODEC);
        register(PotionTintTextureModifier.ID, PotionTintTextureModifier.CODEC);
    }
}
