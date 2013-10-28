package net.minecraft.src;

import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
/*
public class MMM_SlotArmor extends SlotArmor {

	protected final Container parent;
	final int armorType;


	MMM_SlotArmor(Container par1Container, IInventory par2IInventory,
			int par3, int par4, int par5, int par6) {
		super(null, par2IInventory, par3, par4, par5, par6);
		this.parent = par1Container;
		this.armorType = par6;
	}

	public boolean isItemValid(ItemStack par1ItemStack) {
		if (par1ItemStack == null) return false;
		Item litem = par1ItemStack.getItem();
		// ���[
		if (MMM_Helper.isForge) {
			try {
				Item.class.getMethod("isValidArmor", ItemStack.class, int.class).invoke(litem, par1ItemStack, armorType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (litem instanceof ItemArmor) {
			return ((ItemArmor)litem).armorType == armorType;
		}
		if (litem.itemID == Block.pumpkin.blockID || litem.itemID == Item.skull.itemID) {
			return armorType == 0;
		}
		return false;
	}

}
*/
