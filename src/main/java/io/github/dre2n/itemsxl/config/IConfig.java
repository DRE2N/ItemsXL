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

import io.github.dre2n.caliburn.CaliburnAPI;
import io.github.dre2n.commons.config.BRConfig;
import java.io.File;
import org.bukkit.ChatColor;

/**
 * @author Daniel Saukel
 */
public class IConfig extends BRConfig {

    public static final int CONFIG_VERSION = 1;

    private String language = "en";

    private boolean builtInItems = true;

    private String boxName = "&6Mysterious Box";
    private String advancedWorkbenchName = "&6Advanced Workbench";

    public IConfig(File file) {
        super(file, CONFIG_VERSION);

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
     * @param language
     * the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return whether or not the built-in items should be loaded
     */
    public boolean enableBuiltInItems() {
        return builtInItems;
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
            config.set("identifierPrefix", CaliburnAPI.IDENTIFIER_PREFIX);
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
        if (config.contains("language")) {
            language = config.getString("language");
        }

        if (!config.contains("builtInItems")) {
            builtInItems = config.getBoolean("builtInItems");
        }

        if (!config.contains("identifierPrefix")) {
            CaliburnAPI.IDENTIFIER_PREFIX = ChatColor.translateAlternateColorCodes('&', config.getString("identifierPrefix"));
        }
        if (!config.contains("boxNames")) {
            boxName = config.getString("boxName");
        }
        if (!config.contains("advancedWorkbenchName")) {
            advancedWorkbenchName = config.getString("advancedWorkbenchName");
        }
    }

}
