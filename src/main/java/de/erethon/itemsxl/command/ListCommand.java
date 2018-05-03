/*
 * Copyright (C) 2015-2018 Daniel Saukel
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
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * @author Daniel Saukel
 */
public class ListCommand extends DRECommand {

    private CaliburnAPI api;

    public ListCommand(ItemsXL plugin) {
        api = plugin.getAPI();
        setCommand("list");
        setMinArgs(0);
        setMaxArgs(1);
        setHelp(IMessage.COMMAND_HELP_LIST.getMessage());
        setPermission("ixl.list");
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        List<ExItem> iItemList = api.getExItems();
        ArrayList<ExItem> toSend = new ArrayList<>();

        int page = 1;
        if (args.length == 2) {
            page = NumberUtil.parseInt(args[1], 1);
        }
        int send = 0;
        int max = 0;
        int min = 0;
        for (ExItem iItem : iItemList) {
            if (sender.hasPermission("ixl.list." + iItem.getId())) {
                send++;
                if (send >= page * 5 - 4 && send <= page * 5) {
                    min = page * 5 - 4;
                    max = page * 5;
                    toSend.add(iItem);
                }
            }
        }

        MessageUtil.sendPluginTag(sender, ItemsXL.getInstance());
        MessageUtil.sendCenteredMessage(sender, "&4&l[ &6" + min + "-" + max + " &4/&6 " + send + " &4|&6 " + page + " &4&l]");
        toSend.forEach(i -> MessageUtil.sendMessage(sender, "&b" + i.getId() + "&7 | &e" + i.getClass().getSimpleName() + "&7 | &e" + i.getMaterial()));
    }

}
