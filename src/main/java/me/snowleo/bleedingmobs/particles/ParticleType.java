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
package me.snowleo.bleedingmobs.particles;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;


public final class ParticleType
{
	private static final Map<EntityType, ParticleType> MAP = Collections.synchronizedMap(new EnumMap<>(EntityType.class));

	static
	{
		for (EntityType entityType : EntityType.values())
		{
			if (entityType == EntityType.CHICKEN)
			{
				MAP.put(entityType, new ParticleType.Builder(entityType).setWoolChance(20).setBoneChance(0).setAmountFrom(5).setAmountTo(15).setParticleMaterial(Material.FEATHER).build());
			}
			else if (entityType == EntityType.WITHER || entityType == EntityType.WITHER_SKELETON)
			{
				MAP.put(entityType, new ParticleType.Builder(entityType).setWoolChance(20).setBoneChance(0).setAmountFrom(5).setAmountTo(15).setParticleMaterial(Material.COAL).build());
			}
			else if (entityType == EntityType.SKELETON || entityType == EntityType.STRAY || entityType == EntityType.SKELETON_HORSE)
			{
				MAP.put(entityType, new ParticleType.Builder(entityType).setWoolChance(0).setBoneChance(0).setAmountFrom(5).setAmountTo(15).setParticleMaterial(Material.BONE).build());
			}
			else if (entityType == EntityType.SLIME)
			{
				MAP.put(entityType, new ParticleType.Builder(entityType).setWoolChance(0).setBoneChance(0).setAmountFrom(5).setAmountTo(15).setParticleMaterial(Material.SLIME_BALL).build());
			}
			else if (entityType == EntityType.BLAZE)
			{
				MAP.put(entityType, new ParticleType.Builder(entityType).setWoolChance(0).setBoneChance(0).setParticleMaterial(Material.BLAZE_ROD).build());
			}
			else if (entityType == EntityType.IRON_GOLEM)
			{
				MAP.put(entityType, new ParticleType.Builder(entityType).setBoneChance(0).setParticleMaterial(Material.IRON_INGOT).build());
			}
			else if (entityType == EntityType.SNOWMAN)
			{
				MAP.put(entityType, new ParticleType.Builder(entityType).setBoneChance(0).setParticleMaterial(Material.SNOW).build());
			}
			else if (entityType == EntityType.MUSHROOM_COW)
			{
				MAP.put(entityType, new ParticleType.Builder(entityType).setParticleMaterial(Material.RED_MUSHROOM).build());
			}
			else if (entityType.isAlive() && entityType != EntityType.ARMOR_STAND)
			{
				MAP.put(entityType, new ParticleType.Builder(entityType).build());
			}
		}
	}

	public static Set<EntityType> keys()
	{
		synchronized (MAP)
		{
			return EnumSet.copyOf(MAP.keySet());
		}
	}

	public static ParticleType get(final EntityType entityType)
	{
		return MAP.get(entityType);
	}

	public static void save(final ParticleType type)
	{
		MAP.put(type.getEntityType(), type);
	}

	public static Builder getBuilder(final EntityType type)
	{
		ParticleType p = get(type);
		if (p == null)
		{
			throw new IllegalStateException();
		}
		ParticleType.Builder builder = new ParticleType.Builder(p.getEntityType());
		builder.setAmountFrom(p.getAmountFrom());
		builder.setAmountTo(p.getAmountTo());
		builder.setBoneChance(p.getBoneChance());
		builder.setBoneLife(p.getBoneLife());
		builder.setParticleLifeFrom(p.getParticleLifeFrom());
		builder.setParticleLifeTo(p.getParticleLifeTo());
		builder.setParticleMaterial(p.getParticleMaterial());
		builder.setStainLifeFrom(p.getStainLifeFrom());
		builder.setStainLifeTo(p.getStainLifeTo());
		builder.setStainsFloor(p.isStainingFloor());
		builder.setWoolChance(p.getWoolChance());
		return builder;
	}
	private final EntityType entityType;
	private final String entityName;
	private final int woolChance;
	private final int boneChance;
	private final int particleLifeFrom;
	private final int particleLifeTo;
	private final boolean stainsFloor;
	private final int boneLife;
	private final int stainLifeFrom;
	private final int stainLifeTo;
	private final int amountFrom;
	private final int amountTo;
	private final Material particleMaterial;

