/*
 * Copyright (C) 2015-2021 Daniel Saukel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.erethon.itemsxl.item;

import de.erethon.commons.compatibility.Version;
import de.erethon.itemsxl.ItemsXL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
public class ItemBoxListener implements Listener {

    private ItemsXL plugin;

    public ItemBoxListener(ItemsXL plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        ItemStack itemStack;
        if (Version.isAtLeast(Version.MC1_9)) {
            itemStack = event.getPlayer().getInventory().getItemInMainHand();
        } else {
            itemStack = event.getPlayer().getInventory().getItemInHand();
        }
        ItemBox box = ItemBox.getByItemStack(plugin, itemStack);

        if (box != null) {
            box.open(event.getPlayer());
            event.setCancelled(true);
        }
    }

}
