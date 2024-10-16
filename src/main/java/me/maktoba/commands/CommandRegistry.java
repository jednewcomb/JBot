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
 * The CommandRegistry class is responsible for managing and registering slash commands
 * for the Discord bot using JDA. It maintains a list and map of commands, allowing for easy retrieval
 * and execution based on user interactions. This class simplifies the process of adding commands to
 * the bot and ensures that commands are registered on guild startup.
 */
public class CommandRegistry extends ListenerAdapter {

    public static List<Command> commandList = new ArrayList<>();
    public static Map<String, Command> commandMap = new HashMap<>();

    /**
     * Sends each of our Commands to be Mapped to the List and Map for later retrieval.
     *
     * @param bot - Our original Bot.
     */
    public CommandRegistry(JBot bot) {
        mapCommand(new PlayCommand(bot),
                   new PauseCommand(bot));
    }

    /**
     * Maps the given commands to the commandList and commandMap for easy retrieval
     * and execution. The command's name is used as the key in the map.
     *
     * @param commands the commands to be mapped and registered
     */
    private void mapCommand(Command... commands) {
        for (Command cmd : commands) {
            commandMap.put(cmd.name, cmd);
            commandList.add(cmd);
        }
    }

    /**
     * Once we have the name and description of our desired command, we can execute it.
     *
     * @param event - The SlashCommand event.
     */
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Command cmd = commandMap.get(event.getName());
        cmd.execute(event);
    }

    /**
     * Generates a list of CommandData for all registered commands. This data is used to register
     * the commands with the Discord API when the bot starts or when a guild is ready.
     *
     * @return a list of CommandData for the commands, which will be registered on guild startup
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
     * Called when a guild is ready to process commands. This method registers the bot commands
     * with the guild once the guild has finished its setup.
     *
     * @param event the GuildReadyEvent signaling that the guild is ready for command registration
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(unpackCommandData()).queue();
    }

}
