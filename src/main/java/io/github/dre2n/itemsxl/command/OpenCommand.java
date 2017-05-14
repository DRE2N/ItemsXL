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

import io.github.dre2n.commons.command.BRCommand;
import io.github.dre2n.commons.util.messageutil.MessageUtil;
import io.github.dre2n.itemsxl.ItemsXL;
import io.github.dre2n.itemsxl.config.IMessage;
import io.github.dre2n.itemsxl.item.ItemBox;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
public class OpenCommand extends BRCommand {

    ItemsXL plugin = ItemsXL.getInstance();

    public OpenCommand() {
        setCommand("open");
        setMinArgs(0);
        setMaxArgs(0);
        setHelp(IMessage.COMMAND_HELP_OPEN.getMessage());
        setPlayerCommand(true);
        setConsoleCommand(false);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        Player player = (Player) sender;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() != Material.SKULL_ITEM) {
            MessageUtil.sendMessage(sender, IMessage.ERROR_NO_ITEM_BOX.getMessage());
            return;
        }

        ItemBox box = ItemBox.getByItemStack(itemStack);

        if (box != null) {
            box.open(player);

        } else {
            MessageUtil.sendMessage(sender, IMessage.ERROR_NO_ITEM_BOX.getMessage());
        }
    }

}
