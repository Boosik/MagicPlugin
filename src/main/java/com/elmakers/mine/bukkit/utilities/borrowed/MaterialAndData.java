package com.elmakers.mine.bukkit.utilities.borrowed;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.elmakers.mine.bukkit.utilities.InventoryUtils;

public class MaterialAndData {
	protected Material material;
	protected byte data;
	String[] signLines = null;
	String commandLine = null;
	Inventory inventory = null;

	public MaterialAndData() {
		material = Material.AIR;
		data = 0;
	}
	
	public MaterialAndData(MaterialAndData other) {
		material = other.material;
		data = other.data;
		commandLine = other.commandLine;
		inventory = other.inventory;
		signLines = other.signLines;
	}
	
	public MaterialAndData(final Material material) {
		this.material = material;
		this.data = 0;
	}
	
	public MaterialAndData(final Material material, final  byte data) {
		this.material = material;
		this.data = data;
	}
	
	public void setMaterial(Material material, byte data) {
		this.material = material;
		this.data = data;
		signLines = null;
		commandLine = null;
		inventory = null;
	}
	
	public void setMaterial(Material material) {
		setMaterial(material, (byte)0);
	}
	
	@SuppressWarnings("deprecation")
	public void updateFrom(Block block) {
		// Look for special block states
		signLines = null;
		commandLine = null;
		inventory = null;
		
		BlockState blockState = block.getState();
		if (blockState instanceof Sign) {
			Sign sign = (Sign)blockState;
			signLines = sign.getLines();
		} else if (blockState instanceof CommandBlock){
			CommandBlock command = (CommandBlock)blockState;
			commandLine = command.getCommand();
		} else if (blockState instanceof InventoryHolder) {
			InventoryHolder holder = (InventoryHolder)blockState;
			Inventory holderInventory = holder.getInventory();
			inventory = InventoryUtils.createInventory(holder, holderInventory.getSize(), holderInventory.getName());
			ItemStack[] items = holderInventory.getContents();
			for (int i = 0; i < items.length; i++) {
				ItemStack item = items[i];
				if (item != null) {
					inventory.setItem(i, item);
				}
			}
		}
		
		material = block.getType();
		data = block.getData();
	}
	
	@SuppressWarnings("deprecation")
	public void modify(Block block) {
		// Clear chests so they don't dump their contents.
		BlockState oldState = block.getState();
		if (oldState instanceof InventoryHolder) {
			InventoryHolder holder = (InventoryHolder)oldState;
			Inventory inventory = holder.getInventory();
			inventory.clear();
		}
		
		block.setType(material);
		block.setData(data);
		BlockState blockState = block.getState();
		if (blockState instanceof Sign && signLines != null) {
			Sign sign = (Sign)blockState;
			for (int i = 0; i < signLines.length; i++) {
				sign.setLine(i, signLines[i]);
			}
			sign.update();
		} else if (blockState instanceof CommandBlock && commandLine != null) {
			CommandBlock command = (CommandBlock)blockState;
			command.setCommand(commandLine);
			command.update();
		} else if (blockState instanceof InventoryHolder && inventory != null) {
			InventoryHolder holder = (InventoryHolder)blockState;
			Inventory newInventory = holder.getInventory();
			int maxSize = Math.min(newInventory.getSize(), inventory.getSize());
			for (int i = 0; i < maxSize; i++) {
				ItemStack item = inventory.getItem(i);
				item = InventoryUtils.getCopy(item);
				if (item != null) {
					newInventory.setItem(i, item);
				}
			}
		}
	}
	
	public byte getData() {
		return data;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public String getKey() {
		String materialKey = material.name().toLowerCase();
		if (data != 0) {
			materialKey += ":" + data;
		}
		
		return materialKey;
	}

	@SuppressWarnings("deprecation")
	public boolean isDifferent(Block block) {
		Material blockMaterial = block.getType();
		byte blockData = block.getData();
		if (blockMaterial != material || blockData != data) {
			return true;
		}
		
		// Special cases
		BlockState blockState = block.getState();
		if (blockState instanceof Sign && signLines != null) {
			Sign sign = (Sign)blockState;
			String[] currentLines = sign.getLines();
			for (int i = 0; i < signLines.length; i++) {
				if (!currentLines[i].equals(signLines[i])) {
					return true;
				}
			}
		} else if (blockState instanceof CommandBlock && commandLine != null) {
			CommandBlock command = (CommandBlock)blockState;
			if (!command.getCommand().equals(commandLine)) {
				return true;
			}
		} else if (blockState instanceof InventoryHolder && inventory != null) {
			// Just copy it over.... not going to compare inventories :P
			return true;
		}
		
		return false;
	}
}
