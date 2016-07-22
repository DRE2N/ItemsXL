/*
 * Copyright (C) 2015-2016 Daniel Saukel
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
package io.github.dre2n.itemsxl.config;

import io.github.dre2n.commons.config.Messages;
import io.github.dre2n.itemsxl.ItemsXL;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Daniel Saukel
 */
public enum IMessages implements Messages {

    ERROR_NO_OBJECT("error.noObject", "&cThis &v1 does not exist."),
    ERROR_NO_PERMISSION("error.noPermission", "&cYou do not have permission to use the command &o&v1&r&c."),
    ERROR_NO_CONSOLE_COMMAND("error.noConsoleCommand", "&cThe command &o&v1&r&c is not a console command!"),
    ERROR_NO_ITEM_BOX("error.noItemBox", "&cThis is not a valid item box."),
    ERROR_NO_PLAYER_COMMAND("error.noPlayerCommand", "&cThe command &o&v1&r&c is not a player command!"),
    COMMAND_MAIN_WELCOME("command.main.welcome", "&7Welcome to &4Items&fXL"),
    COMMAND_MAIN_LOADED("command.main.loaded", "&eItems loaded: &o[&v1]"),// &eRecipes loaded: &o[&v2]"),
    COMMAND_MAIN_COMPATIBILITY("command.main.compatibility", "&eInternals: &o[&v1]"),
    COMMAND_MAIN_HELP("command.main.help", "&7Type in &o/ixl help&r&7 for further information."),
    COMMAND_OPEN_SUCCESS("command.open.success", "&7You opened the box. You found the item &o&v1&7!"),
    COMMAND_RELOAD_SUCCESS("command.reload.success", "&7Successfully reloaded ItemsXL."),
    COMMAND_HELP_GIVE("help.give", "&7Give an item to a player. Usage: &o/ixl give [player=you] [id] ([amount=1]) (box)"),
    COMMAND_HELP_HELP("help.help", "&7Show command help. Usage: &o/ixl help [page]"),
    COMMAND_HELP_INFO("help.info", "&7Show information about an item. Usage: &o/ixl info [item]"),
    COMMAND_HELP_LIST("help.list", "&7List all custom items. Usage: &o/ixl list [page]"),
    COMMAND_HELP_MAIN("help.main", "&7General status information."),
    COMMAND_HELP_OPEN("help.open", "&7Open the item box in your hand."),
    COMMAND_HELP_RELOAD("help.reload", "&7Reload all configs and data. Usage: &o/ixl reload"),
    COMMAND_HELP_SERIALIZE("help.serialize", "&7Serialize the item stack in your hand. Usage: &o/ixl serialize ([bukkit|caliburn])"),
    OBJECT_PLAYER("object.player", "player"),
    OBJECT_ITEM("object.item", "item");

    private String identifier;
    private String message;

    IMessages(String identifier, String message) {
        this.identifier = identifier;
        this.message = message;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessage(String... args) {
        return ItemsXL.getInstance().getMessageConfig().getMessage(this, args);
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /* Statics */
    /**
     * @param identifer
     * the identifer to set
     */
    public static Messages getByIdentifier(String identifier) {
        for (Messages message : values()) {
            if (message.getIdentifier().equals(identifier)) {
                return message;
            }
        }

        return null;
    }

    /**
     * @return a FileConfiguration containing all messages
     */
    public static FileConfiguration toConfig() {
        FileConfiguration config = new YamlConfiguration();
        for (IMessages message : values()) {
            config.set(message.getIdentifier(), message.message);
        }

        return config;
    }

}
