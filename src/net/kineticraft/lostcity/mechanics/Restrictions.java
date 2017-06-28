package net.kineticraft.lostcity.mechanics;

import net.kineticraft.lostcity.Core;
import net.kineticraft.lostcity.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.stream.Collectors;

/**
 * Restrictions - Contains basic restrictions / alerts about slightly questionable player behaviour.
 *
 * Created by Kneesnap on 6/2/2017.
 */
public class Restrictions extends Mechanic {

    private static String[] DISABLE_TEXT = new String[] { // This text tells certain mods they should disable some of their features.
            "§3 §9 §2 §0 §0 §1 §3 §9 §2 §0 §0 §2 §3 §9 §2 §0 §0 §3 ",
            "§3 §6 §3 §6 §3 §6 §e §3 §6 §3 §6 §3 §6 §d ",
            "§0§0§1§f§e §0§0§2§f§e §0§0§3§4§5§6§7§8§f§e ",
            "§f §f §4 §0 §9 §6 §f §f §1 §0 §2 §4 §f §f §2 §0 §4 §8 "};

    @EventHandler
    public void onChannelRegister(PlayerRegisterChannelEvent evt) {
        if (!evt.getChannel().equalsIgnoreCase("WDL|INIT"))
            return;
        Core.alertStaff(evt.getPlayer().getName() + " was kicked for using World Downloader!");
        evt.getPlayer().kickPlayer(ChatColor.RED + "Please disable World Downloader.");
    }

    @EventHandler
    public void onPlayerTabComplete(PlayerChatTabCompleteEvent evt) {
        if (!"1234567890abcdefghijklmnopqrstuvwxyz".contains(evt.getChatMessage().substring(0, 1).toLowerCase()))
            Core.alertStaff(ChatColor.RED + "[" + evt.getPlayer().getName() + "]" + ChatColor.GRAY + ": " + evt.getChatMessage());
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent evt) {
        if (evt.getVehicle().getType() == EntityType.BOAT && !evt.getFrom().getBlock().isLiquid()
                && evt.getTo().getY() > evt.getFrom().getY() && evt.getVehicle().getVelocity().length() == 0)
            Core.alertStaff(evt.getVehicle().getPassengers().stream().filter(e -> e instanceof Player)
                    .map(Entity::getName).collect(Collectors.joining(", ", "[BoatFly] " + ChatColor.GRAY,
                            ChatColor.GRAY + "may be using BoatFly.")));
    }

    @Override // Removes all infinite potion effects. (Players aren't supposed to keep them.)
    public void onQuit(Player player) {
        player.getActivePotionEffects().stream().map(PotionEffect::getType)
                .forEach(t -> Utils.removeInfinitePotion(player, t));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent evt) {
        if (evt.getBlock().getType() == Material.DIAMOND_ORE)
            Core.alertStaff(ChatColor.BLUE + evt.getPlayer().getName() + " mined some "
                    + ChatColor.AQUA + "diamond ore" + ChatColor.BLUE + ".");
    }

    @Override
    public void onJoin(Player player) {
        player.sendMessage(DISABLE_TEXT);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent evt) {
        evt.getPlayer().sendMessage(DISABLE_TEXT);
    }
}