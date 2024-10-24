package me.maktoba.commands.text;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * Reverse just takes a user given string and reverses it, for the very niche
 *
 */
public class ReverseCommand extends Command {

    public ReverseCommand(JBot jbot) {
        super(jbot);
        this.name = "reverse";
        this.description = "reverse the given text";
        //maybe I want this to not require text and if the given text is null I just ping the user?
        this.commandOptionData.add(new OptionData(OptionType.STRING, "text", "text to be reversed", true));
    }

    /**
     * Take the user given string and reverse it
     * @param event
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String text = event.getOption("text").getAsString();
        StringBuilder sb = new StringBuilder();

        for (int i = text.length() - 1; i >= 0; i--) {
            sb.append(text.charAt(i));
        }

        event.reply(sb.toString()).queue();

    }
}
