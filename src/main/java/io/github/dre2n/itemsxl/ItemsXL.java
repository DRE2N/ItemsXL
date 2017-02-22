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
package io.github.dre2n.itemsxl;

import io.github.dre2n.caliburn.CaliburnAPI;
import io.github.dre2n.caliburn.item.ItemCategories;
import io.github.dre2n.caliburn.item.Items;
import io.github.dre2n.caliburn.item.UniversalItem;
import io.github.dre2n.caliburn.mob.CustomMob;
import io.github.dre2n.caliburn.mob.MobCategories;
import io.github.dre2n.caliburn.mob.Mobs;
import io.github.dre2n.caliburn.mob.UniversalMob;
import io.github.dre2n.caliburn.util.CaliConfiguration;
import io.github.dre2n.commons.command.BRCommands;
import io.github.dre2n.commons.compatibility.Internals;
import io.github.dre2n.commons.config.MessageConfig;
import io.github.dre2n.commons.javaplugin.BRPlugin;
import io.github.dre2n.commons.javaplugin.BRPluginSettings;
import io.github.dre2n.commons.util.FileUtil;
import io.github.dre2n.itemsxl.command.*;
import io.github.dre2n.itemsxl.config.IConfig;
import io.github.dre2n.itemsxl.config.IMessages;
import io.github.dre2n.itemsxl.listener.BlockListener;
import java.io.File;
import java.io.IOException;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

/**
 * @author Daniel Saukel
 */
public class ItemsXL extends BRPlugin {

    CaliburnAPI api;

    private static ItemsXL instance;

    public static File ITEMS;
    public static File MOBS;

    private IConfig iConfig;
    private MessageConfig messageConfig;
    private BRCommands iCommands;

    public ItemsXL() {
        /*
         * ##########################
         * ####~BRPluginSettings~####
         * ##########################
         * #~Internals~##~~v1_9_R1+~#
         * #~SpigotAPI~##~~~false~~~#
         * #~~~~UUID~~~##~~~~true~~~#
         * #~~Economy~~##~~~false~~~#
         * #Permissions##~~~false~~~#
         * #~~Metrics~~##~~~~true~~~#
         * #Resource ID##~~~14472~~~#
         * ##########################
         */

        settings = new BRPluginSettings(false, true, false, false, true, 14472, Internals.andHigher(Internals.v1_9_R1));
    }

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        loadAPI();
        loadIConfig();
        loadMessageConfig();
        loadICommands();

        manager.registerEvents(new BlockListener(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    /**
     * @return the plugin instance
     */
    public static ItemsXL getInstance() {
        return instance;
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
     * @return the loaded instance of MessageConfig
     */
    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    /**
     * load / reload a new instance of MessageConfig
     */
    public void loadMessageConfig() {
        File folder = new File(getDataFolder(), "languages");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        messageConfig = new MessageConfig(IMessages.class, new File(folder, iConfig.getLanguage() + ".yml"));
    }

    /**
     * @return the iCommands
     */
    @Override
    public BRCommands getCommands() {
        return iCommands;
    }

    /**
     * @param iCommands
     * the iCommands to set
     */
    public void loadICommands() {
        iCommands = new BRCommands(
                "itemsxl",
                this,
                new HelpCommand(),
                new GiveCommand(),
                new ListCommand(),
                new MainCommand(),
                new OpenCommand(),
                new ReloadCommand(),
                new SerializeCommand()
        );

        iCommands.register(this);
    }

    /**
     * load / reload a new instance of CaliburnAPI
     */
    public void loadAPI() {
        api = new CaliburnAPI(this);
        api.setup(loadItems(), loadMobs(), loadItemCategories(), loadMobCategories());
    }

    /**
     * load / reload a new instance of ItemCategories
     */
    public ItemCategories loadItemCategories() {
        File file = new File(getDataFolder(), "ItemCategories.yml");
        CaliConfiguration config;

        if (!file.exists()) {
            try {
                file.createNewFile();
                config = CaliConfiguration.loadConfiguration(file);
                config.save(file);

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        config = CaliConfiguration.loadConfiguration(file);

        return new ItemCategories(api, config);
    }

    /**
     * load / reload a new instance of MobCategories
     */
    public MobCategories loadMobCategories() {
        File file = new File(getDataFolder(), "MobCategories.yml");
        CaliConfiguration config;

        if (!file.exists()) {
            try {
                file.createNewFile();
                config = CaliConfiguration.loadConfiguration(file);
                config.save(file);

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        config = CaliConfiguration.loadConfiguration(file);

        return new MobCategories(api, config);
    }

    /**
     * load / reload a new instance of Items
     */
    public Items loadItems() {
        Items items = new Items(api);

        File folder = new File(getDataFolder() + "/custom/items");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (File file : FileUtil.getFilesForFolder(folder)) {
            items.addItem(file.getName().substring(0, file.getName().length() - 4), CaliConfiguration.loadConfiguration(file));
        }

        File vanilla = new File(getDataFolder() + "/vanilla/items");
        if (!vanilla.exists()) {
            vanilla.mkdirs();
        }

        for (Material material : Material.values()) {
            File file = new File(vanilla, material.getId() + ".yml");
            CaliConfiguration config;
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                config = CaliConfiguration.loadConfiguration(file);
                config.set("material", material.toString());
                config.createSection("categoryDamageModifiers");
                config.createSection("mobDamageModifiers");
                try {
                    config.save(file);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            } else {
                config = CaliConfiguration.loadConfiguration(file);
            }

            items.addItem(new UniversalItem(api, String.valueOf(material.getId()), config));
        }

        return items;
    }

    /**
     * load / reload a new instance of Mobs
     */
    public Mobs loadMobs() {
        Mobs mobs = new Mobs(api);

        File folder = new File(getDataFolder() + "/custom/mobs");
        if (!folder.exists()) {
            folder.mkdir();
        }

        for (File file : FileUtil.getFilesForFolder(folder)) {
            mobs.addMob(new CustomMob(api, file.getName().substring(0, file.getName().length() - 4), CaliConfiguration.loadConfiguration(file)));
        }

        File vanilla = new File(getDataFolder() + "/vanilla/mobs");
        vanilla.mkdirs();

        for (EntityType entity : EntityType.values()) {
            File file = new File(vanilla, entity.getTypeId() + ".yml");
            CaliConfiguration config;
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                config = CaliConfiguration.loadConfiguration(file);
                config.set("species", entity.toString());
                config.createSection("categoryDamageModifiers");
                config.createSection("itemDamageModifiers");
                try {
                    config.save(file);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            } else {
                config = CaliConfiguration.loadConfiguration(file);
            }

            mobs.addMob(new UniversalMob(api, String.valueOf(entity.getTypeId()), config));
        }

        return mobs;
    }

}
