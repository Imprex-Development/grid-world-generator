package dev.imprex.gridworldgenerator.util;

import java.util.Map;

import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.DoubleTag;
import com.sk89q.jnbt.EndTag;
import com.sk89q.jnbt.FloatTag;
import com.sk89q.jnbt.IntArrayTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.LongArrayTag;
import com.sk89q.jnbt.LongTag;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;

public class WorldEditUtil {

    public static net.minecraft.nbt.Tag fromNative(Tag foreign) {
        if (foreign == null) {
            return null;
        }
        if (foreign instanceof CompoundTag) {
            net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
            for (Map.Entry<String, Tag> entry : ((CompoundTag) foreign)
                .getValue().entrySet()) {
                tag.put(entry.getKey(), fromNative(entry.getValue()));
            }
            return tag;
        } else if (foreign instanceof ByteTag) {
            return net.minecraft.nbt.ByteTag.valueOf(((ByteTag) foreign).getValue());
        } else if (foreign instanceof ByteArrayTag) {
            return new net.minecraft.nbt.ByteArrayTag(((ByteArrayTag) foreign).getValue());
        } else if (foreign instanceof DoubleTag) {
            return net.minecraft.nbt.DoubleTag.valueOf(((DoubleTag) foreign).getValue());
        } else if (foreign instanceof FloatTag) {
            return net.minecraft.nbt.FloatTag.valueOf(((FloatTag) foreign).getValue());
        } else if (foreign instanceof IntTag) {
            return net.minecraft.nbt.IntTag.valueOf(((IntTag) foreign).getValue());
        } else if (foreign instanceof IntArrayTag) {
            return new net.minecraft.nbt.IntArrayTag(((IntArrayTag) foreign).getValue());
        } else if (foreign instanceof LongArrayTag) {
            return new net.minecraft.nbt.LongArrayTag(((LongArrayTag) foreign).getValue());
        } else if (foreign instanceof ListTag) {
            net.minecraft.nbt.ListTag tag = new net.minecraft.nbt.ListTag();
            ListTag foreignList = (ListTag) foreign;
            for (Tag t : foreignList.getValue()) {
                tag.add(fromNative(t));
            }
            return tag;
        } else if (foreign instanceof LongTag) {
            return net.minecraft.nbt.LongTag.valueOf(((LongTag) foreign).getValue());
        } else if (foreign instanceof ShortTag) {
            return net.minecraft.nbt.ShortTag.valueOf(((ShortTag) foreign).getValue());
        } else if (foreign instanceof StringTag) {
            return net.minecraft.nbt.StringTag.valueOf(((StringTag) foreign).getValue());
        } else if (foreign instanceof EndTag) {
            return net.minecraft.nbt.EndTag.INSTANCE;
        } else {
            throw new IllegalArgumentException("Don't know how to make NMS " + foreign.getClass().getCanonicalName());
        }
    }
}
