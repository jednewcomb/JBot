package me.maktoba.listeners;


import me.maktoba.handlers.ModerationHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildListener extends ListenerAdapter {

    private final ModerationHandler modHandler = new ModerationHandler();


    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String user = event.getUser().getName();
        Member newMember = event.getMember();
        modHandler.setDefaultNickname(newMember);

        String welcome = user + " has just joined " + event.getGuild().getName() + ". Welcome!";

        event.getGuild().getDefaultChannel().asTextChannel().sendMessage(welcome).queue();
    }


    private void onGuildMemberLeave(@NotNull GuildMemberRemoveEvent event) {

    }
}