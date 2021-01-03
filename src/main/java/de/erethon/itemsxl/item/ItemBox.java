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

import de.erethon.caliburn.CaliburnAPI;
import de.erethon.caliburn.item.ExItem;
import de.erethon.caliburn.item.VanillaItem;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.compatibility.Version;
import de.erethon.itemsxl.ItemsXL;
import de.erethon.itemsxl.config.IConfig;
import de.erethon.itemsxl.config.IMessage;
import java.util.Arrays;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author Daniel Saukel
 */
public class ItemBox {

    private ItemsXL plugin;
    private CaliburnAPI api;
    private IConfig config;

    private ExItem item;

    public ItemBox(ItemsXL plugin, ExItem item) {
        this.plugin = plugin;
        api = plugin.getAPI();
        config = plugin.getIConfig();

        this.item = item;
    }

    /**
     * @return the item
     */
    public ExItem getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(ExItem item) {
        this.item = item;
    }

    /**
     * @return the box
     */
    public ItemStack toItemStack(int amount) {
        ItemStack itemStack = VanillaItem.PLAYER_HEAD.toItemStack(amount);
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
        ItemStack itemStack;
        if (Version.isAtLeast(Version.MC1_9)) {
            itemStack = player.getInventory().getItemInMainHand();
        } else {
            itemStack = player.getInventory().getItemInHand();
        }
        String name = itemStack.getItemMeta().getDisplayName();

        if (name.equals(config.getBoxName())) {
            ExItem item = api.getExItem(itemStack);
            player.getInventory().remove(itemStack);
            player.getInventory().addItem(item.toItemStack(itemStack.getAmount()));

            MessageUtil.sendPluginTag(player, plugin);
            MessageUtil.sendCenteredMessage(player, IMessage.COMMAND_OPEN_SUCCESS.getMessage(item.getName()));
            return true;

        } else {
            return false;
        }
    }

    /* Statics */
    public static ItemBox getByItemStack(ItemsXL plugin, ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {

            if (plugin.getIConfig().getBoxName().equals(itemStack.getItemMeta().getDisplayName())) {
                return new ItemBox(plugin, plugin.getAPI().getExItem(itemStack));

            } else {
                return null;
            }

        } else {
            return null;
        }
    }

}
