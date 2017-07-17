package net.kineticraft.lostcity.commands.player;

import net.kineticraft.lostcity.commands.PlayerCommand;
import net.kineticraft.lostcity.data.KCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Get a player's username from their nickname.
 *
 * Created by Kneesnap on 6/27/17.
 */
public class CommandRealName extends PlayerCommand {

    public CommandRealName() {
        super("<nick>", "Displays a player's real name.", "realname", "rn");
        autocomplete(p -> Bukkit.getOnlinePlayers().stream().map(KCPlayer::getWrapper)
                .map(KCPlayer::getNickname).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        String search = args[0].toLowerCase();
        List<String> matches = Bukkit.getOnlinePlayers().stream().map(KCPlayer::getWrapper)
                .filter(k -> k.getNickname() != null) // Verify they have a nickname.
                .filter(k -> ChatColor.stripColor(k.getNickname()).toLowerCase().contains(search)
                    || k.getUsername().toLowerCase().contains(search)).map(KCPlayer::getUsername).collect(Collectors.toList());

        sender.sendMessage(ChatColor.GRAY + "Matches: " + (matches.isEmpty() ? ChatColor.RED + "None"
                : matches.stream().collect(Collectors.joining(ChatColor.GRAY + ", " + ChatColor.GREEN,
                ChatColor.GREEN.toString(), ""))));
    }
}