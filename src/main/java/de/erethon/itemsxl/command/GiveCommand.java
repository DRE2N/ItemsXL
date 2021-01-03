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

import de.erethon.caliburn.CaliburnAPI;
import de.erethon.caliburn.item.ExItem;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.erethon.commons.misc.NumberUtil;
import de.erethon.itemsxl.ItemsXL;
import de.erethon.itemsxl.config.IMessage;
import de.erethon.itemsxl.item.ItemBox;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Daniel Saukel
 */
public class GiveCommand extends DRECommand {

    private ItemsXL plugin;
    private CaliburnAPI api;

    public GiveCommand(ItemsXL plugin) {
        this.plugin = plugin;
        api = plugin.getAPI();
        setCommand("give");
        setMinArgs(1);
        setMaxArgs(4);
        setHelp(IMessage.HELP_GIVE.getMessage());
        setPermission("ixl.give");
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        int i = 2;
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null && sender instanceof Player) {
            player = (Player) sender;
            i = 1;
        }
        if (player == null) {
            MessageUtil.sendMessage(sender, IMessage.ERROR_NO_OBJECT.getMessage(IMessage.OBJECT_PLAYER.getMessage()));
            return;
        }

        ExItem item = api.getExItem(args[i]);
        if (item == null) {
            MessageUtil.sendMessage(player, IMessage.ERROR_NO_OBJECT.getMessage(IMessage.OBJECT_ITEM.getMessage()));
            return;
        }

        int amount = 1;
        if (args.length >= i + 2) {
            amount = NumberUtil.parseInt(args[i + 1], 1);
        }

        if (args.length == i + 3) {
            player.getInventory().addItem(new ItemBox(plugin, item).toItemStack(amount));
            return;
        }

        player.getInventory().addItem(item.toItemStack(amount));
        MessageUtil.sendMessage(sender, IMessage.COMMAND_GIVE_SUCCESS.getMessage(String.valueOf(amount), item.getName(), player.getName()));
    }

}
