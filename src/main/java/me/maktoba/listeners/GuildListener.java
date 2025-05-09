package me.maktoba.listeners;


import me.maktoba.handlers.ModerationHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildListener extends ListenerAdapter {

    ModerationHandler modHandler = new ModerationHandler();

    //TODO:
//     * There are a few things we want to do when we join the guild. Real Simple.
//     * - We want to check for a JBotCommand text channel, if there isn't one, create it
//     * - We want to check for a JBotMusic text channel, if there isn't one, create it.
//     *
//     * - Maybe we need to figure out a way to send a message to the guild owner
//     * @param event
//     */
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event){}

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String user = event.getUser().getName();
        Member newMember = event.getMember();
        modHandler.setDefaultNickname(newMember);

        String welcome = user + " has just joined " + event.getGuild().getName() + ". Welcome!";

        event.getGuild().getDefaultChannel().asTextChannel().sendMessage(welcome).queue();
    }

    private void giveNickname(GuildMemberJoinEvent event) {
        Member newMember = event.getMember();
        newMember.modifyNickname(newMember.getEffectiveName()).queue();
    }
}