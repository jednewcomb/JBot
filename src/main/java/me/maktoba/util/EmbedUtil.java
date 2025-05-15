package me.maktoba.util;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.Instant;

public class EmbedUtil {

    public static EmbedBuilder createDefaultEmbed() {
        return new EmbedBuilder()
                .setColor(Color.CYAN)
                .setFooter("JBot", null)
                .setTimestamp(Instant.now());
    }

    public static EmbedBuilder createErrorEmbed(String message) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("❌ Error")
                .setDescription(message);
    }

    public static EmbedBuilder createSuccessEmbed(String message) {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("✅ Success")
                .setDescription(message);
    }
}