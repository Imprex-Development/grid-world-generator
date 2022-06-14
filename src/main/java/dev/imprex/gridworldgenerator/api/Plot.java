package dev.imprex.gridworldgenerator.api;

import org.bukkit.Location;

public class Plot {

	private final Location min;
	private final Location max;

	public Plot(Location min, Location max) {
		this.min = min;
		this.max = max;
	}

	public Location getMin() {
		return min;
	}

	public Location getMax() {
		return max;
	}
}
