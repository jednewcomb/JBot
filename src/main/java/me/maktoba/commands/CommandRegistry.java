package me.maktoba.commands;

import me.maktoba.JBot;
import me.maktoba.commands.music.PlayCommand;
import me.maktoba.commands.music.PauseCommand;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command Registry could be thought of as a "Command Manager".
 * It uses a List and Map to register the commands we create by adding an Event Listener to our Shard builder. It makes
 * adding commands and registering them much more simple, as the alternative would be to manually add each command's
 * data via its own listener to the shard builder, which makes for more unsightly and confusing code.
 */
public class CommandRegistry extends ListenerAdapter {

    public static List<Command> commandList = new ArrayList<>();
    public static Map<String, Command> commandMap = new HashMap<>();

    /**
     * Sends each of our Commands to be Mapped to the List and Map for later retrieval.
     * @param bot - Our original Bot.
     */
    public CommandRegistry(JBot bot) {
        mapCommand(new PlayCommand(bot),
                   new PauseCommand(bot));
    }

    private void mapCommand(Command... commands) {
        for (Command cmd : commands) {
            commandMap.put(cmd.name, cmd);
            commandList.add(cmd);
        }
    }

    /**
     * Once we have the name and description of our desired command, we can execute it.
     * @param event - The SlashCommand event.
     */
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Command cmd = commandMap.get(event.getName());
        cmd.execute(event);
    }

    /**
     * Generates CommandData for all commands on startup.
     *
     * @return - A list of CommandData to be used on guild start for registration.
     */

    public static List<CommandData> unpackCommandData() {
        List<CommandData> commandData = new ArrayList<>();
        for (Command command : commandList) {
            SlashCommandData slashCommand
                    = Commands.slash(command.name, command.description).addOptions(command.commandOptionData);

            if (!command.subCommands.isEmpty()) {
                commandData.add(slashCommand);
            }

            commandData.add(slashCommand);
        }

        return commandData;
    }

    /**
     * An Overridden method of the JDA that runs when the desired guild for the bot is finished setting up.
     * @param event - The signal to our bot that the desired Guild(server) is set up.
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(unpackCommandData()).queue();
    }

}
