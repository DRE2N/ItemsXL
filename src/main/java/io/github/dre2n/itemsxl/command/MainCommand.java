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
import static io.github.dre2n.commons.util.messageutil.FatLetters.*;
import io.github.dre2n.commons.util.messageutil.MessageUtil;
import io.github.dre2n.itemsxl.ItemsXL;
import io.github.dre2n.itemsxl.config.IMessage;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * @author Daniel Saukel
 */
public class MainCommand extends BRCommand {

    ItemsXL plugin = ItemsXL.getInstance();

    public MainCommand() {
        setCommand("main");
        setHelp(IMessage.COMMAND_HELP_MAIN.getMessage());
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        List<UniversalItem> itemList = CaliburnAPI.getInstance().getItems().getItems(CustomItem.class);

        MessageUtil.sendCenteredMessage(sender, "&4" + I[0] + T[0] + E[0] + M[0] + S[0] + "&f" + X[0] + L[0]);
        MessageUtil.sendCenteredMessage(sender, "&4" + I[1] + T[1] + E[1] + M[1] + S[1] + "&f" + X[1] + L[1]);
        MessageUtil.sendCenteredMessage(sender, "&4" + I[2] + T[2] + E[2] + M[2] + S[2] + "&f" + X[2] + L[2]);
        MessageUtil.sendCenteredMessage(sender, "&4" + I[3] + T[3] + E[3] + M[3] + S[3] + "&f" + X[3] + L[3]);
        MessageUtil.sendCenteredMessage(sender, "&4" + I[4] + T[4] + E[4] + M[4] + S[4] + "&f" + X[4] + L[4]);
        MessageUtil.sendCenteredMessage(sender, "&b&l######## " + IMessage.COMMAND_MAIN_WELCOME.getMessage() + " &7v" + plugin.getDescription().getVersion() + " &b&l########");
        MessageUtil.sendCenteredMessage(sender, IMessage.COMMAND_MAIN_LOADED.getMessage(String.valueOf(itemList.size())));
        MessageUtil.sendCenteredMessage(sender, IMessage.COMMAND_MAIN_COMPATIBILITY.getMessage(CompatibilityHandler.getInstance().getInternals().toString()));
        MessageUtil.sendCenteredMessage(sender, IMessage.COMMAND_MAIN_HELP.getMessage());
        MessageUtil.sendCenteredMessage(sender, "&7\u00a92015-2017 Daniel Saukel; licensed under GPLv3.");
    }

}
