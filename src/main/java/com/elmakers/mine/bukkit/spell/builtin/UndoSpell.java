package com.elmakers.mine.bukkit.spell.builtin;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.elmakers.mine.bukkit.api.block.BlockBatch;
import com.elmakers.mine.bukkit.api.block.UndoList;
import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.spell.SpellResult;
import com.elmakers.mine.bukkit.block.batch.SpellBatch;
import com.elmakers.mine.bukkit.spell.TargetingSpell;
import com.elmakers.mine.bukkit.utility.Target;

public class UndoSpell extends TargetingSpell
{
	private String undoListName;
	
	@Override
	public SpellResult onCast(ConfigurationSection parameters) 
	{
		Target target = getTarget();
		Player player = getPlayer();
		if (target.hasEntity() && target.getEntity() instanceof Player)
		{
			// Don't let just anyone rewind someone else's thing
			if (player != null && target.getEntity() != player && !mage.isSuperPowered()) {
				return SpellResult.NO_TARGET;
			}
			
			Mage mage = controller.getMage((Player)target.getEntity());
			UndoList undoList = mage.undo();
			if (undoList != null) {
				undoListName = undoList.getName();
			}
			return undoList != null ? SpellResult.CAST : SpellResult.FAIL;
		}
		
		Block targetBlock = isLookingDown() ? getLocation().getBlock() : target.getBlock();
		if (targetBlock != null)
		{
			boolean targetAll = mage.isSuperPowered();
			if (targetAll)
			{
				UndoList undid = controller.undoAny(targetBlock);
				if (undid != null) 
				{
					Mage targetMage = undid.getOwner();
					undoListName = undid.getName();
					setTargetName(targetMage.getName());
					return SpellResult.CAST;
				}
			}
			else
			{
				setTargetName(mage.getName());
				BlockBatch batch = mage.cancelPending();
				if (batch != null) {
					undoListName = (batch instanceof SpellBatch) ? ((SpellBatch)batch).getSpell().getName() : null;
					return SpellResult.COST_FREE;
				}
				UndoList undoList = mage.undo(targetBlock);
				undoListName = undoList.getName();
				return SpellResult.CAST;
			}
		}
		
		return SpellResult.NO_TARGET;	
	}
	
	@Override
	public String getMessage(String messageKey, String def) {
		String message = super.getMessage(messageKey, def);
		return message.replace("$spell", undoListName == null ? "Unknown" : undoListName);
	}
}