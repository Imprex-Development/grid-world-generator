package dev.imprex.gridworldgenerator;

import org.bukkit.Material;

public class DefaultGridWorldLayerProvider implements GridWorldLayerProvider {

	@Override
	public int getHeight() {
		return 64;
	}

	@Override
	public Material getLayer(int y) {
		if (y < 4) {
			return Material.BEDROCK;
		} else if (y < 59) {
			return Material.STONE;
		} else if (y < 63) {
			return Material.DIRT;
		} else {
			return Material.GRASS_BLOCK;
		}
	}
}
