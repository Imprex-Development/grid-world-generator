package dev.imprex.gridworldgenerator.provider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;

import dev.imprex.gridworldgenerator.api.GridBlockProvider;
import dev.imprex.gridworldgenerator.util.WorldEditUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;

public class WorldEditClipboardProvider implements GridBlockProvider {

	private static final BlockData DEFAULT_BLOCK = Material.AIR.createBlockData();
	private static final Biome DEFAULT_BIOME = Biome.PLAINS;

	private final int width;
	private final int height;
	private final int length;

	private final int[][] heighestBlockY;
	private final BlockData[][][] blockArray;

	private final Set<Biome> biomes = new HashSet<>();
	private final Biome[][][] biomeArray;

	private final CompoundTag[][][] blockEntities;

	public WorldEditClipboardProvider(Clipboard clipboard) {
		BlockVector3 dimensions = clipboard.getDimensions();
		this.width = dimensions.getBlockX();
		this.height = dimensions.getBlockY();
		this.length = dimensions.getBlockZ();

		this.heighestBlockY = new int[width][length];
		this.blockArray = new BlockData[width][height][length];

		if (clipboard.hasBiomes()) {
			this.biomeArray = new Biome[width][height][length];
		} else {
			this.biomeArray = null;
		}

		CompoundTag[][][] blockEntities = null;

		BlockVector3 origin = clipboard.getRegion().getMinimumPoint();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					BlockVector3 position = origin.add(x, y, z);

					BaseBlock block = clipboard.getFullBlock(position);
					this.blockArray[x][y][z] = BukkitAdapter.adapt(block);

					if (y > heighestBlockY[x][z] && !block.getBlockType().getMaterial().isAir()) {
						heighestBlockY[x][z] = y;
					}

					if (block.hasNbtData()) {
						if (blockEntities == null) {
							blockEntities = new CompoundTag[width][height][length];
						}
						blockEntities[x][y][z] = block.getNbtData();
					}

					if (clipboard.hasBiomes()) {
						this.biomes.add(this.biomeArray[x][y][z] = BukkitAdapter.adapt(clipboard.getBiome(position)));
					}
				}
			}
		}

		this.blockEntities = blockEntities;
	}

	private boolean checkLocation(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < width && y < height && z < length;
	}

	@Override
	public int getHighestBlockY(int x, int z) {
		return this.checkLocation(x, 0, z)
				? this.heighestBlockY[x][z] + 1
				: 0;
	}

	@Override
	public BlockData getBlock(int x, int y, int z) {
		return this.checkLocation(x, y, z)
				? this.blockArray[x][y][z]
				: DEFAULT_BLOCK;
	}

	@Override
	public List<Biome> getBiomes() {
		return new ArrayList<>(this.biomes);
	}

	@Override
	public Biome getBiome(int x, int y, int z) {
		return this.checkLocation(x, y, z)
				? this.biomeArray[x][y][z]
				: DEFAULT_BIOME;
	}

	@Override
	public void populate(int x, int y, int z, int levelX, int levelY, int levelZ, WorldGenLevel level) {
		CompoundTag blockEntityTag = this.blockEntities[x][y][z];
		if (blockEntityTag != null) {
			var blockEntity = level.getBlockEntity(new BlockPos(levelX, levelY, levelZ));
			if (blockEntity == null) {
				System.out.println("blockEntity is null");
				return;
			}
			var blockEntityPosition = blockEntity.getBlockPos();
		
			var compound = (net.minecraft.nbt.CompoundTag) WorldEditUtil.fromNative(blockEntityTag);
			compound.putInt("x", blockEntityPosition.getX());
			compound.putInt("y", blockEntityPosition.getY());
			compound.putInt("z", blockEntityPosition.getZ());

			blockEntity.load(compound);
			blockEntity.setChanged();
		}
	}
}
