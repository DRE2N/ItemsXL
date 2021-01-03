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
package de.erethon.itemsxl.command;

import de.erethon.caliburn.item.VanillaItem;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.erethon.commons.compatibility.Version;
import de.erethon.itemsxl.ItemsXL;
import de.erethon.itemsxl.config.IMessage;
import de.erethon.itemsxl.item.ItemBox;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
public class OpenCommand extends DRECommand {

    private ItemsXL plugin;

    public OpenCommand(ItemsXL plugin) {
        this.plugin = plugin;
        setCommand("open");
        setMinArgs(0);
        setMaxArgs(0);
        setHelp(IMessage.HELP_OPEN.getMessage());
        setPlayerCommand(true);
        setConsoleCommand(false);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        Player player = (Player) sender;

        ItemStack itemStack;
        if (Version.isAtLeast(Version.MC1_9)) {
            itemStack = player.getInventory().getItemInMainHand();
        } else {
            itemStack = player.getInventory().getItemInHand();
        }

        if (itemStack.getType() != VanillaItem.PLAYER_HEAD.getMaterial()) {
            MessageUtil.sendMessage(sender, IMessage.ERROR_NO_ITEM_BOX.getMessage());
            return;
        }

        ItemBox box = ItemBox.getByItemStack(plugin, itemStack);

        if (box != null) {
            box.open(player);

        } else {
            MessageUtil.sendMessage(sender, IMessage.ERROR_NO_ITEM_BOX.getMessage());
        }
    }

}
