package me.maktoba.listeners;

import me.maktoba.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Just a cute little Listener that acknowledges message reactions in channels.
 */
public class MessageReactionListener extends ListenerAdapter {

    /**
     * Provided that the user isn't a bot, make an embed that reacts to user's
     * "reacting" to other users messages in text channels.
     * @param event - Event trigger.
     */
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        if (user == null || user.isBot()) return;

        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();
        String jumpLink = event.getJumpUrl();

        EmbedBuilder embed = EmbedUtil.createDefaultEmbed()
                .setTitle("📌 New Reaction")
                .setDescription(user.getAsMention() + " reacted with **" + emoji + "** in " + channelMention)
                .addField("Jump to Message", "[Click here](" + jumpLink + ")", false)
                .setFooter("User: " + user.getAsTag(), user.getEffectiveAvatarUrl());

        StandardGuildMessageChannel defaultChannel = (StandardGuildMessageChannel) event.getGuild().getDefaultChannel();
        if (defaultChannel != null && defaultChannel.canTalk()) {
            defaultChannel.sendMessageEmbeds(embed.build()).queue();
        }
    }
}