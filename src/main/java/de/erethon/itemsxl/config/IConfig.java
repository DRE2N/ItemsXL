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

import de.erethon.commons.config.DREConfig;
import de.erethon.commons.javaplugin.DREPlugin;
import java.io.File;
import org.bukkit.ChatColor;

/**
 * @author Daniel Saukel
 */
public class IConfig extends DREConfig {

    private DREPlugin plugin;

    public static final int CONFIG_VERSION = 1;

    private String language = "english";
    private boolean builtInItems = true;
    private String identifierPrefix = "&7";
    private String boxName = "&6Mysterious Box";
    private String advancedWorkbenchName = "&6Advanced Workbench";

    public IConfig(DREPlugin plugin, File file) {
        super(file, CONFIG_VERSION);
        this.plugin = plugin;

        if (initialize) {
            initialize();
        }
        load();
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return whether or not the built-in items should be loaded
     */
    public boolean areBuiltInItemsEnabled() {
        return builtInItems;
    }

    /**
     * @return the prefix used to identify IXL items
     */
    public String getIdentifierPrefix() {
        return identifierPrefix;
    }

    /**
     * @return the boxName
     */
    public String getBoxName() {
        return ChatColor.translateAlternateColorCodes('&', boxName);
    }

    /**
     * @return the advancedWorkbenchName
     */
    public String getAdvancedWorkbenchName() {
        return ChatColor.translateAlternateColorCodes('&', advancedWorkbenchName);
    }

    @Override
    public void initialize() {
        if (!config.contains("language")) {
            config.set("language", language);
        }
        if (!config.contains("builtInItems")) {
            config.set("builtInItems", builtInItems);
        }
        if (!config.contains("identifierPrefix")) {
            config.set("identifierPrefix", identifierPrefix);
        }
        if (!config.contains("boxNames")) {
            config.set("boxName", boxName);
        }
        if (!config.contains("advancedWorkbenchName")) {
            config.set("advancedWorkbenchName", advancedWorkbenchName);
        }

        save();
    }

    @Override
    public void load() {
        language = config.getString("language", language);
        plugin.getMessageHandler().setDefaultLanguage(language);
        builtInItems = config.getBoolean("builtInItems", builtInItems);
        identifierPrefix = config.getString("identifierPrefix", identifierPrefix);
        boxName = config.getString("boxName", boxName);
        advancedWorkbenchName = config.getString("advancedWorkbenchName", advancedWorkbenchName);
    }

}
