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
import io.github.dre2n.caliburn.item.CustomItem;
import io.github.dre2n.caliburn.item.UniversalItem;
import io.github.dre2n.commons.command.BRCommand;
import io.github.dre2n.commons.compatibility.CompatibilityHandler;
import io.github.dre2n.commons.util.messageutil.MessageUtil;
import io.github.dre2n.itemsxl.ItemsXL;
import io.github.dre2n.itemsxl.config.IMessages;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * @author Daniel Saukel
 */
public class ReloadCommand extends BRCommand {

    ItemsXL plugin = ItemsXL.getInstance();

    public ReloadCommand() {
        setCommand("reload");
        setMinArgs(0);
        setMaxArgs(0);
        setHelp(IMessages.COMMAND_HELP_RELOAD.getMessage());
        setPermission("ixl.reload");
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        List<UniversalItem> iItemList = CaliburnAPI.getInstance().getItems().getItems(CustomItem.class);

        plugin.loadIConfig();
        plugin.loadMessageConfig();
        plugin.loadICommands();
        plugin.loadAPI();

        MessageUtil.sendPluginTag(sender, plugin);
        MessageUtil.sendCenteredMessage(sender, IMessages.COMMAND_RELOAD_SUCCESS.getMessage());
        MessageUtil.sendCenteredMessage(sender, IMessages.COMMAND_MAIN_LOADED.getMessage(String.valueOf(iItemList.size())));
        MessageUtil.sendCenteredMessage(sender, IMessages.COMMAND_MAIN_COMPATIBILITY.getMessage(String.valueOf(CompatibilityHandler.getInstance().getInternals())));
    }

}
