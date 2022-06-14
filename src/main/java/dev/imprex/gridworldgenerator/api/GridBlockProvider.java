package dev.imprex.gridworldgenerator.api;

import java.util.List;

import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

import com.sk89q.worldedit.extent.clipboard.Clipboard;

import dev.imprex.gridworldgenerator.provider.WorldEditClipboardProvider;
import net.minecraft.world.level.WorldGenLevel;

public interface GridBlockProvider {

	static GridBlockProvider create(Clipboard clipboard) {
		return new WorldEditClipboardProvider(clipboard);
	}

	int getHighestBlockY(int x, int z);

	BlockData getBlock(int x, int y, int z);

	default List<Biome> getBiomes() {
		return List.of(Biome.PLAINS);
	}

	default Biome getBiome(int x, int y, int z) {
		return Biome.PLAINS;
	}

	default void populate(int x, int y, int z, int levelX, int levelY, int levelZ, WorldGenLevel level) {
	}
}
