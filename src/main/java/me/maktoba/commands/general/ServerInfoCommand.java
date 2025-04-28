package me.maktoba.commands.general;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * This command creates an embed containing info on the current server.
 */
public class ServerInfoCommand extends Command {

    /**
     * Creatres an instance of the ServerInfoCommand.
     * @param jbot - Bot singleton to which the command is registered.
     */
    public ServerInfoCommand(JBot jbot) {
        super(jbot);
        this.name = "serverinfo";
        this.description = "Displays information about the server";
    }

    /**
     * Create an embed containing info on a desired user.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        //TODO with this and userinfocommand, maybe formatting a string by looping through the lists would
        //TODO be nicer for formatting. like for sure it would. do it. you should do it. do it now!

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(guild.getName());
        embed.setDescription("Members: " + guild.getMemberCount() +"\n"+
                             "Channels: " + guild.getChannels() + "\n" +
                             "Creator: " + guild.getOwnerId() + "\n" +
                             "Created: " + guild.getTimeCreated());
        embed.setImage(guild.getBannerUrl());

        event.replyEmbeds(embed.build()).queue();
    }
}
