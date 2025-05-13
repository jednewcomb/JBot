package me.maktoba.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class GuildUtil {

    public static TextChannel getDefaultTextChannel(Guild guild) {
        if (guild.getSystemChannel() != null && guild.getSystemChannel().canTalk()) {
            return guild.getSystemChannel();
        }

        return guild.getTextChannels().stream()
                .filter(TextChannel::canTalk)
                .findFirst()
                .orElse(null);
    }
}