/*
 * Copyright (C) 2015-2017 Daniel Saukel
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
package io.github.dre2n.itemsxl.command;

import io.github.dre2n.caliburn.CaliburnAPI;
import io.github.dre2n.caliburn.item.UniversalItem;
import io.github.dre2n.commons.chat.MessageUtil;
import io.github.dre2n.commons.command.DRECommand;
import io.github.dre2n.commons.misc.NumberUtil;
import io.github.dre2n.itemsxl.config.IMessage;
import io.github.dre2n.itemsxl.item.ItemBox;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Daniel Saukel
 */
public class GiveCommand extends DRECommand {

    CaliburnAPI api = CaliburnAPI.getInstance();

    public GiveCommand() {
        setCommand("give");
        setMinArgs(2);
        setMaxArgs(4);
        setHelp(IMessage.COMMAND_HELP_GIVE.getMessage());
        setPermission("ixl.give");
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        Player player;
        if (Bukkit.getPlayer(args[1]) != null) {
            player = Bukkit.getPlayer(args[1]);

        } else if (sender instanceof Player) {
            player = (Player) sender;

        } else {
            MessageUtil.sendMessage(sender, IMessage.ERROR_NO_OBJECT.getMessage(IMessage.OBJECT_PLAYER.getMessage()));
            return;
        }

        UniversalItem item = api.getItems().getById(args[2]);

        int amount = 1;
        if (args.length >= 4) {
            amount = NumberUtil.parseInt(args[3], 1);
        }

        if (args.length == 5) {
            player.getInventory().addItem(new ItemBox(item).toItemStack(amount));
            return;
        }

        if (item != null) {
            player.getInventory().addItem(item.toItemStack(amount));

        } else {
            MessageUtil.sendMessage(player, IMessage.ERROR_NO_OBJECT.getMessage(IMessage.OBJECT_ITEM.getMessage()));
        }

    }

}
