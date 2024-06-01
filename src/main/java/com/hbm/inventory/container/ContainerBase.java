package com.hbm.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**For now, only used for stuff with filters and crates as a reference implementation,
 * because I really needed to get the te from a container
 * But you should very much use this to kill the giant amount of boilerplate in container classes
 * @author 70k **/
public class ContainerBase extends Container {

    protected IInventory te;

    public ContainerBase (InventoryPlayer invPlayer, IInventory tedf){
        te = tedf;
    }
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return te.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int par2) {
        ItemStack var3 = null;
        Slot var4 = (Slot) this.inventorySlots.get(par2);

        if(var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if(par2 <= te.getSizeInventory() - 1) {
                if(!this.mergeItemStack(var5, te.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if(!this.mergeItemStack(var5, 0, te.getSizeInventory(), false)) {
                return null;
            }

            if(var5.stackSize == 0) {
                var4.putStack(null);
            } else {
                var4.onSlotChanged();
            }

            var4.onPickupFromSlot(p_82846_1_, var5);
        }

        return var3;
    }

    public void playerInv(InventoryPlayer invPlayer, int playerInvX, int playerInvY, int playerHotbarY){
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, playerInvX + j * 18, playerInvY + i * 18));
            }
        }

        for(int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(invPlayer, i, playerInvX + i * 18, playerHotbarY));
        }
    }

}
