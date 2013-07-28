package com.hawkfalcon.deathswap.utilities;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryData {

    private final ItemStack[] items;
    private final ItemStack[] armor;

    public InventoryData(PlayerInventory inv) {
        items = inv.getContents();
        armor = inv.getArmorContents();
    }

    public ItemStack[] getContents() {
        return items;
    }

    public ItemStack[] getArmorContents() {
        return armor;
    }

}
