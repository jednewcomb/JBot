package me.maktoba.commands.general;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.commands.CommandRegistry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * This command creates an embed containing info on JBot's commands.
 */
public class HelpCommand extends Command {

    /**
     * Creates an instance of the HelpCommand.
     * @param jbot - Bot singleton to which the command is registered.
     */
    public HelpCommand(JBot jbot) {
        super(jbot);
        this.name = "help";
        this.description = "display information on JBot commands";
        this.type = "text";
        this.commandOptionData
                .add(new OptionData(OptionType.STRING, "type", "the type of command to see info for", true)
                .addChoice("general", "general")
                .addChoice("music", "music")
                .addChoice("moderation", "moderation")
                .addChoice("text", "text"));
    }

    /**
     * Create an embed containing command info based on command type.
     * @param event - Event trigger.
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