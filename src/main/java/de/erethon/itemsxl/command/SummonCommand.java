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
import de.erethon.caliburn.mob.ExMob;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.erethon.commons.config.CommonMessage;
import de.erethon.itemsxl.ItemsXL;
import de.erethon.itemsxl.config.IMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Daniel Saukel
 */
public class SummonCommand extends DRECommand {
    
    private CaliburnAPI api;
    
    public SummonCommand(ItemsXL plugin) {
        api = plugin.getAPI();
        setCommand("summon");
        setMinArgs(1);
        setMaxArgs(7);
        setHelp(IMessage.HELP_SUMMON.getMessage());
        setPermission("ixl.summon");
        setPlayerCommand(true);
        setConsoleCommand(true);
    }
    
    @Override
    public void onExecute(String[] args, CommandSender sender) {
        ExMob mob = api.getExMob(args[1]);
        if (mob == null) {
            MessageUtil.sendMessage(sender, IMessage.ERROR_NO_OBJECT.getMessage(IMessage.OBJECT_MOB.getMessage()));
            return;
        }
        
        Location location = null;
        World world = null;
        double x = 0;
        double y = 0;
        double z = 0;
        float yaw = 0;
        float pitch = 0;
        
        int worldI = 2;
        int xI = 3;
        int yI = 4;
        int zI = 5;
        int yawI = 6;
        int pitchI = 7;

        // No world specified
        if (args.length == 7 || args.length == 5) {
            if (!(sender instanceof Player)) {
                MessageUtil.sendMessage(sender, CommonMessage.CMD_NO_CONSOLE_COMMAND.getMessage());
                return;
            }
            world = ((Player) sender).getWorld();
            xI--;
            yI--;
            zI--;
            yawI--;
            pitchI--;
        }

        // Everything specified or everything but yaw and pitch
        if (args.length == 8 || args.length == 6) {
            world = Bukkit.getWorld(args[worldI]);
            if (world == null) {
                MessageUtil.sendMessage(sender, IMessage.ERROR_NO_OBJECT.getMessage());
            }
        }

        // Everything specified (8) or everything but yaw and pitch (6), everything but world (7) or just x, y & z (5)
        if (args.length >= 5) {
            try {
                x = Double.parseDouble(args[xI]);
                y = Double.parseDouble(args[yI]);
                z = Double.parseDouble(args[zI]);
            } catch (NumberFormatException exception) {
                MessageUtil.sendMessage(sender, IMessage.ERROR_COORDS_NOT_NUMERIC.getMessage());
                return;
            }
        }

        // Everything specified or everything but world
        if (args.length >= 7) {
            try {
                yaw = Float.parseFloat(args[yawI]);
                pitch = Float.parseFloat(args[pitchI]);
            } catch (NumberFormatException exception) {
                MessageUtil.sendMessage(sender, IMessage.ERROR_COORDS_NOT_NUMERIC.getMessage());
                return;
            }
        }
        
        location = new Location(world, x, y, z, yaw, pitch);
        
        if (args.length == 2 && sender instanceof Player) {
            location = ((Player) sender).getTargetBlock(null, 20).getLocation().add(0, 1, 0);
        }
        
        mob.toEntity(location);
    }
    
}
