package dev.imprex.gridworldgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

public class GridWorldGenerator extends ChunkGenerator {

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
	private final GridWorldLayerProvider layerProvider;

	GridWorldGenerator(GridWorldBuilder gridWorldBuilder) {
		this.gridWidth = gridWorldBuilder.gridWidth;
		this.gridHeight = gridWorldBuilder.gridHeight;
		
		this.plotWidth = gridWorldBuilder.plotWidth;
		this.plotHeight = gridWorldBuilder.plotHeight;
		
		this.gapWidth = gridWorldBuilder.gapWidth;
		this.gapHeight = gridWorldBuilder.gapHeight;

		this.strideWidth = this.plotWidth + this.gapWidth;
		this.strideHeight = this.plotHeight + this.gapHeight;

		this.worldWidth = this.strideWidth * this.gridWidth + this.gapWidth;
		this.worldHeight = this.strideHeight * this.gridHeight + this.gapHeight;

		this.maxChunkX = (int) Math.ceil(this.worldWidth / 16d);
		this.maxChunkZ = (int) Math.ceil(this.worldHeight / 16d);

		this.gapBlockData = gridWorldBuilder.gapBlockData;
		this.layerProvider = gridWorldBuilder.layerProvider;
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

		int absMinX = chunkX << 4;
		int absMinZ = chunkZ << 4;

		for (int sectionX = 0; sectionX < maxX; sectionX++) {
			for (int sectionZ = 0; sectionZ < maxZ; sectionZ++) {

				int x = ((absMinX + sectionX) % this.strideWidth);
				int z = ((absMinZ + sectionZ) % this.strideHeight);

				boolean gap = x < this.gapWidth || z < this.gapHeight;
				int maxY = gap ? chunkData.getMaxHeight() : chunkData.getMinHeight() + this.layerProvider.getHeight();

				for (int sectionY = chunkData.getMinHeight(); sectionY < maxY; sectionY++) {
					chunkData.setBlock(sectionX, sectionY, sectionZ, gap ? this.gapBlockData
							: this.layerProvider.getLayer(sectionY - chunkData.getMinHeight()).createBlockData());
				}
			}
		}

		chunkData.setRegion(
				0, chunkData.getMaxHeight() - 4, 0,
				maxX, chunkData.getMaxHeight(), maxZ, this.gapBlockData);
	}
}
