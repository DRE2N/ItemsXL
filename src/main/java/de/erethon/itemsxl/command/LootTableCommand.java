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
import de.erethon.caliburn.loottable.LootTable;
import de.erethon.caliburn.loottable.LootTable.Entry;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.erethon.commons.misc.NumberUtil;
import de.erethon.headlib.HeadLib;
import de.erethon.itemsxl.ItemsXL;
import de.erethon.itemsxl.config.IMessage;
import de.erethon.vignette.api.GUI;
import de.erethon.vignette.api.PaginatedInventoryGUI;
import de.erethon.vignette.api.SingleInventoryGUI;
import de.erethon.vignette.api.action.Action;
import de.erethon.vignette.api.action.InteractionListener;
import de.erethon.vignette.api.component.InventoryButton;
import de.erethon.vignette.api.context.StatusModifier;
import de.erethon.vignette.api.layout.FlowInventoryLayout;
import de.erethon.vignette.api.layout.PaginatedFlowInventoryLayout;
import de.erethon.vignette.api.layout.PaginatedInventoryLayout;
import de.erethon.vignette.api.layout.PaginatedInventoryLayout.PaginationButtonPosition;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Daniel Saukel
 */
public class LootTableCommand extends DRECommand {

    private CaliburnAPI api;

    private static final String LOOT_TABLE = "LOOT_TABLE";
    private static final String SETTING_CHANCE = "SETTING_CHANCE";
    private static final String LAST_GUI = "LAST_GUI";
    private static final String TABLE_ENTRY = "TABLE_ENTRY";
    private static final String CURRENT_NUMBER = "CURRENT_NUMBER";

    private static final BigDecimal BD_100 = new BigDecimal("100.0");
    private static final BigDecimal BD_0 = new BigDecimal("0.0");

    private static final InventoryButton BACK = new InventoryButton(HeadLib.WOODEN_ARROW_LEFT.toItemStack(ChatColor.GOLD + "<="));
    private static final InventoryButton PLUS_10 = calcButton(new BigDecimal("10"));
    private static final InventoryButton PLUS_1 = calcButton(new BigDecimal("1"));
    private static final InventoryButton PLUS_0_1 = calcButton(new BigDecimal("0.1"));
    private static final InventoryButton PLUS_0_01 = calcButton(new BigDecimal("0.01"));
    private static final InventoryButton MINUS_10 = calcButton(new BigDecimal("-10"));
    private static final InventoryButton MINUS_1 = calcButton(new BigDecimal("-1"));
    private static final InventoryButton MINUS_0_1 = calcButton(new BigDecimal("-0.1"));
    private static final InventoryButton MINUS_0_01 = calcButton(new BigDecimal("-0.01"));

    private static final SingleInventoryGUI SET_NUMBER_GUI = new SingleInventoryGUI();

    static {
        SET_NUMBER_GUI.addStatusModifier(new StatusModifier<>(CURRENT_NUMBER, BD_100));
        SET_NUMBER_GUI.setLayout(new FlowInventoryLayout(SET_NUMBER_GUI, 18));

        BACK.setInteractionListener(e -> {
            e.getGUI().close(e.getPlayer());
        });
        SET_NUMBER_GUI.setCloseListener(e -> {
            GUI gui = e.getGUI();
            Entry entry = (Entry) gui.getStatusModifier(TABLE_ENTRY).getValue();
            entry.setLootChance(((BigDecimal) gui.getStatusModifier(CURRENT_NUMBER).getValue()).doubleValue());
            GUI main = (GUI) gui.getStatusModifier(LAST_GUI).getValue();
            // Apparently this doesn't work in the same tick -_-
            new BukkitRunnable() {
                @Override
                public void run() {
                    main.open(e.getPlayer());
                }
            }.runTaskLater(ItemsXL.getInstance(), 1l);
            main.removeStatusModifier(SETTING_CHANCE);
        });

        SET_NUMBER_GUI.add(BACK);
        SET_NUMBER_GUI.add(PaginatedInventoryLayout.PLACEHOLDER);
        SET_NUMBER_GUI.add(PaginatedInventoryLayout.PLACEHOLDER);
        SET_NUMBER_GUI.add(PLUS_10);
        SET_NUMBER_GUI.add(PLUS_1);
        SET_NUMBER_GUI.add(PLUS_0_1);
        SET_NUMBER_GUI.add(PLUS_0_01);
        SET_NUMBER_GUI.add(PaginatedInventoryLayout.PLACEHOLDER);
        SET_NUMBER_GUI.add(PaginatedInventoryLayout.PLACEHOLDER);
        SET_NUMBER_GUI.add(PaginatedInventoryLayout.PLACEHOLDER);
        SET_NUMBER_GUI.add(PaginatedInventoryLayout.PLACEHOLDER);
        SET_NUMBER_GUI.add(PaginatedInventoryLayout.PLACEHOLDER);
        SET_NUMBER_GUI.add(MINUS_10);
        SET_NUMBER_GUI.add(MINUS_1);
        SET_NUMBER_GUI.add(MINUS_0_1);
        SET_NUMBER_GUI.add(MINUS_0_01);
        SET_NUMBER_GUI.add(PaginatedInventoryLayout.PLACEHOLDER);
        SET_NUMBER_GUI.add(PaginatedInventoryLayout.PLACEHOLDER);
        SET_NUMBER_GUI.register();
    }

