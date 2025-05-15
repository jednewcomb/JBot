package me.maktoba.listeners;

import me.maktoba.handlers.ModerationHandler;
import me.maktoba.util.EmbedUtil;
import me.maktoba.util.GuildUtil;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * GuildListener adds functionality related to certain events that take place
 * in the server.
 */
public class GuildListener extends ListenerAdapter {

    private final ModerationHandler modHandler = new ModerationHandler();

    /**
     * Reacts to new users joining the guild with a welcoming message. WHOLESOME!
     * @param event - Event Trigger.
     */
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Member member = event.getMember();
        modHandler.setDefaultNickname(member);

        String welcomeMsg = "🎉 **" + member.getEffectiveName() + "** has joined **" + event.getGuild().getName() + "**. Welcome!";
        sendToDefaultChannel(event.getGuild(), "👋 Welcome!", welcomeMsg);
    }

    /**
     * Reacts to users leaving the guild with a farewell message.
     * @param event - Event Trigger.
     */
    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        String goodbyeMsg = "😢 **" + event.getUser().getName() + "** has left the server.";
        sendToDefaultChannel(event.getGuild(), "Goodbye!", goodbyeMsg);
    }

    /**
     * Upon JBot joining a guild, thank the user for inviting!
     * @param event - Event Trigger.
     */
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        String botName = event.getJDA().getSelfUser().getName();
        String message = "Thanks for inviting **" + botName + "** to **" + event.getGuild().getName() + "**!";
        sendToDefaultChannel(event.getGuild(), "🤖 JBot has arrived!", message);
    }

    /**
     * Acknowledge someone's nickname change.
     * @param event - Event Trigger.
     */
    @Override
    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event) {
        String oldNick = event.getOldNickname() == null ? event.getUser().getName() : event.getOldNickname();
        String newNick = event.getNewNickname() == null ? event.getUser().getName() : event.getNewNickname();

        String msg = "✏️ **" + oldNick + "** changed their nickname to **" + newNick + "**.";
        sendToDefaultChannel(event.getGuild(), "Nickname Changed", msg);
    }

    /**
     * Acknowledge server boost.
     * @param event - Event Trigger.
     */
    @Override
    public void onGuildMemberUpdateBoostTime(@NotNull GuildMemberUpdateBoostTimeEvent event) {
        Member member = event.getMember();

        if (event.getNewTimeBoosted() != null) {
            sendToDefaultChannel(event.getGuild(), "🚀 Server Boosted!",
                    "**" + member.getEffectiveName() + "** just boosted the server! Thank you! ❤️");
        } else {
            sendToDefaultChannel(event.getGuild(), "💔 Boost Ended",
                    "**" + member.getEffectiveName() + "** has stopped boosting the server.");
        }
    }

    /**
     * Create embed to be sent to default channel.
     * @param guild - Guild which triggered event.
     * @param title - Embed title.
     * @param description - Description for embed.
     */
    private void sendToDefaultChannel(Guild guild, String title, String description) {
        TextChannel channel = GuildUtil.getDefaultTextChannel(guild);
        if (channel != null) {
            channel.sendMessageEmbeds(
                    EmbedUtil.createDefaultEmbed()
                            .setTitle(title)
                            .setDescription(description)
                            .build()
            ).queue();
        }
    }
}