package dev.imprex.gridworldgenerator;

import java.util.function.IntFunction;

import org.bukkit.Material;

public interface GridWorldLayerProvider {

	static GridWorldLayerProvider create(int height, IntFunction<Material> layerMapper) {
		return new GridWorldLayerProvider() {
			
			@Override
			public int getHeight() {
				return height;
			}
			
			@Override
			public Material getLayer(int y) {
				return layerMapper.apply(y);
			}
		};
	}

	int getHeight();

	Material getLayer(int y);
}
