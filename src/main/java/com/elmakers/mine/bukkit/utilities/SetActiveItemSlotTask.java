package com.elmakers.mine.bukkit.utilities;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SetActiveItemSlotTask extends BukkitRunnable {
	private final Player player;
	private final int itemSlot;

	public SetActiveItemSlotTask(Player player, int slot) {
		this.player = player;
		this.itemSlot = slot;
	}

	public void run() {
		player.getInventory().setHeldItemSlot(itemSlot);
	}
}