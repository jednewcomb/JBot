package me.maktoba;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.io.IOException;
import java.util.Collections;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Main {

    public static void main(String[] args) throws IOException {
        JDA jda = JDABuilder
                  .createLight("token", Collections.emptyList())
                  .addEventListeners(new SlashCommandListener())
                  .build();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
            Commands.slash("say", "Makes the bot say what you tell it to")
                    .addOption(STRING, "content", "What the bot should say", true),
            Commands.slash("sarcasm", "capitalizes every other letter")
                    .addOption(STRING, "change", "The string to mutate", true)
        ).queue();

        jda.getRestPing().queue(ping ->
                System.out.println("Logged in with ping: " + ping)
        );

    }

}