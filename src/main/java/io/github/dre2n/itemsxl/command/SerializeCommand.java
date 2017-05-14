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
import io.github.dre2n.caliburn.item.UniversalItemStack;
import io.github.dre2n.commons.command.BRCommand;
import io.github.dre2n.commons.util.messageutil.MessageUtil;
import io.github.dre2n.itemsxl.ItemsXL;
import io.github.dre2n.itemsxl.config.IMessage;
import java.io.File;
import java.io.IOException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

/**
 * @author Daniel Saukel
 */
public class SerializeCommand extends BRCommand {

    public SerializeCommand() {
        setCommand("serialize");
        setMinArgs(0);
        setMaxArgs(1);
        setHelp(IMessage.COMMAND_HELP_SERIALIZE.getMessage());
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        Player player = (Player) sender;

        String type = "";

        if (args.length >= 2) {
            type = args[1];
        }

        ConfigurationSerializable serializable = player.getInventory().getItemInMainHand();

        if (type.equalsIgnoreCase("caliburn")) {
            serializable = new UniversalItemStack(CaliburnAPI.getInstance().getItems(), player.getInventory().getItemInMainHand());
        }

        File file = new File(ItemsXL.getInstance().getDataFolder(), "serialized.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        FileConfiguration config = new YamlConfiguration();
        config.set("serialized", serializable);
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        MessageUtil.sendMessage(sender, IMessage.COMMAND_SERIALIZE_SUCCESS.getMessage());
    }

}
