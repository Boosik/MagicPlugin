package com.elmakers.mine.bukkit.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public abstract class EffectPlayer implements Runnable {
	
	protected final Plugin plugin;
	protected Effect effect = null;
	protected int period = 1;
	protected int data = 0;
	protected FireworkEffect fireworkEffect = null;
	protected int power = 0;
	protected String effectName = null;
	protected float xOffset = 0;
	protected float yOffset = 0;
	protected float zOffset = 0;
	protected float effectSpeed = 20f;
	protected int particleCount = 10;
	
	public EffectPlayer(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void setEffect(Effect effect) {
		this.effect = effect;
	}
	
	public void setFireworkEffect(FireworkEffect fireworkEffect, int power) {
		this.fireworkEffect = fireworkEffect;
		this.power = power;
	}
	
	public void setEffectName(String effectName) {
		this.effectName = effectName;
	}
	
	public void start() {
		schedule();
	}
	
	protected void schedule() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, period);
	}
	
	public void setPeriod(int period) {
		this.period = period;
	}
	
	public void setData(int data) {
		this.data = data;
	}
	
	protected void playEffect(Location location) {
		if (effect != null) {
			location.getWorld().playEffect(location, effect, data);
		}
		if (fireworkEffect != null) {
			EffectUtils.spawnFireworkEffect(location, fireworkEffect, power);
		}
		if (effectName != null) {
			EffectUtils.playEffect(location, effectName, xOffset, yOffset, zOffset, effectSpeed, particleCount);
		}
	}
	
	public abstract void setSpeed(float speed);

	public float getEffectSpeed() {
		return effectSpeed;
	}

	public void setEffectSpeed(float effectSpeed) {
		this.effectSpeed = effectSpeed;
	}

	public int getParticleCount() {
		return particleCount;
	}

	public void setParticleCount(int particleCount) {
		this.particleCount = particleCount;
	}
}