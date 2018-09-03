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
import de.erethon.caliburn.category.Categorizable;
import de.erethon.caliburn.item.CustomItem;
import de.erethon.caliburn.item.ExItem;
import de.erethon.caliburn.item.VanillaItem;
import de.erethon.caliburn.mob.CustomMob;
import de.erethon.caliburn.mob.VanillaMob;
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
        setMaxArgs(2);
        setHelp(IMessage.HELP_LIST.getMessage());
        setPermission("ixl.list");
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        int i = 1;
        List<?> objects = null;
        if (args.length > 1) {
            i++;
            if (args[1].equalsIgnoreCase("ci")) {
                objects = api.getExItems(CustomItem.class);
            } else if (args[1].equalsIgnoreCase("vi")) {
                objects = api.getExItems(VanillaItem.class);
            } else if (args[1].equalsIgnoreCase("cm")) {
                objects = api.getExMobs(CustomMob.class);
            } else if (args[1].equalsIgnoreCase("vm")) {
                objects = api.getExMobs(VanillaMob.class);
            } else {
                objects = api.getExItems();
            }
        } else {
            objects = api.getExItems();
        }
        List<Object> toSend = new ArrayList<>();

        int page = 1;
        if (args.length == i + 1) {
            page = NumberUtil.parseInt(args[i], 1);
        }
        int send = 0;
        int max = 0;
        int min = 0;
        for (Object object : objects) {
            if (sender.hasPermission("ixl.list." + ((Categorizable) object).getId())) {
                send++;
                if (send >= page * 5 - 4 && send <= page * 5) {
                    min = page * 5 - 4;
                    max = page * 5;
                    toSend.add(object);
                }
            }
        }

        MessageUtil.sendPluginTag(sender, ItemsXL.getInstance());
        MessageUtil.sendCenteredMessage(sender, "&4&l[ &6" + min + "-" + max + " &4/&6 " + send + " &4|&6 " + page + " &4&l]");
        toSend.forEach(o -> MessageUtil.sendMessage(sender, "&b" + ((Categorizable) o).getId() + "&7 | &e" + o.getClass().getSimpleName()
                + ((o instanceof ExItem) ? "&7 | &e" + ((ExItem) o).getMaterial() : "")));
    }

}
