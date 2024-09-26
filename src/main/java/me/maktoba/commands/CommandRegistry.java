package me.maktoba.commands;

import me.maktoba.JBot;
import me.maktoba.commands.music.PlayCommand;
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

public class CommandRegistry extends ListenerAdapter {

    public static List<Command> commandList = new ArrayList<>();
    public static Map<String, Command> commandMap = new HashMap<>();

    public CommandRegistry(JBot bot) {
        mapCommand(new PlayCommand(bot));
    }

    private void mapCommand(Command ...commands) {
        for (Command cmd : commands) {
            commandMap.put(cmd.name, cmd);
            commandList.add(cmd);
        }
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        //get command by name
        //execute command
        //might need to do more later based on permissions, intents and caches etc

        Command cmd = commandMap.get(event.getName());
        cmd.execute(event);
    }

    /**
     * Generates CommandData for all commands on startup.
     *
     * @return - A list of CommandData to be used on guild start for registration.
     */
    /*
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



    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(unpackCommandData()).queue();
    }

     */
}