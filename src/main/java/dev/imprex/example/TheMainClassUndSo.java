package dev.imprex.example;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.imprex.gridworldgenerator.GridWorldBuilder;

public class TheMainClassUndSo extends JavaPlugin {

	@Override
	public void onEnable() {
		try {
			Path path = Paths.get("dev/");
			if (Files.exists(path)) {
				Files.walkFileTree(Paths.get("dev/"), new SimpleFileVisitor<Path>() {
					
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}
					
					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						Files.delete(dir);
						return FileVisitResult.CONTINUE;
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		new WorldCreator("dev")
				.type(WorldType.FLAT)
				.environment(Environment.NORMAL)
				.generator(new GridWorldBuilder().build())
				.createWorld();
	}

	@Override
	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.kickPlayer("");
		}
		Bukkit.unloadWorld("dev", false);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("world-tp") && sender instanceof Player player) {
			if (args.length == 0) {
				sender.sendMessage("/world-tp <world>");
				return false;
			}

			World world = Bukkit.getWorld(args[0]);
			if (world != null) {
				Location location = player.getLocation();
				location.setWorld(world);
				player.teleport(location);
			} else {
				sender.sendMessage("unknown world");
			}
			
			return true;
		}
		return false;
	}
}
