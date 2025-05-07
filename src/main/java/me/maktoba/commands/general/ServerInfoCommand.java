package me.maktoba.commands.general;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;

/**
 * This command creates an embed containing info on the current server.
 */
public class ServerInfoCommand extends Command {
    static final Logger logger = LoggerFactory.getLogger(ServerInfoCommand.class);

    /**
     * Creates an instance of the ServerInfoCommand.
     * @param bot - Bot singleton to which the command is registered.
     */
    public ServerInfoCommand(JBot bot) {
        super(bot);
        this.name = "serverinfo";
        this.description = "Displays information about the server.";
        this.type = "general";
    }

    /**
     * Create an embed containing info on a desired user.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        String owner = guild.getOwner().getEffectiveName();
        String memberCount = String.valueOf(guild.getMemberCount());
        String createDate = guild.getTimeCreated()
                                 .format(DateTimeFormatter
                                 .ofPattern("yyyy-MM-dd"));

        event.replyEmbeds(buildEmbed(guild, owner, memberCount, createDate).build())
                .setEphemeral(true)
                .queue();
    }

    /**
     * Helper method to build Embed.
     * @param owner - Owner of the Guild.
     * @param memberCount - Member count of Guild.
     * @param createDate - Date of Guild creation.
     * @return a formatted Embed with all the information found above.
     */
    private EmbedBuilder buildEmbed(Guild guild, String owner, String memberCount, String createDate) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(guild.getName());
        builder.setImage(guild.getIconUrl());
        builder.setColor(guild.getOwner().getColor());

        builder.addField(new MessageEmbed.Field("Owner", owner, true));
        builder.addField(new MessageEmbed.Field("Members", memberCount, true));
        builder.addField(new MessageEmbed.Field("Created", createDate, false));
        return builder;
    }

}
