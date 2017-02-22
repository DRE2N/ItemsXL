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
import io.github.dre2n.commons.command.BRCommand;
import io.github.dre2n.commons.util.NumberUtil;
import io.github.dre2n.commons.util.messageutil.MessageUtil;
import io.github.dre2n.itemsxl.ItemsXL;
import io.github.dre2n.itemsxl.config.IMessages;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * @author Daniel Saukel
 */
public class ListCommand extends BRCommand {

    CaliburnAPI api = CaliburnAPI.getInstance();

    public ListCommand() {
        setCommand("list");
        setMinArgs(0);
        setMaxArgs(1);
        setHelp(IMessages.COMMAND_HELP_LIST.getMessage());
        setPermission("ixl.list");
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        List<UniversalItem> iItemList = api.getItems().getItems();
        ArrayList<UniversalItem> toSend = new ArrayList<>();

        int page = 1;
        if (args.length == 2) {
            page = NumberUtil.parseInt(args[1], 1);
        }
        int send = 0;
        int max = 0;
        int min = 0;
        for (UniversalItem iItem : iItemList) {
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

        for (UniversalItem iItem : toSend) {
            MessageUtil.sendMessage(sender, "&b" + iItem.getId() + "&7 | &e" + iItem.getClass().getSimpleName() + "&7 | &e" + iItem.getMaterial());
        }
    }

}
