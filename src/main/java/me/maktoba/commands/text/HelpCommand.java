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
        this.commandOptionData.add(new OptionData
                (OptionType.STRING, "choice", "the type of command to see info for", true)
                .addChoice("text", "text")
                .addChoice("music", "music"));
    }


    /**
     * @param event
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String choice = event.getOption("choice").getAsString();

        //this is so ugly I've got to find a better way. Probably like pop up list window or something
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(choice + " Commands");

        for (Command cmd : CommandRegistry.commandList) {
            if (cmd.type.equals(choice)) {
                embed.addField("/" + cmd.name, cmd.description, false);
            }
        }

        event.replyEmbeds(embed.build()).queue();
    }
}
