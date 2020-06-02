package me.snowleo.bleedingmobs.particles;

import me.snowleo.bleedingmobs.IBleedingMobs;
import me.snowleo.bleedingmobs.Settings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;


public class BloodStain
{
	private static final class StainedBlock
	{
		private final Location location;
		private final Material material;
		private final BlockData data;
		private final boolean meltedSnow;
		private final BlockData snowData;

		private StainedBlock(final Block block)
		{
			location = block.getLocation();
			material = block.getType();
			data = block.getBlockData();
			block.setType(Material.RED_CONCRETE);

			Block snowBlock = block.getRelative(BlockFace.UP);
			if (snowBlock.getType() == Material.SNOW)
			{
				meltedSnow = true;
				snowData = snowBlock.getBlockData();
				snowBlock.setType(Material.RED_CARPET);
			}
			else
			{
				meltedSnow = false;
				snowData = null;
			}
		}

		private Location getLocation()
		{
			return location;
		}

		private void restoreBlock()
		{
			Block block = location.getBlock();

			block.setType(material);
			block.setBlockData(data);
			if (meltedSnow)
			{
				Block restore = block.getRelative(BlockFace.UP);
				restore.setType(Material.SNOW);
				restore.setBlockData(snowData);
			}
		}
	}
	private final IBleedingMobs plugin;
	private final ParticleType type;
	private final Settings settings;
	private final int duration;
	private final StainedBlock stainedBlock;

	public BloodStain(final IBleedingMobs plugin, final ParticleType type, final Location loc)
	{
		this.settings = plugin.getSettings();
		this.plugin = plugin;
		this.type = type;
		Block block = getSolidBlock(loc);

		if (canStainBlock(block))
		{
			stainedBlock = new StainedBlock(block);
			duration = Util.getRandomBetween(type.getStainLifeFrom(), type.getStainLifeTo());
		}
		else
		{
			stainedBlock = null;
			duration = -1;
		}
	}

	private Block getSolidBlock(final Location loc)
	{
		Block block = loc.getBlock();
		if (block == null
			|| block.getType() == Material.AIR
			|| block.getType() == Material.SNOW
			|| block.getType() == Material.WATER)
		{
			block = loc.subtract(0, 1, 0).getBlock();
		}
		return block;
	}

	private boolean canStainBlock(final Block block)
	{
		return block != null && type.isStainingFloor()
			   && settings.getSaturatedMats().contains(block.getType())
			   && !plugin.getStorage().getUnbreakables().contains(block.getLocation());
	}

	public Location getStainedFloorLocation()
	{
		return stainedBlock == null ? null : stainedBlock.getLocation();
	}

	public void restore()
	{
		if (stainedBlock != null) {
			stainedBlock.restoreBlock();
		}
	}

	public int getDuration()
	{
		return duration;
	}
}
