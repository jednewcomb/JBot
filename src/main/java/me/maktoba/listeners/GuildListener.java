package me.maktoba.listeners;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String user = event.getUser().getName();

        String welcome = user + " has just joined " + event.getGuild().getName() + ". Welcome!";

        event.getGuild().getDefaultChannel().asTextChannel().sendMessage(welcome).queue();
    }
}
