package me.maktoba.listeners;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();
        String jumpLink = event.getJumpUrl();

        String message = user.getName() + " reacted to a message with " + emoji
<<<<<<< HEAD
                + " in the " + channelMention + " channel!";
=======
                                        + " in the " + channelMention + " channel!";
>>>>>>> 2a96f7afc68743454c462ea2565c2dbf076ba998

        event.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage(message).queue();

    }
}
