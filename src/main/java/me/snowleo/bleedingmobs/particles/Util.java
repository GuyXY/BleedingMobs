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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Material;


public final class Util
{
	private static final Random RANDOM = new Random();
	private static final Map<UUID, Integer> COUNTER = new HashMap<UUID, Integer>();
	public static final int COUNTER_MIN = 2767;
	public static final int COUNTER_MAX = 32767;
	public static final int COUNTER_SIZE = COUNTER_MAX - COUNTER_MIN;

	private Util()
	{
	}

	public static int getRandomBetween(final int from, final int to)
	{
		int span = to - from;
		return (span > 0 ? RANDOM.nextInt(span) : 0) + from;
	}

	public static boolean isAllowedMaterial(final Material mat)
	{
		if (mat.getMaxDurability() != 0)
			return false;

		switch (mat)
		{
			case PUMPKIN:
			case PLAYER_HEAD:
			case DRAGON_HEAD:
			case PISTON_HEAD:
			case ZOMBIE_HEAD:
			case CREEPER_HEAD:
			case DRAGON_WALL_HEAD:
			case PLAYER_WALL_HEAD:
			case ZOMBIE_WALL_HEAD:
			case CREEPER_WALL_HEAD:
				return false;
			default:
				return true;
		}
	}

	public static int getCounter(final UUID worldId)
	{
		synchronized (COUNTER)
		{
			Integer c = COUNTER.get(worldId);
			int r = c == null ? COUNTER_MIN : (c >= COUNTER_MAX ? COUNTER_MIN : c + 1);
			COUNTER.put(worldId, Integer.valueOf(r));
			return r;
		}
	}
}
