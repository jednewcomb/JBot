package me.maktoba.handlers;

import me.maktoba.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * ModerationHandler serves as a helper class which handles a few moderation tasks
 * so as to not overcomplicate the commands themselves.
 */
public class ModerationHandler {

    private static final String NOT_FOUND = "User %s not found in server %s.";
    private static final String GUILD_OUT = "User %s was banned.";
    private static final String USER_OUT = "You have been banned from %s due to inappropriate conduct. Reason: %s.";
    private static final Logger logger = LoggerFactory.getLogger(ModerationHandler.class);

    public ModerationHandler() {}

    /**
     * We set the new user nickname to their EffectiveName on startup to avoid issues
     * with null nicknames in NicknameCommand.
     * @param member - The member whose name we're modifying.
     */
    public void setDefaultNickname(Member member) {
        member.modifyNickname(member.getEffectiveName()).queue();
    }

    /**
     * Sends embed if the member wasn't found.
     * @param event - Event Trigger.
     * @param guild - Guild where event occurred.
     * @param targetMember - Member to ban.
     */
    public void handleMemberNotFound(SlashCommandInteractionEvent event, Guild guild, Member targetMember) {
        EmbedBuilder builder = EmbedUtil.createErrorEmbed(
                String.format(
                        NOT_FOUND, targetMember, guild.getName()
                )
        );
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }

    /**
     * Ban the user, try to send them a message detailing why they'r banned, acknowledge in channel.
     * @param event - Event Trigger.
     * @param guild - Guild where event occurred.
     * @param target - User to ban.
     */
    public void carryOutBan(SlashCommandInteractionEvent event, Guild guild, User target) {
        attemptMessageBannedUser(event, guild.getName(), target);
        Objects.requireNonNull(guild).ban(target, 7, TimeUnit.DAYS).queue();

        EmbedBuilder builder = EmbedUtil.createSuccessEmbed(
                String.format(
                    GUILD_OUT, target.getEffectiveName()
                )
        );

        event.replyEmbeds(builder.build()).queue();
    }

    /**
     * This isn't always going to work because there are many variables deciding if
     * JBot can send a message to a user or not.
     * @param event - Event Trigger.
     * @param guildName - name of guild where event occurred.
     * @param target - User to ban.
     */
    private void attemptMessageBannedUser(SlashCommandInteractionEvent event, String guildName, User target) {
        String reason = event.getOption("reason").getAsString();
        String content = String.format(USER_OUT, guildName, reason);

        try {
            target.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessage(content))
                    .queue();
        } catch (NullPointerException e) {
            logger.info("Could not private message the user.");
        }
    }
}