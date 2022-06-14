package dev.imprex.gridworldgenerator;

import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import dev.imprex.gridworldgenerator.api.GridBlockProvider;
import dev.imprex.gridworldgenerator.provider.DefaultGridBlockLayerProvider;

/**
 * PARAMETER:
 * - grid-size:
 *   - size of the whole grid
 *   - COLUMNS X ROWS
 *   - example 10x6:
 *     ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗
 *     ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝
 *     ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗
 *     ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝
 *     ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗
 *     ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝
 *     ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗
 *     ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝
 *     ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗
 *     ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝
 *     ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗ ╔═╗
 *     ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝ ╚═╝
 * 
 * - plot-size:
 *   - size of actual plot in blocks 
 *   - columns X rows
 *  
 * - gap:
 *   - distance between neighboring plots in blocks
 *   - column-gap X row-gap
 *   - example: 4x4
 *     x x x ║║ g g g g ║║ x x x x x x x x
 *     x x x ║║ g g g g ║║ x x x x x x x x
 *     ══════╝║ g g g g ║╚════════════════
 *     ═══════╝ g g g g ╚═════════════════
 *     g g g g  g g g g  g g g g g g g g g
 *     g g g g  g g g g  g g g g g g g g g
 *     g g g g  g g g g  g g g g g g g g g
 *     g g g g  g g g g  g g g g g g g g g
 *     ═══════╗ g g g g ╔═════════════════
 *     ══════╗║ g g g g ║╔════════════════
 *     x x x ║║ g g g g ║║ x x x x x x x x
 *     x x x ║║ g g g g ║║ x x x x x x x x
 * 
 * - blocks in the example above:
 *   - block x is retrieved from LayerProvider
 *   - block p is fixed gap block
 */
public final class GridWorldBuilder {

	int gridWidth = 10;
	int gridHeight = 10;

	int plotWidth = 100;
	int plotHeight = 100;

	int gapWidth = 5;
	int gapHeight = 5;

	BlockData gapBlockData = Material.BARRIER.createBlockData();
	GridBlockProvider blockProvider = new DefaultGridBlockLayerProvider();

	public GridWorldBuilder withGridSize(int width, int height) {
		this.gridWidth = Math.max(1, width);
		this.gridHeight = Math.max(1, height);
		return this;
	}

	public GridWorldBuilder withPlotSize(int width, int height) {
		this.plotWidth = Math.max(1, width);
		this.plotHeight = Math.max(1, height);
		return this;
	}

	public GridWorldBuilder withGapSize(int width, int height) {
		this.gapWidth = Math.max(0, width);
		this.gapHeight = Math.max(0, height);
		return this;
	}

	public GridWorldBuilder withGapMaterial(Material material) {
		return this.withGapMaterial(material.createBlockData());
	}

	public GridWorldBuilder withGapMaterial(BlockData blockData) {
		this.gapBlockData = Objects.requireNonNull(blockData);
		return this;
	}

	public GridWorldBuilder withBlockProvider(GridBlockProvider blockProvider) {
		this.blockProvider = Objects.requireNonNull(blockProvider);
		return this;
	}

	public GridWorldGenerator build() {
		return new GridWorldGenerator(this);
	}
}
