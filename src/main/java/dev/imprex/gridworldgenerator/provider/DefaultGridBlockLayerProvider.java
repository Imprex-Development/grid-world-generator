package dev.imprex.gridworldgenerator.provider;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import dev.imprex.gridworldgenerator.api.GridBlockLayerProvider;

public class DefaultGridBlockLayerProvider implements GridBlockLayerProvider {

	private static final BlockData BEDROCK = Material.BEDROCK.createBlockData();
	private static final BlockData STONE = Material.STONE.createBlockData();
	private static final BlockData DIRT = Material.DIRT.createBlockData();
	private static final BlockData GRASS_BLOCK = Material.GRASS_BLOCK.createBlockData();

	@Override
	public int getHighestBlockY() {
		return 64;
	}

	@Override
	public BlockData getBlockLayer(int y) {
		if (y < 4) {
			return BEDROCK;
		} else if (y < 59) {
			return STONE;
		} else if (y < 63) {
			return DIRT;
		} else {
			return GRASS_BLOCK;
		}
	}
}
