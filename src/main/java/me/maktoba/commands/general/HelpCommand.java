package me.maktoba.commands.general;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.commands.CommandRegistry;
import me.maktoba.util.EmbedUtil;
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
     * @param bot - Bot singleton to which the command is registered.
     */
    public HelpCommand(JBot bot) {
        super(bot);
        this.name = "help";
        this.description = "display information on JBot commands.";
        this.type = "general";
        this.commandOptionData
                .add(new OptionData(OptionType.STRING, "type", "the type of command to see info for", true)
                .addChoice("general", "general")
                .addChoice("music", "music")
                .addChoice("moderation", "moderation")
                .addChoice("text", "text"));
    }

    /**
     * Create an Embed containing command info based on command type.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String choice = event.getOption("type").getAsString();
        choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);

        EmbedBuilder builder = EmbedUtil.createSuccessEmbed(choice + " Commands");

        for (Command cmd : CommandRegistry.commandList) {
            if (cmd.type.equals(choice.toLowerCase())) {
                builder.addField("/" + cmd.name, cmd.description, false);
            }
        }

        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }
}