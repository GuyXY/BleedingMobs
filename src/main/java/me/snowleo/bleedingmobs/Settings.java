/*
 * BleedingMobs - make your monsters and players bleed
 *
 * Copyright (C) 2011-2012 snowleo
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.snowleo.bleedingmobs;

import java.util.*;
import me.snowleo.bleedingmobs.particles.ParticleType;
import me.snowleo.bleedingmobs.particles.Util;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

public class Settings
{
	private static final int MAX_PARTICLES = 2000;
	private volatile Set<String> worlds = Collections.emptySet();
	private volatile boolean bleedWhenCanceled = false;
	private volatile boolean bleedingEnabled = true;
	private volatile int maxParticles = MAX_PARTICLES;
	private final IBleedingMobs plugin;
	private volatile boolean permissionOnly = false;
	private volatile int attackPercentage = 30;
	private volatile int fallPercentage = 20;
	private volatile int deathPercentage = 50;
	private volatile int projectilePercentage = 25;
	private volatile int bloodstreamPercentage = 10;
	private volatile int bloodstreamTime = 200;
	private volatile int bloodstreamInterval = 10;
	private volatile EnumSet<Material> particleMaterials = EnumSet.allOf(Material.class);

	private volatile EnumSet<Material> saturatedMats = EnumSet.copyOf(Arrays.asList(
			Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.PODZOL,
			Material.STONE, Material.GRANITE, Material.DIORITE, Material.ANDESITE,
			Material.POLISHED_GRANITE, Material.POLISHED_DIORITE, Material.POLISHED_ANDESITE,
			Material.COBBLESTONE,
			Material.SAND, Material.RED_SAND,
			Material.SANDSTONE, Material.CUT_SANDSTONE, Material.CHISELED_SANDSTONE,
			Material.RED_SANDSTONE, Material.CUT_RED_SANDSTONE, Material.CHISELED_RED_SANDSTONE,
			Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.JUNGLE_LOG, Material.DARK_OAK_LOG, Material.ACACIA_LOG,
			Material.OAK_PLANKS, Material.BIRCH_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS, Material.DARK_OAK_PLANKS, Material.ACACIA_PLANKS,
			Material.GRAVEL,
			Material.NETHERRACK,
			Material.CLAY,
			Material.SNOW_BLOCK,
			Material.BRICK,
			Material.MOSSY_COBBLESTONE));

	public Settings(final IBleedingMobs plugin)
	{
		this.plugin = plugin;
		loadConfig();
		saveConfig();
	}

	public final void loadConfig()
	{
		plugin.reloadConfig();
		final FileConfiguration config = plugin.getConfig();
		bleedingEnabled = config.getBoolean("enabled", true);
		final int newMaxParticles = Math.max(1, Math.min(Util.COUNTER_SIZE, config.getInt("max-particles", MAX_PARTICLES)));
		if (plugin.getStorage() != null)
		{
			plugin.getStorage().getItems().setLimit(newMaxParticles);
		}
		maxParticles = newMaxParticles;
		bleedWhenCanceled = config.getBoolean("bleed-when-canceled", bleedWhenCanceled);
		permissionOnly = config.getBoolean("permission-only", permissionOnly);
		attackPercentage = Math.max(0, Math.min(2000, config.getInt("attack-percentage", attackPercentage)));
		fallPercentage = Math.max(0, Math.min(2000, config.getInt("fall-percentage", fallPercentage)));
		deathPercentage = Math.max(0, Math.min(2000, config.getInt("death-percentage", deathPercentage)));
		projectilePercentage = Math.max(0, Math.min(2000, config.getInt("projectile-percentage", projectilePercentage)));
		bloodstreamPercentage = Math.max(0, Math.min(2000, config.getInt("bloodstream.percentage", bloodstreamPercentage)));
		bloodstreamTime = Math.max(0, Math.min(72000, config.getInt("bloodstream.time", bloodstreamTime)));
		bloodstreamInterval = Math.max(1, Math.min(1200, config.getInt("bloodstream.interval", bloodstreamInterval)));
		EnumSet<Material> partMaterials = EnumSet.noneOf(Material.class);
		partMaterials.add(Material.CAKE);
		partMaterials.add(Material.BONE);
		partMaterials.add(Material.RED_WOOL);
		partMaterials.add(Material.RED_CONCRETE);
		partMaterials.add(Material.RED_DYE);
		partMaterials.add(Material.REDSTONE);

		final List<String> mats = config.getStringList("saturated-materials");
		final EnumSet<Material> materials = EnumSet.noneOf(Material.class);

		for (String matName : mats)
		{
			final Material material = Material.matchMaterial(matName.replaceAll("-", "_").toUpperCase());
			if (material != null)
			{
				materials.add(material);
			}
		}

		if (!materials.isEmpty())
		{
			setSaturatedMats(materials);
		}

		for (EntityType entityType : ParticleType.keys())
		{
			ParticleType.Builder builder = ParticleType.getBuilder(entityType);
			final String name = builder.toString().toLowerCase(Locale.ENGLISH);

			builder.setWoolChance(Math.min(100, Math.max(0, config.getInt(name + ".wool-chance", builder.getWoolChance()))));
			builder.setBoneChance(Math.min(100, Math.max(0, config.getInt(name + ".bone-chance", builder.getBoneChance()))));
			builder.setParticleLifeFrom(Math.max(0, Math.min(1200, config.getInt(name + ".particle-life.from", builder.getParticleLifeFrom()))));
			builder.setParticleLifeTo(Math.max(builder.getParticleLifeFrom(), Math.min(1200, config.getInt(name + ".particle-life.to", builder.getParticleLifeTo()))));
			builder.setStainsFloor(config.getBoolean(name + ".stains-floor", builder.isStainsFloor()));
			builder.setBoneLife(Math.max(0, Math.min(1200, config.getInt(name + ".bone-life", builder.getBoneLife()))));
			builder.setStainLifeFrom(Math.max(0, Math.min(12000, config.getInt(name + ".stain-life.from", builder.getStainLifeFrom()))));
			builder.setStainLifeTo(Math.max(builder.getStainLifeFrom(), Math.min(12000, config.getInt(name + ".stain-life.to", builder.getStainLifeTo()))));
			builder.setAmountFrom(Math.max(0, Math.min(1000, config.getInt(name + ".amount.from", builder.getAmountFrom()))));
			builder.setAmountTo(Math.max(builder.getAmountFrom(), Math.min(1000, config.getInt(name + ".amount.to", builder.getAmountTo()))));

			String particleMatName = config.getString(name + ".particle-material");
			if (particleMatName != null)
			{
				final Material material = Material.matchMaterial(particleMatName.replaceAll("-", "_").toUpperCase());
				if (material != null && Util.isAllowedMaterial(material))
				{
					partMaterials.add(material);
				}
			}
			ParticleType.save(builder.build());
		}
		particleMaterials = partMaterials;
		worlds = new HashSet<>(config.getStringList("worlds"));
	}

	public final void saveConfig()
	{
		final FileConfiguration config = plugin.getConfig();
		config.options().header("Bleeding Mobs config\n"
								+ "Don't use tabs in this file\n"
								+ "You can always reset this to the defaults by removing the file.\n"
								+ "Chances are from 0 to 100, no fractions allowed. 100 means 100% chance of drop.\n"
								+ "There is no chance value for the particle material (e.g. red dye), \n"
								+ "because it's calculated from the wool and bone chances (so if you set them both to 0, it's 100%).\n"
								+ "All time values are in ticks = 1/20th of a second.\n"
								+ "If there are from and to values, then the value is randomly selected between from and to.\n"
								+ "Wool colors: white, orange, magenta, light-blue, yellow, lime, pink,\n"
								+ "gray, silver, cyan, purple, blue, brown, green, red, black\n"
								+ "When permission-only is set, then everything will only bleed, when they are hit \n"
								+ "by a player with bleedingmobs.bloodstrike permission. \n"
								+ "This will disable bleeding on death and on fall damage.\n");

		config.set("enabled", bleedingEnabled);
		config.set("max-particles", maxParticles);
		config.set("bleed-when-canceled", bleedWhenCanceled);
		config.set("permission-only", permissionOnly);
		config.set("attack-percentage", attackPercentage);
		config.set("fall-percentage", fallPercentage);
		config.set("death-percentage", deathPercentage);
		config.set("projectile-percentage", projectilePercentage);
		config.set("bloodstream.percentage", bloodstreamPercentage);
		config.set("bloodstream.time", bloodstreamTime);
		config.set("bloodstream.interval", bloodstreamInterval);

		final List<String> converted = new ArrayList<>();
		for (Material material : getSaturatedMats())
		{
			converted.add(material.toString().toLowerCase(Locale.ENGLISH).replaceAll("_", "-"));
		}
		config.set("saturated-materials", converted);

		for (EntityType entityTypeType : ParticleType.keys())
		{
			ParticleType particleType = ParticleType.get(entityTypeType);
			final String name = particleType.toString().toLowerCase(Locale.ENGLISH);
			config.set(name + ".wool-chance", particleType.getWoolChance());
			config.set(name + ".bone-chance", particleType.getBoneChance());
			config.set(name + ".particle-life.from", particleType.getParticleLifeFrom());
			config.set(name + ".particle-life.to", particleType.getParticleLifeTo());
			config.set(name + ".stains-floor", particleType.isStainingFloor());
			config.set(name + ".bone-life", particleType.getBoneLife());
			config.set(name + ".stain-life.from", particleType.getStainLifeFrom());
			config.set(name + ".stain-life.to", particleType.getStainLifeTo());
			config.set(name + ".amount.from", particleType.getAmountFrom());
			config.set(name + ".amount.to", particleType.getAmountTo());
			String particleMat = particleType.getParticleMaterial().toString().toLowerCase(Locale.ENGLISH).replaceAll("_", "-");
			config.set(name + ".particle-material", particleMat);
		}
		config.set("worlds", new ArrayList<>(worlds));
		plugin.saveConfig();
	}

	public boolean isWorldEnabled(final World world)
	{
		return worlds.isEmpty() || worlds.contains(world.getName());
	}

	public int getMaxParticles()
	{
		return maxParticles;
	}

	public boolean isBleedingWhenCanceled()
	{
		return bleedWhenCanceled;
	}

	public boolean isBleedingEnabled()
	{
		return bleedingEnabled;
	}

	public void setBleedingEnabled(final boolean set)
	{
		this.bleedingEnabled = set;
	}

	public Set<String> getWorlds()
	{
		return worlds;
	}

	public void setMaxParticles(final int maxParticles)
	{
		this.maxParticles = maxParticles;
	}

	public void setBleedWhenCanceled(final boolean set)
	{
		this.bleedWhenCanceled = set;
	}

	public boolean isPermissionOnly()
	{
		return permissionOnly;
	}

	public void setPermissionOnly(final boolean permissionOnly)
	{
		this.permissionOnly = permissionOnly;
	}

	public int getBloodstreamPercentage()
	{
		return bloodstreamPercentage;
	}

	public void setBloodstreamPercentage(final int bloodstreamPercentage)
	{
		this.bloodstreamPercentage = bloodstreamPercentage;
	}

	public int getBloodstreamTime()
	{
		return bloodstreamTime;
	}

	public void setBloodstreamTime(final int bloodstreamTime)
	{
		this.bloodstreamTime = bloodstreamTime;
	}

	public int getBloodstreamInterval()
	{
		return bloodstreamInterval;
	}

	public void setBloodstreamInterval(final int bloodstreamInterval)
	{
		this.bloodstreamInterval = bloodstreamInterval;
	}

	public int getAttackPercentage()
	{
		return attackPercentage;
	}

	public void setAttackPercentage(final int attackPercentage)
	{
		this.attackPercentage = attackPercentage;
	}

	public int getFallPercentage()
	{
		return fallPercentage;
	}

	public EnumSet<Material> getSaturatedMats()
	{
		return saturatedMats;
	}

	public void setSaturatedMats(final EnumSet<Material> saturatedMats)
	{
		this.saturatedMats = saturatedMats;
	}

	public void setFallPercentage(final int fallPercentage)
	{
		this.fallPercentage = fallPercentage;
	}

	public int getDeathPercentage()
	{
		return deathPercentage;
	}

	public void setDeathPercentage(final int deathPercentage)
	{
		this.deathPercentage = deathPercentage;
	}

	public int getProjectilePercentage()
	{
		return projectilePercentage;
	}

	public void setProjectilePercentage(final int projectilePercentage)
	{
		this.projectilePercentage = projectilePercentage;
	}

	public Set<Material> getParticleMaterials()
	{
		return particleMaterials;
	}
}
