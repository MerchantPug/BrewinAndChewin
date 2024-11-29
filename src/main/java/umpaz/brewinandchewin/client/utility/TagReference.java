package umpaz.brewinandchewin.client.utility;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public interface TagReference {

    static TagReference createArrayValue(int index) {
        if (index < 0)
            throw new IllegalStateException("Cannot create an array value with an index less than 0.");
        return new ArrayValue(index);
    }

    static TagReference createObject(String key) {
        return new Object(key);
    }

    static List<TagReference> createFromString(String string) {
        List<TagReference> references = new ArrayList<>();
        String[] strings = string.split("\\.");
        for (String key : strings) {
            if (key.matches("/\\[[0-9]+]/")) {
                references.add(createArrayValue(Integer.getInteger(key.substring(1, key.length() - 1))));
            } else {
                references.add(createObject(key));
            }
        }
        return references;
    }

    default boolean isArrayInObject() {
        return this instanceof ArrayInObject;
    }

    default boolean isArrayValue() {
        return this instanceof ArrayValue;
    }

    default boolean isObject() {
        return this instanceof Object;
    }

    default int index() {
        return -1;
    }

    default String key() {
        return "";
    }

    record ArrayInObject(String key, int index) implements TagReference {
        @Override
        public int index() {
            return index;
        }

        @Override
        public String key() {
            return key;
        }
    }

    record ArrayValue(int index) implements TagReference {
        @Override
        public int index() {
            return index;
        }
    }

    record Object(String key) implements TagReference {
        @Override
        public String key() {
            return key;
        }
    }
}
