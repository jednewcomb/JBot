package me.maktoba.commands.text;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * Reverse just takes a user given string and reverses it, kind of niche.
 */
public class ReverseCommand extends Command {

    /**
     * Creates an instance of ReverseCommand.
     * @param bot - Bot singleton.
     */
    public ReverseCommand(JBot bot) {
        super(bot);
        this.name = "reverse";
        this.description = "reverse the given text";
        this.type = "text";
        this.commandOptionData.add(new OptionData(OptionType.STRING, "text", "text to be reversed", true));

    }

    /**
     * Take the user given string and reverse it.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String text = event.getOption("text").getAsString();
        StringBuilder sb = new StringBuilder();

        for (int i = text.length() - 1; i >= 0; i--) {
            sb.append(text.charAt(i));
        }

        EmbedBuilder builder = EmbedUtil.createSuccessEmbed(sb.toString());
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }
}