    public LootTableCommand(ItemsXL plugin) {
        api = plugin.getAPI();
        setCommand("loottable");
        setAliases("lt");
        setMinArgs(1);
        setMaxArgs(2);
        setHelp(IMessage.HELP_LOOT_TABLE.getMessage());
        setPermission("ixl.loottable");
        setPlayerCommand(true);
        setConsoleCommand(false);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        String name = args[1].replace("/", "");
        LootTable table = api.getLootTable(name) != null ? api.getLootTable(name) : new LootTable(api, name);
        if (args.length == 3 && args[2].toUpperCase().startsWith("-D")) {
            deleteIfExists(sender, table);
            return;
        }

        PaginatedInventoryGUI gui = new PaginatedInventoryGUI();
        gui.addStatusModifier(new StatusModifier<>(LOOT_TABLE, table));
        PaginatedFlowInventoryLayout layout = new PaginatedFlowInventoryLayout(gui, 54, PaginationButtonPosition.BOTTOM);
        gui.setLayout(layout);
        gui.setTitle(IMessage.COMMAND_LOOT_TABLE_TITLE.getMessage());

        for (LootTable.Entry entry : table.getEntries()) {
            addButton(layout, entry);
        }

        gui.setMoveItemStackListener(e -> {
            InventoryButton button = e.confirmAsButton();
            if (button != null) {
                Entry entry = table.new Entry(String.valueOf(e.getSlot()), e.getItemStack(), 100.0);
                table.addEntry(entry);
                button.setLeftClickLocked(false);
                button.setInteractionListener(getEntryClickListener(layout.getGUI()));
                button.addStatusModifier(new StatusModifier<>(TABLE_ENTRY, entry));
                addButton(layout, entry);
            } else {
                button = (InventoryButton) layout.getComponent(e.getSlot());
                gui.remove(button);
                Entry entry = table.getEntry(String.valueOf(e.getSlot()));
                table.removeEntry(entry);
            }
        });

        gui.setCloseListener(e -> {
            if (e.getGUI().hasStatusModifier(SETTING_CHANCE)) {
                return;
            }
            boolean empty = true;
            for (Entry entry : table.getEntries()) {
                if (entry != null && entry.getLootItem().getType() != Material.AIR) {
                    empty = false;
                }
            }
            if (empty) {
                deleteIfExists(sender, table);
            } else {
                save(sender, table);
            }
        });

        gui.register();
        gui.open((Player) sender);
    }

    private File getLootTableFile(LootTable table) {
        return new File(api.getDataFolder() + "/custom/loottables", table.getName() + ".yml");
    }

    private void deleteIfExists(CommandSender sender, LootTable table) {
        api.getLootTables().remove(table);
        File file = getLootTableFile(table);
        if (!file.exists()) {
            MessageUtil.sendMessage(sender, IMessage.ERROR_NO_OBJECT.getMessage(IMessage.OBJECT_LOOT_TABLE.getMessage()));
            return;
        }
        file.delete();
        MessageUtil.sendMessage(sender, IMessage.COMMAND_LOOT_TABLE_DELETED.getMessage(table.getName()));
    }

    private void save(CommandSender sender, LootTable table) {
        FileConfiguration config = new YamlConfiguration();
        table.serialize().entrySet().forEach(e -> config.set(e.getKey(), e.getValue()));
        try {
            config.save(getLootTableFile(table));
            MessageUtil.sendMessage(sender, IMessage.COMMAND_LOOT_TABLE_SAVED.getMessage(table.getName()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void addButton(PaginatedFlowInventoryLayout layout, Entry entry) {
        InventoryButton button = new InventoryButton(entry.getLootItem());
        button.setLeftClickLocked(false);
        button.setInteractionListener(getEntryClickListener(layout.getGUI()));
        button.addStatusModifier(new StatusModifier<>(TABLE_ENTRY, entry));
        int id = NumberUtil.parseInt(entry.getId(), Integer.MIN_VALUE);
        if (id != Integer.MIN_VALUE) {
            layout.set(id, button);
        } else {
            layout.add(button);
        }
    }

    private InteractionListener getEntryClickListener(GUI gui) {
        return e -> {
            if (e.getAction() != Action.RIGHT_CLICK) {
                return;
            }
            e.getGUI().addStatusModifier(new StatusModifier(SETTING_CHANCE));
            Entry entry = (Entry) e.getButton().getStatusModifier(TABLE_ENTRY).getValue();
            SET_NUMBER_GUI.setTitle(writeChance(new BigDecimal(entry.getLootChance())));
            GUI snGUI = SET_NUMBER_GUI.copy().open(e.getPlayer());
            snGUI.addStatusModifier(new StatusModifier<>(LAST_GUI, gui));
            snGUI.addStatusModifier(new StatusModifier<>(TABLE_ENTRY, entry));
            snGUI.addStatusModifier(new StatusModifier<>(CURRENT_NUMBER, new BigDecimal(entry.getLootChance()).setScale(2, RoundingMode.HALF_UP)));
        };
    }

    private static String writeChance(BigDecimal d) {
        return ChatColor.BLUE.toString() + d.setScale(2, RoundingMode.HALF_UP) + '%';
    }

    private static InventoryButton calcButton(BigDecimal d) {
        InventoryButton button = new InventoryButton((d.doubleValue() > 0 ? HeadLib.STONE_ARROW_UP : HeadLib.STONE_ARROW_DOWN)
                .toItemStack((d.doubleValue() > 0 ? ChatColor.GREEN : ChatColor.DARK_RED) + d.toString()));
        button.setInteractionListener(e -> {
            StatusModifier<BigDecimal> currentNumber = e.getGUI().getStatusModifier(CURRENT_NUMBER);
            BigDecimal value = currentNumber.getValue().add(d);
            if (value.compareTo(BD_100) > 0) {
                value = BD_100;
            } else if (value.compareTo(BD_0) < 0) {
                value = BD_0;
            }
            currentNumber.setValue(value);
            e.getGUI().setTitle(writeChance(currentNumber.getValue()));
            e.getGUI().open(e.getPlayer());
        });
        return button;
    }

}
