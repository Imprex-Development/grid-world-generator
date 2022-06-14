package dev.imprex.gridworldgenerator;

import java.util.Random;

import org.bukkit.craftbukkit.v1_18_R2.generator.CraftLimitedRegion;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import dev.imprex.gridworldgenerator.api.GridBlockProvider;
import net.minecraft.world.level.WorldGenLevel;

public class GridWorldBlockPopulator extends BlockPopulator {

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

	private final GridBlockProvider blockProvider;

	GridWorldBlockPopulator(GridWorldBuilder builder) {
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

		this.blockProvider = builder.blockProvider;
	}

	@Override
	public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion) {
		if (chunkX < 0 || chunkX >= this.maxChunkX || chunkZ < 0 || chunkZ >= this.maxChunkZ) {
			return;
		}

		WorldGenLevel level = ((CraftLimitedRegion) limitedRegion).getHandle();

		int maxX = chunkX + 1 == this.maxChunkX && this.worldWidth % 16 != 0 ? this.worldWidth % 16 : 16;
		int maxZ = chunkZ + 1 == this.maxChunkZ && this.worldHeight % 16 != 0 ? this.worldHeight % 16 : 16;

		int positionX = chunkX << 4;
		int positionZ = chunkZ << 4;

		for (int sectionX = 0; sectionX < maxX; sectionX++) {
			for (int sectionZ = 0; sectionZ < maxZ; sectionZ++) {

				int x = ((positionX + sectionX) % this.strideWidth);
				int z = ((positionZ + sectionZ) % this.strideHeight);

				if (x < this.gapWidth || z < this.gapHeight) {
					continue;
				}

				x -= this.gapWidth;
				z -= this.gapHeight;

				int maxY = level.getMinBuildHeight() + this.blockProvider.getHighestBlockY(x, z);

				for (int sectionY = level.getMinBuildHeight(); sectionY < maxY; sectionY++) {
					this.blockProvider.populate(x, sectionY - level.getMinBuildHeight(), z,
							positionX + sectionX, sectionY, positionZ + sectionZ, level);
				}
			}
		}
	}
}
