package me.maktoba.listeners;

import me.maktoba.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.jetbrains.annotations.NotNull;

/**
 * Listener which carries out some other tasks upon events
 * related to moderation.
 */
public class ModerationListener extends ListenerAdapter {

    /**
     * Mod log info on guild bans when they occur.
     * @param event - Event trigger.
     */
    public void onGuildBan(@NotNull GuildBanEvent event) {
        EmbedBuilder embed = EmbedUtil.createDefaultEmbed()
                .setTitle("🔨 User Banned")
                .setDescription(event.getUser().getAsMention() + " has been banned from the server.")
                .setFooter("User ID: " + event.getUser().getId(), event.getUser().getEffectiveAvatarUrl());

        sendModLog(event.getGuild().getSystemChannel(), embed);
    }

    /**
     * Mod log info on guild unbans when they occur.
     * @param event - Event trigger.
     */
    @Override
    public void onGuildUnban(@NotNull GuildUnbanEvent event) {
        EmbedBuilder embed = EmbedUtil.createDefaultEmbed()
                .setTitle("⚖️ User Unbanned")
                .setDescription(event.getUser().getAsMention() + " has been unbanned from the server.")
                .setFooter("User ID: " + event.getUser().getId(), event.getUser().getEffectiveAvatarUrl());

        sendModLog(event.getGuild().getSystemChannel(), embed);
    }

    /**
     * React to someone deleting a message. Honestly not sure if this is even needed.
     * @param event - Event trigger.
     */
    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        EmbedBuilder embed = EmbedUtil.createDefaultEmbed()
                .setTitle("🗑️ Message Deleted")
                .setDescription("A message was deleted in " + event.getChannel().getAsMention() + ".")
                .addField("Message ID", event.getMessageId(), false);

        sendModLog(event.getGuild().getSystemChannel(), embed);
    }

    /**
     * Send an embed to channel from which the event came from.
     * @param channel - The channel in which the event was triggered.
     * @param embed - A formatted message embed.
     */
    private void sendModLog(MessageChannel channel, EmbedBuilder embed) {
        if (channel != null && channel.canTalk()) {
            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }
}