package com.hbm.inventory.container;

import com.hbm.items.weapon.sedna.ItemGunBaseNT;
import com.hbm.items.weapon.sedna.mods.WeaponModManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerWeaponTable extends Container {
	
	public InventoryBasic mods = new InventoryBasic("Mods", false, 7);
	public IInventory gun = new InventoryCraftResult();

	public ContainerWeaponTable(InventoryPlayer inventory) {
		
		for(int i = 0; i < 7; i++) this.addSlotToContainer(new ModSlot(mods, i, 44 + 18 * i, 108));

		this.addSlotToContainer(new Slot(gun, 0, 8, 108) {

			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof ItemGunBaseNT;
			}

			@Override
			public void putStack(ItemStack stack) {
				
				if(stack != null) {
					ItemStack[] mods = WeaponModManager.getUpgradeItems(stack);
					
					if(mods != null) for(int i = 0; i < Math.min(mods.length, 7); i++) {
						ContainerWeaponTable.this.mods.setInventorySlotContents(i, mods[i]);
					}
				}
				
				super.putStack(stack);
			}

			@Override
			public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
				super.onPickupFromSlot(player, stack);
				
				WeaponModManager.install(
						stack,
						mods.getStackInSlot(0),
						mods.getStackInSlot(1),
						mods.getStackInSlot(2),
						mods.getStackInSlot(3),
						mods.getStackInSlot(4),
						mods.getStackInSlot(5),
						mods.getStackInSlot(6));
				
				for(int i = 0; i < 7; i++) {
					ItemStack mod = ContainerWeaponTable.this.mods.getStackInSlot(i);
					if(WeaponModManager.isApplicable(stack, mod, false)) ContainerWeaponTable.this.mods.setInventorySlotContents(i, null);
				}
			}
		});
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 158 + i * 18));
			}
		}

		for(int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 216));
		}
		
		this.onCraftMatrixChanged(this.mods);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);

		if(!player.worldObj.isRemote) {
			for(int i = 0; i < this.mods.getSizeInventory(); ++i) {
				ItemStack itemstack = this.mods.getStackInSlotOnClosing(i);

				if(itemstack != null) {
					player.dropPlayerItemWithRandomChoice(itemstack, false);
				}
			}
			
			ItemStack itemstack = this.gun.getStackInSlotOnClosing(0);
			
			if(itemstack != null) {
				WeaponModManager.uninstall(itemstack);
				player.dropPlayerItemWithRandomChoice(itemstack, false);
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int par2) {
		return null;
	}
	
	public class ModSlot extends Slot {

		public ModSlot(IInventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return gun.getStackInSlot(0) != null && WeaponModManager.isApplicable(gun.getStackInSlot(0), stack, true);
		}
		
		@Override
		public void putStack(ItemStack stack) {
			super.putStack(stack);
			refreshInstalledMods();
		}

		@Override
		public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
			super.onPickupFromSlot(player, stack);
			refreshInstalledMods();
		}
		
		public void refreshInstalledMods() {
			if(gun.getStackInSlot(0) == null) return;
			WeaponModManager.uninstall(gun.getStackInSlot(0));
			WeaponModManager.install(
					gun.getStackInSlot(0),
					mods.getStackInSlot(0),
					mods.getStackInSlot(1),
					mods.getStackInSlot(2),
					mods.getStackInSlot(3),
					mods.getStackInSlot(4),
					mods.getStackInSlot(5),
					mods.getStackInSlot(6)); //miscalculated, slot array isn't visible - fuck!
		}
	}
}
