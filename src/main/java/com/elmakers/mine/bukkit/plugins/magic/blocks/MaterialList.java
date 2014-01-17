package com.elmakers.mine.bukkit.plugins.magic.blocks;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Implements a hashset of Materials for quick add/lookup
 * 
 * Uses Material to differentiate between data variants.
 * 
 * @author NathanWolf
 * 
 */

public class MaterialList extends HashSet<Material>
{
	/**
	 * 
	 */
	private static final long         serialVersionUID = 1L;

	protected String                  id               = null;

	public MaterialList()
	{

	}

	public MaterialList(Collection<Material> copy)
	{
		this.addAll(copy);
	}

	public MaterialList(String id)
	{
		this.id = id;
	}

	public void add(Block block)
	{
		add(block.getType());
	}

	public String getId()
	{
		return id;
	}
}