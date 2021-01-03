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
import de.erethon.caliburn.item.CustomItem;
import de.erethon.caliburn.item.VanillaItem;
import static de.erethon.commons.chat.FatLetter.*;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.erethon.commons.compatibility.CompatibilityHandler;
import de.erethon.itemsxl.ItemsXL;
import de.erethon.itemsxl.config.IMessage;
import java.util.Collection;
import org.bukkit.command.CommandSender;

/**
 * @author Daniel Saukel
 */
public class MainCommand extends DRECommand {

    private ItemsXL plugin;
    private CaliburnAPI api;

    public MainCommand(ItemsXL plugin) {
        this.plugin = plugin;
        api = plugin.getAPI();
        setCommand("main");
        setHelp(IMessage.HELP_MAIN.getMessage());
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        Collection<CustomItem> ci = api.getCustomItems();
        Collection<VanillaItem> vi = VanillaItem.getLoaded();

        MessageUtil.sendCenteredMessage(sender, "&4" + I[0] + T[0] + E[0] + M[0] + S[0] + "&f" + X[0] + L[0]);
        MessageUtil.sendCenteredMessage(sender, "&4" + I[1] + T[1] + E[1] + M[1] + S[1] + "&f" + X[1] + L[1]);
        MessageUtil.sendCenteredMessage(sender, "&4" + I[2] + T[2] + E[2] + M[2] + S[2] + "&f" + X[2] + L[2]);
        MessageUtil.sendCenteredMessage(sender, "&4" + I[3] + T[3] + E[3] + M[3] + S[3] + "&f" + X[3] + L[3]);
        MessageUtil.sendCenteredMessage(sender, "&4" + I[4] + T[4] + E[4] + M[4] + S[4] + "&f" + X[4] + L[4]);
        MessageUtil.sendCenteredMessage(sender, "&b&l######## " + IMessage.COMMAND_MAIN_WELCOME.getMessage() + " &7v" + plugin.getDescription().getVersion() + " &b&l########");
        MessageUtil.sendCenteredMessage(sender, "&b&o" + plugin.getDescription().getDescription());
        MessageUtil.sendCenteredMessage(sender, IMessage.COMMAND_MAIN_LOADED.getMessage(String.valueOf(ci.size()), String.valueOf(vi.size())));
        MessageUtil.sendCenteredMessage(sender, IMessage.COMMAND_MAIN_COMPATIBILITY.getMessage(CompatibilityHandler.getInstance().getInternals().toString()));
        MessageUtil.sendCenteredMessage(sender, IMessage.COMMAND_MAIN_HELP.getMessage());
        MessageUtil.sendCenteredMessage(sender, "&7\u00a92015-2021 Daniel Saukel; licensed under GPLv3.");
    }

}
