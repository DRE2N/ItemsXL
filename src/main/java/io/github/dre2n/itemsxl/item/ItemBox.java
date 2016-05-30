/*
 * Copyright (C) 2015-2016 Daniel Saukel
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
package io.github.dre2n.itemsxl.item;

import io.github.dre2n.caliburn.item.Items;
import io.github.dre2n.caliburn.item.UniversalItem;
import io.github.dre2n.commons.util.messageutil.MessageUtil;
import io.github.dre2n.itemsxl.ItemsXL;
import io.github.dre2n.itemsxl.config.IConfig;
import io.github.dre2n.itemsxl.config.IMessages;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author Daniel Saukel
 */
public class ItemBox {

    static ItemsXL plugin = ItemsXL.getInstance();
    static IConfig config = plugin.getIConfig();
    static Items items = plugin.getAPI().getItems();

    private UniversalItem item;

    public ItemBox(UniversalItem item) {
        this.item = item;
    }

    /**
     * @return the item
     */
    public UniversalItem getItem() {
        return item;
    }

    /**
     * @param item
     * the item to set
     */
    public void setItem(UniversalItem item) {
        this.item = item;
    }

    /**
     * @return the box
     */
    public ItemStack toItemStack(int amount) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setDisplayName(config.getBoxName());
        meta.setOwner("MHF_Chest");
        meta.setLore(Arrays.asList(item.getIdLore()));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * @return the item in the box
     */
    public boolean open(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        String name = itemStack.getItemMeta().getDisplayName();

        if (name.equals(config.getBoxName())) {
            UniversalItem item = items.getById(items.getCustomItemId(itemStack));
            player.getInventory().remove(itemStack);
            player.getInventory().addItem(item.toItemStack(itemStack.getAmount()));

            MessageUtil.sendPluginTag(player, plugin);
            MessageUtil.sendCenteredMessage(player, IMessages.COMMAND_OPEN_SUCCESS.getMessage(item.getName()));
            return true;

        } else {
            return false;
        }
    }

    /* Statics */
    public static ItemBox getByItemStack(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {

            if (itemStack.getItemMeta().getDisplayName().equals(config.getBoxName())) {
                return new ItemBox(items.getById(items.getCustomItemId(itemStack)));

            } else {
                return null;
            }

        } else {
            return null;
        }
    }

}
