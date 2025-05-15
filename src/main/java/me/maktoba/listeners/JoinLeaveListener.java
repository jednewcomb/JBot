package me.maktoba.listeners;

import me.maktoba.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;


/**
 * JoinLeaveListener handles events related to the bot joining or leaving a server.
 */
public class JoinLeaveListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(JoinLeaveListener.class);
    private static final String COMMAND_CHANNEL_NAME = "jbot-commands";
    private static final String MUSIC_CHANNEL_NAME = "jbot-music";

    /**
     * Upon joining a guild, create dedicated channels for music commands,
     * and non-music commands.
     * @param event - Event Trigger.
     */
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Guild guild = event.getGuild();

        // Create #jbot-commands if it doesn't exist
        if (!channelExists(guild, COMMAND_CHANNEL_NAME)) {
            createTextChannel(guild, COMMAND_CHANNEL_NAME, "Use this channel for JBot commands.");
        }

        // Create #jbot-music if it doesn't exist
        if (!channelExists(guild, MUSIC_CHANNEL_NAME)) {
            createTextChannel(guild, MUSIC_CHANNEL_NAME, "This channel is used for music playback commands.");
        }

        // Attempt to DM the server owner
        sendOwnerWelcomeMessage(guild);
    }

    /**
     * Helper method to check if our dedicated channels are there or not.
     * @param guild - Guild where event was triggered.
     * @param name - Desired name for dedicated channels.
     * @return
     */
    private boolean channelExists(Guild guild, String name) {
        return guild.getTextChannelsByName(name, true).size() > 0;
    }

    /**
     * Create text channel for commands.
     * @param guild - Guild where event was triggered.
     * @param name - Desired name for dedicated channels.
     * @param topic - The topic for which the channel is originally created.
     */
    private void createTextChannel(Guild guild, String name, String topic) {
        guild.createTextChannel(name)
                .setTopic(topic)
                .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.MESSAGE_MANAGE))
                .queue();
    }

    /**
     * Try to send an embed as a dm to the owner of the guild to which JBot was invited.
     * @param guild - The guild JBot has joined.
     */
    private void sendOwnerWelcomeMessage(Guild guild) {
        Member guildOwner = guild.retrieveOwner().complete();
        if (guildOwner == null) {
            logger.info("Could not retrieve guild owner for " + guild.getName());
            return;
        }

        User owner = guildOwner.getUser();
        try {
            PrivateChannel privateChannel = owner.openPrivateChannel().complete();

            MessageEmbed welcomeEmbed = EmbedUtil.createDefaultEmbed()
                    .setTitle("🤖 Thanks for inviting JBot!")
                    .setDescription(
                            "JBot has successfully joined **" + guild.getName() + "**!\n\n" +
                                    "📌 I’ve created two channels for you:\n" +
                                    "`#jbot-commands` - for general bot commands\n" +
                                    "`#jbot-music` - for music control\n\n" +
                                    "You can rename or manage them as needed!\n\n" +
                                    "Use `/help` to get started."
                    ).build();

            privateChannel.sendMessageEmbeds(welcomeEmbed).queue();
        } catch (Exception e) {
            logger.info("Failed to send DM to server owner of " + guild.getName() + ": " + e.getMessage());
        }
    }
}