	private ParticleType(final Builder builder)
	{
		this.entityType = builder.getEntityType();
		this.entityName = builder.getEntityName();
		this.woolChance = builder.getWoolChance();
		this.boneChance = builder.getBoneChance();
		this.particleLifeFrom = builder.getParticleLifeFrom();
		this.particleLifeTo = builder.getParticleLifeTo();
		this.stainsFloor = builder.isStainsFloor();
		this.boneLife = builder.getBoneLife();
		this.stainLifeFrom = builder.getStainLifeFrom();
		this.stainLifeTo = builder.getStainLifeTo();
		this.amountFrom = builder.getAmountFrom();
		this.amountTo = builder.getAmountTo();
		this.particleMaterial = builder.getParticleMaterial();
	}

	public int getWoolChance()
	{
		return woolChance;
	}

	public int getBoneChance()
	{
		return boneChance;
	}

	public int getParticleLifeFrom()
	{
		return particleLifeFrom;
	}

	public int getParticleLifeTo()
	{
		return particleLifeTo;
	}

	public boolean isStainingFloor()
	{
		return stainsFloor;
	}

	public int getBoneLife()
	{
		return boneLife;
	}

	public int getStainLifeFrom()
	{
		return stainLifeFrom;
	}

	public int getStainLifeTo()
	{
		return stainLifeTo;
	}

	public int getAmountFrom()
	{
		return amountFrom;
	}

	public int getAmountTo()
	{
		return amountTo;
	}

	public Material getParticleMaterial()
	{
		return particleMaterial;
	}

	public EntityType getEntityType()
	{
		return entityType;
	}

	@Override
	public String toString()
	{
		return entityName;
	}

	public boolean isMagicMaterial()
	{
		return this.getParticleMaterial() == Material.CAKE;
	}

	public static class Builder
	{
		private final EntityType entityType;
		private final String entityName;
		private int woolChance = 50;
		private int boneChance = 50;
		private int particleLifeFrom = 5;
		private int particleLifeTo = 15;
		private boolean stainsFloor = true;
		private int boneLife = 100;
		private int stainLifeFrom = 80;
		private int stainLifeTo = 120;
		private int amountFrom = 15;
		private int amountTo = 25;
		private Material particleMaterial = Material.RED_DYE;

		public Builder(final EntityType entityType)
		{
			this.entityType = entityType;
			this.entityName = entityType.toString().replaceAll("_", "");
		}

		public EntityType getEntityType()
		{
			return entityType;
		}

		public String getEntityName()
		{
			return entityName;
		}

		public int getWoolChance()
		{
			return woolChance;
		}

		public Builder setWoolChance(final int woolChance)
		{
			this.woolChance = woolChance;
			return this;
		}

		public int getBoneChance()
		{
			return boneChance;
		}

		public Builder setBoneChance(final int boneChance)
		{
			this.boneChance = boneChance;
			return this;
		}

		public int getParticleLifeFrom()
		{
			return particleLifeFrom;
		}

		public Builder setParticleLifeFrom(final int particleLifeFrom)
		{
			this.particleLifeFrom = particleLifeFrom;
			return this;
		}

		public int getParticleLifeTo()
		{
			return particleLifeTo;
		}

		public Builder setParticleLifeTo(final int particleLifeTo)
		{
			this.particleLifeTo = particleLifeTo;
			return this;
		}

		public boolean isStainsFloor()
		{
			return stainsFloor;
		}

		public Builder setStainsFloor(final boolean stainsFloor)
		{
			this.stainsFloor = stainsFloor;
			return this;
		}

		public int getBoneLife()
		{
			return boneLife;
		}

		public Builder setBoneLife(final int boneLife)
		{
			this.boneLife = boneLife;
			return this;
		}

		public int getStainLifeFrom()
		{
			return stainLifeFrom;
		}

		public Builder setStainLifeFrom(final int stainLifeFrom)
		{
			this.stainLifeFrom = stainLifeFrom;
			return this;
		}

		public int getStainLifeTo()
		{
			return stainLifeTo;
		}

		public Builder setStainLifeTo(final int stainLifeTo)
		{
			this.stainLifeTo = stainLifeTo;
			return this;
		}

		public int getAmountFrom()
		{
			return amountFrom;
		}

		public Builder setAmountFrom(final int amountFrom)
		{
			this.amountFrom = amountFrom;
			return this;
		}

		public int getAmountTo()
		{
			return amountTo;
		}

		public Builder setAmountTo(final int amountTo)
		{
			this.amountTo = amountTo;
			return this;
		}

		public Material getParticleMaterial()
		{
			return particleMaterial;
		}

		public Builder setParticleMaterial(final Material particleMaterial)
		{
			this.particleMaterial = particleMaterial;
			return this;
		}

		@Override
		public String toString()
		{
			return entityName;
		}

		public ParticleType build()
		{
			return new ParticleType(this);
		}
	}
}
