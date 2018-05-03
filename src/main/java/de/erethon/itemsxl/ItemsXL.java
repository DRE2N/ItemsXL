/*
 * Copyright (C) 2015-2018 Daniel Saukel
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
package de.erethon.itemsxl;

import de.erethon.caliburn.CaliburnAPI;
import de.erethon.caliburn.category.Category;
import de.erethon.caliburn.item.ExItem;
import de.erethon.caliburn.item.VanillaItem;
import de.erethon.caliburn.mob.ExMob;
import de.erethon.caliburn.mob.VanillaMob;
import de.erethon.commons.command.DRECommandCache;
import de.erethon.commons.compatibility.Internals;
import de.erethon.commons.config.MessageConfig;
import de.erethon.commons.javaplugin.DREPlugin;
import de.erethon.commons.javaplugin.DREPluginSettings;
import de.erethon.commons.misc.FileUtil;
import de.erethon.itemsxl.command.GiveCommand;
import de.erethon.itemsxl.command.HelpCommand;
import de.erethon.itemsxl.command.ListCommand;
import de.erethon.itemsxl.command.MainCommand;
import de.erethon.itemsxl.command.OpenCommand;
import de.erethon.itemsxl.command.ReloadCommand;
import de.erethon.itemsxl.command.SerializeCommand;
import de.erethon.itemsxl.config.IConfig;
import de.erethon.itemsxl.config.IMessage;
import de.erethon.itemsxl.item.ItemBoxListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Daniel Saukel
 */
public class ItemsXL extends DREPlugin {

    public static File ITEMS;
    public static File MOBS;

    private CaliburnAPI api;

    private IConfig iConfig;
    private DRECommandCache iCommands;

    public ItemsXL() {
        /*
         * ##########################
         * ####~DREPluginSettings~###
         * ##########################
         * #~Internals~##~~v1_8_R1+~#
         * #~SpigotAPI~##~~~false~~~#
         * #~~~~UUID~~~##~~~~true~~~#
         * #~~Economy~~##~~~false~~~#
         * #Permissions##~~~false~~~#
         * #~~Metrics~~##~~~~true~~~#
         * #Resource ID##~~~14472~~~#
         * ##########################
         */

        settings = new DREPluginSettings(false, true, false, false, true, 14472, Internals.andHigher(Internals.v1_8_R1));
    }

    @Override
    public void onEnable() {
        super.onEnable();

        loadIConfig();
        loadAPI();
        loadMessageConfig();
        loadICommandCache();

        manager.registerEvents(new ItemBoxListener(this), this);
    }

    /**
     * @return the loaded instance of IConfig
     */
    public IConfig getIConfig() {
        return iConfig;
    }

    /**
     * load / reload a new instance of IConfig
     */
    public void loadIConfig() {
        iConfig = new IConfig(new File(getDataFolder(), "config.yml"));
    }

    /**
     * load / reload a new instance of MessageConfig
     */
    public void loadMessageConfig() {
        File folder = new File(getDataFolder(), "languages");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        messageConfig = new MessageConfig(IMessage.class, new File(folder, iConfig.getLanguage() + ".yml"));
    }

    @Override
    public DRECommandCache getCommandCache() {
        return iCommands;
    }

    /**
     * @param iCommands
     * the iCommands to set
     */
    public void loadICommandCache() {
        iCommands = new DRECommandCache(
                "itemsxl",
                this,
                new HelpCommand(this),
                new GiveCommand(this),
                new ListCommand(this),
                new MainCommand(this),
                new OpenCommand(this),
                new ReloadCommand(this),
                new SerializeCommand(this)
        );

        iCommands.register(this);
    }

    /**
     * @return
     * the loaded instance of CaliburnAPI
     */
    public CaliburnAPI getAPI() {
        return api;
    }

    /**
     * load / reload a new instance of CaliburnAPI
     */
    public void loadAPI() {
        api = new CaliburnAPI(this, iConfig.getIdentifierPrefix());
        loadItems();
        loadMobs();
        loadItemCategories();
        loadMobCategories();
        api.finishInitialization();
    }

    /**
     * load / reload item categories
     */
    public void loadItemCategories() {
        File file = new File(getDataFolder(), "ItemCategories.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (Entry<String, Object> entry : config.getValues(true).entrySet()) {
            api.getItemCategories().add(new Category<>(api, entry.getKey(), (List<String>) entry.getValue()));
        }
    }

    /**
     * load / reload mob categories
     */
    public void loadMobCategories() {
        File file = new File(getDataFolder(), "MobCategories.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (Entry<String, Object> entry : config.getValues(true).entrySet()) {
            api.getItemCategories().add(new Category<>(api, entry.getKey(), (List<String>) entry.getValue()));
        }
    }

    /**
     * load / reload items
     */
    public void loadItems() {
        List<ExItem> items = api.getExItems();

        File custom = new File(getDataFolder() + "/custom/items");
        custom.mkdirs();

        for (File file : FileUtil.getFilesForFolder(custom)) {
            ExItem item = ExItem.deserialize(YamlConfiguration.loadConfiguration(file).getValues(true));
            String id = file.getName().substring(0, file.getName().length() - 4);
            items.add((ExItem) item.id(id));
        }

        File vanilla = new File(getDataFolder() + "/vanilla/items");
        if (!vanilla.exists()) {
            vanilla.mkdirs();
        }

        for (VanillaItem item : VanillaItem.getLoaded()) {
            File file = new File(vanilla, item.getId() + ".yml");
            FileConfiguration config;
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                config = YamlConfiguration.loadConfiguration(file);
                config.createSection("categoryDamageModifiers");
                config.createSection("mobDamageModifiers");
                try {
                    config.save(file);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            } else {
                config = YamlConfiguration.loadConfiguration(file);
            }

            item.setRaw(config.getValues(true));
            items.add(item);
        }
    }

    /**
     * load / reload mobs
     */
    public void loadMobs() {
        List<ExMob> mobs = api.getExMobs();

        File custom = new File(getDataFolder() + "/custom/mobs");
        custom.mkdirs();

        for (File file : FileUtil.getFilesForFolder(custom)) {
            ExMob mob = null;
            String id = file.getName().substring(0, file.getName().length() - 4);
            mobs.add((ExMob) mob.id(id));
        }

        File vanilla = new File(getDataFolder() + "/vanilla/mobs");
        vanilla.mkdirs();

        for (VanillaMob mob : VanillaMob.getLoaded()) {
            File file = new File(vanilla, mob.getId() + ".yml");
            FileConfiguration config;
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                config = YamlConfiguration.loadConfiguration(file);
                config.createSection("categoryDamageModifiers");
                config.createSection("itemDamageModifiers");
                try {
                    config.save(file);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            } else {
                config = YamlConfiguration.loadConfiguration(file);
            }

            mob.setRaw(config.getValues(true));
            mobs.add(mob);
        }
    }

}
