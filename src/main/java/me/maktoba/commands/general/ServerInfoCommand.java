package me.maktoba.commands.general;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ServerInfoCommand extends Command {

    public ServerInfoCommand(JBot jbot) {
        super(jbot);
        this.name = "serverinfo";
        this.description = "Displays information about the server";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        //with this and userinfocommand, maybe formatting a string by looping through the lists would
        //be nicer for formatting. like for sure it would. do it. you should do it. do it now!

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
