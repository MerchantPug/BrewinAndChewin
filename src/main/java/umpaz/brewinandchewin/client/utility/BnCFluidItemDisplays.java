package umpaz.brewinandchewin.client.utility;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import umpaz.brewinandchewin.BrewinAndChewin;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BnCFluidItemDisplays {
    private static final Map<Fluid, FluidBasedItemStack> FLUID_TYPE_TO_ITEM_MAP = new HashMap<>();
    private static final Codec<ItemStack> SINGLE_ITEM_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ForgeRegistries.ITEMS.getCodec().fieldOf("id").forGetter(ItemStack::getItem),
            CompoundTag.CODEC.optionalFieldOf("tag").forGetter(stack -> Optional.ofNullable(stack.getTag()))
    ).apply(inst, (item, compoundTag) -> {
        ItemStack stack = new ItemStack(item);
        compoundTag.ifPresent(stack::setTag);
        return stack;
    }));

    public static ItemStack getFluidItemDisplay(RegistryAccess access, FluidStack fluid) {
        if (FLUID_TYPE_TO_ITEM_MAP.containsKey(fluid.getFluid()))
            return FLUID_TYPE_TO_ITEM_MAP.get(fluid.getFluid()).getStack(RegistryOps.create(NbtOps.INSTANCE, access), fluid);
        if (fluid.getFluid().getBucket() != Items.AIR)
            return fluid.getFluid().getBucket().getDefaultInstance();
        return ItemStack.EMPTY;
    }

    public static class Loader extends SimplePreparableReloadListener<Map<Fluid, FluidBasedItemStack>> {
        public static final Loader INSTANCE = new Loader();
        private static final Gson GSON = new GsonBuilder().create();

        protected Loader() {}

        @Override
        protected Map<Fluid, FluidBasedItemStack> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
            FluidBasedItemStack.CACHE.clear();
            Map<Fluid, FluidBasedItemStack> map = new HashMap<>();
            for (Resource resource : resourceManager.getResourceStack(new ResourceLocation(BrewinAndChewin.MODID, "fluid_item_displays.json"))) {
                try (Reader reader = resource.openAsReader()) {
                    JsonObject jsonObject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                    for (var e : jsonObject.entrySet()) {
                        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(e.getKey()));
                        if (fluid == null) {
                            if (e.getValue().isJsonObject() && e.getValue().getAsJsonObject().has("optional") && e.getValue().getAsJsonObject().get("optional").getAsBoolean())
                                continue;
                            BrewinAndChewin.LOG.error("Could not find fluid " + e.getKey() + " from brewinandchewin/fluid_item_displays.json in pack " + resource.sourcePackId());
                            continue;
                        }
                        if (!fluid.isSource(fluid.defaultFluidState())) {
                            BrewinAndChewin.LOG.warn("Using non source fluids in brewinandchewin/fluid_item_displays.json is discouraged.");
                        }
                        map.put(fluid, FluidBasedItemStack.createFromJson(e.getValue(), fluid));
                    }
                } catch (IllegalArgumentException | IOException | JsonParseException jsonparseexception) {
                    BrewinAndChewin.LOG.error("Couldn't parse data file brewinandchewin:fluid_item_displays.json from {}", resource.sourcePackId());
                }
            }
            return map;
        }

        @Override
        protected void apply(Map<Fluid, FluidBasedItemStack> obj, ResourceManager resourceManager, ProfilerFiller profiler) {
            FLUID_TYPE_TO_ITEM_MAP.putAll(obj);
        }
    }

    public record FluidBasedItemStack(Fluid fluid, Map<List<TagReference>, List<TagReference>> fluidTagKeyToItemTagKey, Tag raw) {
        private static final HashMap<CompoundTag, ItemStack> CACHE = new HashMap<>(32);

        private static FluidBasedItemStack createFromJson(JsonElement json, Fluid fluid) {
            Tag tag = JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, json);
            return new FluidBasedItemStack(fluid, getTagKeys(tag), tag);
        }

        private static Map<List<TagReference>, List<TagReference>> getTagKeys(Tag tag) {
            Map<List<TagReference>, List<TagReference>> map = new HashMap<>();
            List<TagReference> fluidKeys = new ArrayList<>();
            if (tag instanceof CompoundTag compoundTag) {
                for (String key : compoundTag.getAllKeys()) {
                    if (compoundTag.get(key) instanceof CompoundTag innerTag) {
                        fluidKeys.add(TagReference.createObject(key));
                        if (innerTag.contains("brewinandchewin:fluid_tag", Tag.TAG_STRING)) {
                            map.put(fluidKeys, TagReference.createFromString(innerTag.getString("brewinandchewin:fluid_tag")));
                            fluidKeys = new ArrayList<>();
                            continue;
                        } else {
                            map.putAll(getTagKeys(innerTag));
                        }
                    } else {
                        map.putAll(getTagKeys(compoundTag.get(key)));
                    }
                    fluidKeys.clear();
                }
            } else if (tag instanceof ListTag listTag) {
                for (int i = 0; i < listTag.size(); ++i) {
                    if (listTag.get(i) instanceof CompoundTag innerTag) {
                        fluidKeys.add(TagReference.createArrayValue(i));
                        if (innerTag.contains("brewinandchewin:fluid_tag", Tag.TAG_STRING)) {
                            map.put(fluidKeys, TagReference.createFromString(innerTag.getString("brewinandchewin:fluid_tag")));
                            fluidKeys = new ArrayList<>();
                            continue;
                        } else {
                            map.putAll(getTagKeys(innerTag));
                        }
                    } else {
                        map.putAll(getTagKeys(listTag.get(i)));
                    }
                    fluidKeys.clear();
                }
            }
            return ImmutableMap.copyOf(map);
        }

        private ItemStack getStack(RegistryOps<Tag> ops, FluidStack stack) {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", ForgeRegistries.FLUIDS.getKey(stack.getFluid()).toString());
            if (stack.getTag() != null)
                tag.put("tag", stack.getTag());
            if (CACHE.containsKey(tag)) {
                return CACHE.get(tag);
            }
            if (raw() instanceof CompoundTag compoundTag) {
                CompoundTag output = compoundTag.copy();
                for (Map.Entry<List<TagReference>, List<TagReference>> entry : fluidTagKeyToItemTagKey.entrySet())
                    encodeTag(entry.getKey(), entry.getValue(), stack, output);
                ItemStack item = SINGLE_ITEM_CODEC.decode(ops, output).getOrThrow(false, BrewinAndChewin.LOG::error).getFirst();
                CACHE.put(tag, item);
                return item;
            }
            ItemStack item = ForgeRegistries.ITEMS.getCodec().xmap(ItemStack::new, ItemStack::getItem).decode(ops, raw()).getOrThrow(false, BrewinAndChewin.LOG::error).getFirst();
            CACHE.put(tag, item);
            return item;
        }

        private void encodeTag(List<TagReference> fluidKeys, List<TagReference> itemKeys, FluidStack stack, CompoundTag output) {
            if (!output.contains("tag"))
                return;
            Tag current = output.get("tag");
            for (int i = 0; i < itemKeys.size(); ++i) {
                TagReference key = itemKeys.get(i);
                if (i == itemKeys.size() - 1) {
                    Tag newElement = getTagFromFluidStack(stack, fluidKeys);
                    if (key.isArrayValue()) {
                        if (current instanceof ListTag listTag) {
                            listTag.set(key.index(), newElement);
                        } else {
                            throw new RuntimeException("Unable to map tag " + String.join(".", itemKeys.stream().map(TagReference::key).toList()) + ". Tag " + String.join(".", itemKeys.stream().map(TagReference::key).toList()) + " is not a list tag.");
                        }
                    } else {
                        if (current instanceof CompoundTag compoundTag) {
                            compoundTag.put(key.key(), newElement);
                        } else {
                            throw new RuntimeException("Unable to map tag " + String.join(".", itemKeys.stream().map(TagReference::key).toList()) + ". Tag " + String.join(".", itemKeys.stream().map(TagReference::key).toList()) + " is not a compound tag.");
                        }
                    }
                    break;
                }
                if (key.isArrayValue())
                    if (current instanceof ListTag listTag) {
                        if (listTag.size() < key.index()) {
                            current = listTag.get(key.index());
                        } else {
                            throw new RuntimeException("Unable to find tag " + String.join(".", itemKeys.subList(0, i).stream().map(TagReference::key).toList()) + " in output json.");
                        }
                    } else {
                        throw new RuntimeException("Unable to map tag " + String.join(".", itemKeys.subList(0, i).stream().map(TagReference::key).toList()) + ". Tag " + String.join(".", String.join(".", itemKeys.subList(0, i).stream().map(TagReference::key).toList())) + " is not a list tag.");
                    }
                else {
                    if (current instanceof CompoundTag compoundTag) {
                        if (compoundTag.contains(key.key())) {
                            current = compoundTag.get(key.key());
                        } else {
                            throw new RuntimeException("Unable to find tag " + String.join(".", itemKeys.subList(0, i).stream().map(TagReference::key).toList()) + " in output json.");
                        }
                    } else {
                        throw new RuntimeException("Unable to map tag " + String.join(".", itemKeys.subList(0, i).stream().map(TagReference::key).toList()) + ". Tag " + String.join(".", String.join(".", itemKeys.subList(0, i).stream().map(TagReference::key).toList())) + " is not a compound tag.");
                    }
                }
            }
        }

        private Tag getTagFromFluidStack(FluidStack stack, List<TagReference> fluidKeys) {
            if (stack.getTag() == null)
                throw new RuntimeException("Fluid Stack " + ForgeRegistries.FLUIDS.getKey(stack.getFluid()) + " does not have the specified tag.");

            Tag fluidTag = stack.getTag();
            Tag returnValue = new CompoundTag();
            for (int i = 0; i < fluidKeys.size(); ++i) {
                TagReference key = fluidKeys.get(i);
                if (i == fluidKeys.size() - 1) {
                    if (key.isArrayValue()) {
                        if (fluidTag instanceof ListTag listTag) {
                            if (listTag.size() < key.index()) {
                                returnValue = listTag.get(key.index());
                            } else {
                                throw new RuntimeException("Unable to find tag " + String.join(".", fluidKeys.stream().map(TagReference::key).toList()) + " in fluid stack.");
                            }
                        } else {
                            throw new RuntimeException("Unable to map tag " + String.join(".", fluidKeys.stream().map(TagReference::key).toList()) + ". Tag " + String.join(".", fluidKeys.stream().map(TagReference::key).toList()) + " is not a list tag.");
                        }
                    } else {
                        if (fluidTag instanceof CompoundTag compoundTag) {
                            if (compoundTag.contains(key.key())) {
                                returnValue = compoundTag.get(key.key());
                            } else {
                                throw new RuntimeException("Unable to find tag " + String.join(".", fluidKeys.stream().map(TagReference::key).toList()) + " in fluid stack.");
                            }
                        } else {
                            throw new RuntimeException("Unable to map tag " + String.join(".", fluidKeys.stream().map(TagReference::key).toList()) + ". Tag " + String.join(".", fluidKeys.stream().map(TagReference::key).toList()) + " is not a compound tag.");
                        }
                    }
                    break;
                }
                if (key.isArrayValue())
                    if (fluidTag instanceof ListTag listTag) {
                        if (listTag.size() < key.index()) {
                            fluidTag = listTag.get(key.index());
                        } else {
                            throw new RuntimeException("Unable to find tag " + String.join(".", fluidKeys.subList(0, i).stream().map(TagReference::key).toList()) + " in fluid stack.");
                        }
                    } else {
                        throw new RuntimeException("Unable to map tag " + String.join(".", fluidKeys.subList(0, i).stream().map(TagReference::key).toList()) + ". Tag " + String.join(".", fluidKeys.subList(0, i).stream().map(TagReference::key).toList()) + " is not a list tag.");
                    }
                else {
                    if (fluidTag instanceof CompoundTag compoundTag) {
                        if (compoundTag.contains(key.key())) {
                            fluidTag = compoundTag.get(key.key());
                        } else {
                            throw new RuntimeException("Unable to find tag " + String.join(".", fluidKeys.subList(0, i).stream().map(TagReference::key).toList()) + " in fluid stack.");
                        }
                    } else {
                        throw new RuntimeException("Unable to map tag " + String.join(".", fluidKeys.subList(0, i).stream().map(TagReference::key).toList()) + ". Tag " + String.join(".", fluidKeys.subList(0, i).stream().map(TagReference::key).toList()) + " is not a compound tag.");
                    }
                }
            }
            return returnValue;
        }
    }
}
