package dev.imprex.gridworldgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import dev.imprex.gridworldgenerator.api.GridBlockProvider;
import dev.imprex.gridworldgenerator.api.Plot;

public class GridWorldGenerator extends ChunkGenerator {

	private final GridWorldBuilder builder;

	private final int gridWidth;
	private final int gridHeight;

	private final int plotWidth;
	private final int plotHeight;

	private final int gapWidth;
	private final int gapHeight;

	private final int strideWidth;
	private final int strideHeight;

	private final int worldWidth;
	private final int worldHeight;

	private final int maxChunkX;
	private final int maxChunkZ;

	private final BlockData gapBlockData;
	private final GridBlockProvider blockProvider;

	GridWorldGenerator(GridWorldBuilder builder) {
		this.builder = builder;

		this.gridWidth = builder.gridWidth;
		this.gridHeight = builder.gridHeight;
		
		this.plotWidth = builder.plotWidth;
		this.plotHeight = builder.plotHeight;
		
		this.gapWidth = builder.gapWidth;
		this.gapHeight = builder.gapHeight;

		this.strideWidth = this.plotWidth + this.gapWidth;
		this.strideHeight = this.plotHeight + this.gapHeight;

		this.worldWidth = this.strideWidth * this.gridWidth + this.gapWidth;
		this.worldHeight = this.strideHeight * this.gridHeight + this.gapHeight;

		this.maxChunkX = (int) Math.ceil(this.worldWidth / 16d);
		this.maxChunkZ = (int) Math.ceil(this.worldHeight / 16d);

		this.gapBlockData = builder.gapBlockData;
		this.blockProvider = builder.blockProvider;
	}

	public List<Plot> getPlots(World world) {
		List<Plot> plots = new ArrayList<>();

		Location base = new Location(world, this.gapWidth, 0, this.gapHeight);

		for (int x = 0; x < this.gridWidth; x++) {
			for (int z = 0; z < this.gridHeight; z++) {
				Location min = base.clone().add(x * this.strideWidth, 0, z * this.strideHeight);
				Location max = min.clone().add(this.plotWidth, world.getMaxHeight(), this.plotHeight);
				plots.add(new Plot(min, max));
			}
		}

		return plots;
	}

	@Override
	public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
		if (chunkX < 0 || chunkX >= this.maxChunkX || chunkZ < 0 || chunkZ >= this.maxChunkZ) {
			return;
		}

		int maxX = chunkX + 1 == this.maxChunkX && this.worldWidth % 16 != 0 ? this.worldWidth % 16 : 16;
		int maxZ = chunkZ + 1 == this.maxChunkZ && this.worldHeight % 16 != 0 ? this.worldHeight % 16 : 16;

		int positionX = chunkX << 4;
		int positionZ = chunkZ << 4;

		for (int sectionX = 0; sectionX < maxX; sectionX++) {
			for (int sectionZ = 0; sectionZ < maxZ; sectionZ++) {

				int x = ((positionX + sectionX) % this.strideWidth);
				int z = ((positionZ + sectionZ) % this.strideHeight);

				boolean gap = x < this.gapWidth || z < this.gapHeight;
				x -= this.gapWidth;
				z -= this.gapHeight;

				int maxY = gap ? chunkData.getMaxHeight() : chunkData.getMinHeight() + this.blockProvider.getHighestBlockY(x, z);

				for (int sectionY = chunkData.getMinHeight(); sectionY < maxY; sectionY++) {
					chunkData.setBlock(sectionX, sectionY, sectionZ, gap ? this.gapBlockData
							: this.blockProvider.getBlock(x, sectionY - chunkData.getMinHeight(), z));
				}
			}
		}

		chunkData.setRegion(
				0, chunkData.getMaxHeight() - 4, 0,
				maxX, chunkData.getMaxHeight(), maxZ, this.gapBlockData);
	}

	@Override
	public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
		return new GridWorldBiomeProvider(this.builder);
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return List.of(new GridWorldBlockPopulator(this.builder));
	}
}
