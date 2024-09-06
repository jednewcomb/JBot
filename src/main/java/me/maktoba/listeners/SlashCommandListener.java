package me.maktoba.listeners;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;



public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("say", "Bot will say what you tell it to")
                   .addOption(OptionType.STRING, "content", "what bot says", true));

        commandData.add(Commands.slash("sarcasm", "Every other character is uppercase"));
        commandData.add(Commands.slash("spoiler", "Covers message with potential spoilers"));
        commandData.add(Commands.slash("reverse", "Reverses given text"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "say":
                String content = event.getOption("content", OptionMapping::getAsString);
                event.reply(content).queue();
                break;

            case "sarcasm":
                String inputString = event.getOption("change", OptionMapping::getAsString).toLowerCase();
                StringBuilder toReturn = new StringBuilder();

                for (int i = 0; i < inputString.length(); i++) {
                    toReturn.append(i % 2 == 0
                                    ? inputString.charAt(i)
                                    : Character.toUpperCase(inputString.charAt(i)));
                }

                event.reply(toReturn.toString()).queue();
                break;

            case "spoiler":
                String string = event.getOption("content", OptionMapping::getAsString);
                event.reply("||" + string + "||").queue();

            case "reverse":
                String strung = event.getOption("content", OptionMapping::getAsString);

                String out = "";
                for (int i = strung.length() - 1 ; i >= 0; i--) {
                    out += strung.charAt(i);
                }

                event.reply(out).queue();

            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }
}