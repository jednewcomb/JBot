package me.maktoba.util;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.TimeUnit;

public class MessageUtil {

    public static void sendTempMessage(TextChannel channel, String content, long deleteDelayMillis) {
        channel.sendMessage(content).queue(message ->
                message.delete().queueAfter(deleteDelayMillis, TimeUnit.MILLISECONDS));
    }

    public static boolean isFromBot(Message message) {
        return message.getAuthor().isBot();
    }
}