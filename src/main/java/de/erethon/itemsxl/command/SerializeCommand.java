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
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.erethon.commons.compatibility.Version;
import de.erethon.itemsxl.ItemsXL;
import de.erethon.itemsxl.config.IMessage;
import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
public class SerializeCommand extends DRECommand {

    private ItemsXL plugin;
    private CaliburnAPI api;

    public SerializeCommand(ItemsXL plugin) {
        this.plugin = plugin;
        api = plugin.getAPI();
        setCommand("serialize");
        setMinArgs(0);
        setMaxArgs(1);
        setHelp(IMessage.HELP_SERIALIZE.getMessage());
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

        ItemStack itemStack;
        if (Version.isAtLeast(Version.MC1_9)) {
            itemStack = player.getInventory().getItemInMainHand();
        } else {
            itemStack = player.getInventory().getItemInHand();
        }
        Object serialized = itemStack;

        if (type.equalsIgnoreCase("simple")) {
            serialized = api.getSimpleSerialization().serialize(itemStack);
            MessageUtil.sendMessage(sender, (String) serialized);
        }

        File file = new File(plugin.getDataFolder(), "serialized.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        FileConfiguration config = new YamlConfiguration();
        config.set("==", "org.bukkit.inventory.ItemStack");
        for (Entry<String, Object> entry : itemStack.serialize().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        MessageUtil.sendMessage(sender, IMessage.COMMAND_SERIALIZE_SUCCESS.getMessage());
    }

}
