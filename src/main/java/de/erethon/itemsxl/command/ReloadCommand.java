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
public class ReloadCommand extends DRECommand {

    private ItemsXL plugin;
    private CaliburnAPI api;

    public ReloadCommand(ItemsXL plugin) {
        this.plugin = plugin;
        api = plugin.getAPI();
        setCommand("reload");
        setMinArgs(0);
        setMaxArgs(0);
        setHelp(IMessage.HELP_RELOAD.getMessage());
        setPermission("ixl.reload");
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        Collection<CustomItem> ci = api.getCustomItems();
        Collection<VanillaItem> vi = VanillaItem.getLoaded();

        plugin.reloadMessageHandler();
        plugin.loadIConfig();
        plugin.loadICommandCache();
        plugin.getAPI().reload();

        MessageUtil.sendPluginTag(sender, plugin);
        MessageUtil.sendCenteredMessage(sender, IMessage.COMMAND_RELOAD_SUCCESS.getMessage());
        MessageUtil.sendCenteredMessage(sender, IMessage.COMMAND_MAIN_LOADED.getMessage(String.valueOf(ci.size()), String.valueOf(vi.size())));
        MessageUtil.sendCenteredMessage(sender, IMessage.COMMAND_MAIN_COMPATIBILITY.getMessage(String.valueOf(CompatibilityHandler.getInstance().getInternals())));
    }

}
