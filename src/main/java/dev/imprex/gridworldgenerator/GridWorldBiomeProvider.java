package dev.imprex.gridworldgenerator;

import java.util.List;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;

import dev.imprex.gridworldgenerator.api.GridBlockProvider;

public class GridWorldBiomeProvider extends BiomeProvider {

	private final int gapWidth;
	private final int gapHeight;

	private final int strideWidth;
	private final int strideHeight;

	private final int worldWidth;
	private final int worldHeight;

	private final GridBlockProvider blockProvider;

	GridWorldBiomeProvider(GridWorldBuilder builder) {
		this.gapWidth = builder.gapWidth;
		this.gapHeight = builder.gapHeight;

		this.strideWidth = builder.plotWidth + this.gapWidth;
		this.strideHeight = builder.plotHeight + this.gapHeight;

		this.worldWidth = this.strideWidth * builder.gridWidth + this.gapWidth;
		this.worldHeight = this.strideHeight * builder.gridHeight + this.gapHeight;

		this.blockProvider = builder.blockProvider;
	}

	@Override
	public Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {
		if (x >= this.worldWidth || z >= this.worldHeight) {
			return Biome.THE_VOID;
		}

		x %= this.strideWidth;
		z %= this.strideHeight;
		if (x < this.gapWidth || z < this.gapHeight) {
			return Biome.THE_VOID;
		}

		x -= this.gapWidth;
		z -= this.gapHeight;
		return this.blockProvider.getBiome(x, y, z);
	}

	@Override
	public List<Biome> getBiomes(WorldInfo worldInfo) {
		return this.blockProvider.getBiomes();
	}

}
