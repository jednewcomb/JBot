package me.maktoba.commands.text;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.commands.CommandRegistry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class HelpCommand extends Command {

    public HelpCommand(JBot jbot) {
        super(jbot);
        this.name = "help";
        this.description = "display information on JBot commands";
        this.type = "text";
        this.commandOptionData
                .add(new OptionData(OptionType.STRING, "type", "the type of command to see info for", true)
                .addChoice("text", "text")
                .addChoice("music", "music"));
    }

    /**
     * @param event
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String choice = event.getOption("type").getAsString();
        choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(choice + " Commands");

        for (Command cmd : CommandRegistry.commandList) {
            if (cmd.type.equals(choice.toLowerCase())) {
                embed.addField("/" + cmd.name, cmd.description, false);
            }
        }

        event.replyEmbeds(embed.build()).queue();
    }
}