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
package de.erethon.itemsxl.config;

import de.erethon.commons.config.Message;

/**
 * @author Daniel Saukel
 */
public enum IMessage implements Message {

    ERROR_COORDS_NOT_NUMERIC("error.coordsNotNumeric"),
    ERROR_INVALID_ID_TYPE("error.invalidIDType"),
    ERROR_IO("error.io"),
    ERROR_NO_OBJECT("error.noObject"),
    ERROR_NO_PERMISSION("error.noPermission"),
    ERROR_NO_CONSOLE_COMMAND("error.noConsoleCommand"),
    ERROR_NO_ITEM_BOX("error.noItemBox"),
    ERROR_NO_ITEM_IN_HAND("error.noItemInHand"),
    ERROR_NO_MOB_NEARBY("error.noMobNearby"),
    ERROR_NO_PLAYER_COMMAND("error.noPlayerCommand"),
    ERROR_VANILLA_FEATURE("error.vanillaFeature"),
    COMMAND_GIVE_SUCCESS("command.give.success"),
    COMMAND_LOOT_TABLE_DELETED("command.lootTable.deleted"),
    COMMAND_LOOT_TABLE_SAVED("command.lootTable.saved"),
    COMMAND_LOOT_TABLE_TITLE("command.lootTable.title"),
    COMMAND_MAIN_WELCOME("command.main.welcome"),
    COMMAND_MAIN_LOADED("command.main.loaded"),
    COMMAND_MAIN_COMPATIBILITY("command.main.compatibility"),
    COMMAND_MAIN_HELP("command.main.help"),
    COMMAND_OPEN_SUCCESS("command.open.success"),
    COMMAND_REGISTER_ITEM_SUCCESS("command.registerItem.success"),
    COMMAND_REGISTER_MOB_SUCCESS("command.registerMob.success"),
    COMMAND_RELOAD_SUCCESS("command.reload.success"),
    COMMAND_SERIALIZE_SUCCESS("command.serialize.success"),
    HELP_GIVE("help.give"),
    HELP_HELP("help.help"),
    HELP_INFO("help.info"),
    HELP_LIST("help.list"),
    HELP_LOOT_TABLE("help.lootTable"),
    HELP_MAIN("help.main"),
    HELP_OPEN("help.open"),
    HELP_REGISTER_ITEM("help.registerItem"),
    HELP_REGISTER_MOB("help.registerMob"),
    HELP_RELOAD("help.reload"),
    HELP_SERIALIZE("help.serialize"),
    HELP_SUMMON("help.summon"),
    OBJECT_PLAYER("object.player"),
    OBJECT_ITEM("object.item"),
    OBJECT_LOOT_TABLE("object.lootTable"),
    OBJECT_MOB("object.mob"),
    OBJECT_WORLD("object.world");

    private String path;

    IMessage(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

}
