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
import de.erethon.caliburn.category.Categorizable;
import de.erethon.caliburn.item.ExItem;
import de.erethon.caliburn.item.VanillaItem;
import de.erethon.caliburn.loottable.LootTable;
import de.erethon.caliburn.mob.VanillaMob;
import de.erethon.caliburn.recipe.CustomRecipe;
import de.erethon.caliburn.recipe.CustomShapedRecipe;
import de.erethon.caliburn.recipe.CustomShapelessRecipe;
import de.erethon.caliburn.util.RecipeUtil;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.erethon.commons.misc.NumberUtil;
import de.erethon.itemsxl.ItemsXL;
import de.erethon.itemsxl.config.IMessage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Daniel Saukel, Fyreum
 */
public class ListCommand extends DRECommand {

    private final CaliburnAPI api;

    public ListCommand(ItemsXL plugin) {
        api = plugin.getAPI();
        setCommand("list");
        setMinArgs(0);
        setMaxArgs(2);
        setHelp(IMessage.HELP_LIST.getMessage());
        setPermission("ixl.list");
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        int i = 1;
        Collection<?> objects;
        if (args.length > 1) {
            i++;
            if (args[1].equalsIgnoreCase("ci")) {
                objects = api.getCustomItems();
            } else if (args[1].equalsIgnoreCase("vi") || args[1].equalsIgnoreCase("vanillaItems")) {
                objects = VanillaItem.getLoaded();
            } else if (args[1].equalsIgnoreCase("cm") || args[1].equalsIgnoreCase("customMobs")) {
                objects = api.getCustomMobs();
            } else if (args[1].equalsIgnoreCase("vm") || args[1].equalsIgnoreCase("vanillaMobs")) {
                objects = VanillaMob.getLoaded();
            } else if (args[1].equalsIgnoreCase("m") || args[1].equalsIgnoreCase("mobs")) {
                objects = api.getExMobs();
            } else if (args[1].equalsIgnoreCase("lt") || args[1].equalsIgnoreCase("lootTables")) {
                objects = api.getLootTables();
            } else if (args[1].equalsIgnoreCase("r") || args[1].equalsIgnoreCase("recipes")) {
                objects = api.getRecipes();
            } else {
                objects = api.getExItems();
            }
        } else {
            objects = api.getExItems();
        }
        List<Object> toSend = new ArrayList<>();

        int page = 1;
        if (args.length == i + 1) {
            page = NumberUtil.parseInt(args[i], 1);
        }
        int send = 0;
        int max = 0;
        int min = 0;
        for (Object object : objects) {
            if (sender.hasPermission("ixl.list." + getId(object))) {
                send++;
                if (send >= page * 5 - 4 && send <= page * 5) {
                    min = page * 5 - 4;
                    max = page * 5;
                    toSend.add(object);
                }
            }
        }

        MessageUtil.sendPluginTag(sender, ItemsXL.getInstance());
        MessageUtil.sendCenteredMessage(sender, "&4&l[ &6" + min + "-" + max + " &4/&6 " + send + " &4|&6 " + page + " &4&l]");
        toSend.forEach(o -> MessageUtil.sendMessage(sender, getMessage(o)));
    }

    private TextComponent getMessage(Object o) {
        if (!(o instanceof CustomRecipe)) {
            return new TextComponent(ChatColor.translateAlternateColorCodes('&',
                    "&b" + getId(o) + "&7 | &e" + o.getClass().getSimpleName() + ((o instanceof ExItem) ? "&7 | &e" + ((ExItem) o).getMaterial() : "")));
        }
        return createRecipeTextComponent((CustomRecipe) o);
    }

    private String getId(Object object) {
        if (object instanceof Categorizable) {
            return ((Categorizable) object).getId();
        } else if (object instanceof LootTable) {
            return ((LootTable) object).getName();
        } else if (object instanceof CustomRecipe) {
            return ((CustomRecipe) object).getId();
        } else {
            return "";
        }
    }

    public TextComponent createRecipeTextComponent(CustomRecipe recipe) {
        TextComponent component = new TextComponent();
        String stringKey;
        if (recipe instanceof CustomShapedRecipe) {
            CustomShapedRecipe shaped = (CustomShapedRecipe) recipe;
            stringKey = shaped.getKey().getKey();
            // info
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                    "Result: " + stringKey + "\n" +
                            "Amount: " + shaped.getAmount() + "\n" +
                            "Shape: " + RecipeUtil.toShapeString(shaped.getShape()) + "\n" +
                            "Ingredients: " + shaped.toIngredientString()
            )));
        } else {
            CustomShapelessRecipe shapeless = (CustomShapelessRecipe) recipe;
            stringKey = shapeless.getKey().getKey();
            // info
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                    "Result: " + shapeless.getRecipeResult().getStringKey() + "\n" +
                            "Amount: " + shapeless.getAmount() + "\n" +
                            "Ingredients: " + shapeless.toIngredientString()
            )));
        }
        component.setText(stringKey);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ixl recipeEditor " + stringKey));
        return component;
    }

}
