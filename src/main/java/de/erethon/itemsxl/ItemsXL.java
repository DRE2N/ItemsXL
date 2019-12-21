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
import de.erethon.caliburn.item.CustomItem;
import de.erethon.caliburn.item.VanillaItem;
import de.erethon.caliburn.loottable.LootTable;
import de.erethon.caliburn.mob.CustomMob;
import de.erethon.caliburn.mob.VanillaMob;
import de.erethon.commons.command.DRECommandCache;
import de.erethon.commons.compatibility.Internals;
import de.erethon.commons.config.MessageConfig;
import de.erethon.commons.config.RawConfiguration;
import de.erethon.commons.javaplugin.DREPlugin;
import de.erethon.commons.javaplugin.DREPluginSettings;
import de.erethon.commons.misc.FileUtil;
import de.erethon.itemsxl.command.*;
import de.erethon.itemsxl.config.IConfig;
import de.erethon.itemsxl.config.IMessage;
import de.erethon.itemsxl.item.ItemBoxListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.ChatColor;

/**
 * @author Daniel Saukel
 */
public class ItemsXL extends DREPlugin {

    private CaliburnAPI api;

    private IConfig iConfig;
    private DRECommandCache iCommands;

    public ItemsXL() {
        settings = DREPluginSettings.builder()
                .internals(Internals.andHigher(Internals.v1_8_R1))
                .metrics(true)
                .spigotMCResourceId(14472)
                .build();
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
     * load / reload a new instance of DRECommandCache
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
                new SerializeCommand(this),
                new SummonCommand(this)
        );

        iCommands.register(this);
    }

    /**
     * @return the loaded instance of CaliburnAPI
     */
    public CaliburnAPI getAPI() {
        return api;
    }

    /**
     * load / reload a new instance of CaliburnAPI
     */
    public void loadAPI() {
        api = new CaliburnAPI(this, ChatColor.translateAlternateColorCodes('&', iConfig.getIdentifierPrefix()));
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

        RawConfiguration config = RawConfiguration.loadConfiguration(file);
        for (Object entry : config.getArgs().entrySet()) {
            api.getItemCategories().add(new Category<>(api, ((Entry) entry).getKey().toString(), (List<String>) ((Entry) entry).getValue()));
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

        RawConfiguration config = RawConfiguration.loadConfiguration(file);
        for (Object entry : config.getArgs().entrySet()) {
            api.getItemCategories().add(new Category<>(api, ((Entry) entry).getKey().toString(), (List<String>) ((Entry) entry).getValue()));
        }
    }

    /**
     * load / reload items
     */
    public void loadItems() {
        File custom = new File(getDataFolder() + "/custom/items");
        custom.mkdirs();

        for (File file : FileUtil.getFilesForFolder(custom)) {
            RawConfiguration config = RawConfiguration.loadConfiguration(file);
            CustomItem item = new CustomItem(config.getArgs());
            String id = file.getName().substring(0, file.getName().length() - 4);
            item.register(id);
        }

        File vanilla = new File(getDataFolder() + "/vanilla/items");
        if (!vanilla.exists()) {
            vanilla.mkdirs();
        }

        for (VanillaItem item : VanillaItem.getLoaded()) {
            File file = new File(vanilla, item.getId() + ".yml");
            RawConfiguration config;
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                config = RawConfiguration.loadConfiguration(file);
                config.createSection("categoryDamageModifiers");
                config.createSection("mobDamageModifiers");
                try {
                    config.save(file);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            } else {
                config = RawConfiguration.loadConfiguration(file);
            }

            item.setRaw(config.getArgs());
        }
    }

    /**
     * load / reload mobs
     */
    public void loadMobs() {
        File custom = new File(getDataFolder() + "/custom/mobs");
        custom.mkdirs();

        for (File file : FileUtil.getFilesForFolder(custom)) {
            RawConfiguration config = RawConfiguration.loadConfiguration(file);
            CustomMob mob = new CustomMob(config.getArgs());
            String id = file.getName().substring(0, file.getName().length() - 4);
            mob.register(id);
        }

        File vanilla = new File(getDataFolder() + "/vanilla/mobs");
        vanilla.mkdirs();

        for (VanillaMob mob : VanillaMob.getLoaded()) {
            File file = new File(vanilla, mob.getId() + ".yml");
            RawConfiguration config;
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                config = RawConfiguration.loadConfiguration(file);
                config.createSection("categoryDamageModifiers");
                config.createSection("itemDamageModifiers");
                try {
                    config.save(file);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            } else {
                config = RawConfiguration.loadConfiguration(file);
            }

            mob.setRaw(config.getArgs());
        }
    }

    /**
     * load / reload loot tables
     */
    public void loadLootTables() {
        File custom = new File(getDataFolder() + "/custom/loottables");
        custom.mkdirs();
        FileUtil.getFilesForFolder(custom).forEach(f -> api.getLootTables().add(new LootTable(api, f)));
    }

}
