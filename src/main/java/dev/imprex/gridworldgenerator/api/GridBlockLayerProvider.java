package dev.imprex.gridworldgenerator.api;

import java.util.function.IntFunction;

import org.bukkit.block.data.BlockData;

public interface GridBlockLayerProvider extends GridBlockProvider {

	static GridBlockLayerProvider create(int height, IntFunction<BlockData> layerMapper) {
		return new GridBlockLayerProvider() {
			
			@Override
			public int getHighestBlockY() {
				return height;
			}
			
			@Override
			public BlockData getBlockLayer(int y) {
				return layerMapper.apply(y);
			}
		};
	}

	@Override
	default BlockData getBlock(int x, int y, int z) {
		return getBlockLayer(y);
	}

	@Override
	default int getHighestBlockY(int x, int z) {
		return this.getHighestBlockY();
	}

	int getHighestBlockY();

	BlockData getBlockLayer(int y);
}
