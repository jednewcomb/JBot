package me.maktoba;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashCommandListener extends ListenerAdapter {
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

            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }
}