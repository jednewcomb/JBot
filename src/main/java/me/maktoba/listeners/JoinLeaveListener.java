package me.maktoba.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class JoinLeaveListener extends ListenerAdapter {

    //TODO:
//     * There are a few things we want to do when we join the guild. Real Simple.
//     * - We want to check for a JBotCommand text channel, if there isn't one, create it
//     * - We want to check for a JBotMusic text channel, if there isn't one, create it.
//     *
//     * - Maybe we need to figure out a way to send a message to the guild owner
//     * @param event
//     */
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        //Delete duplicate commands
        JDA jda = event.getJDA();

        Set<String> commandsToDelete = Set.of("sarcasm", "say");

        jda.retrieveCommands().queue(commands -> {
            for (Command command : commands) {
                if (commandsToDelete.contains(command.getName())) {
                    command.delete().queue(success ->
                            System.out.println("Deleted duplicate command: " + command.getName())
                    );
                }
            }
        });
    }

}
