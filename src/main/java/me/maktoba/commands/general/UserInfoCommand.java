package me.maktoba.commands.general;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.format.DateTimeFormatter;

/**
 * This command creates an embed containing info on a user-selected member of the current guild.
 */
public class UserInfoCommand extends Command {

    /**
     * Creates an instance of the UserInfoCommand.
     * @param bot - Bot singleton to which the command is registered.
     */
    public UserInfoCommand(JBot bot) {
        super(bot);
        this.name = "userinfo";
        this.description = "Displays information about a given user.";
        this.type = "general";
        this.commandOptionData.add(new OptionData(OptionType.USER, "user", "user to display info for", true));
    }

    /**
     * Create an embed containing info on the server which the command was made.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        User user = event.getOption("user").getAsUser();
        Member member = event.getOption("user").getAsMember();

        if (guild.isMember(user)) {
            event.replyEmbeds(buildSuccessEmbed(guild, user, member).build()).setEphemeral(true).queue();
        }
        else {
            event.replyEmbeds(buildErrorEmbed(member).build()).setEphemeral(true).queue();
        }
    }

    /**
     * Helper method to build Embed. User and Member are both used on the
     * same member as they each have useful methods associated with them.
     * @param guild - The Guild.
     * @param user - User in Guild.
     * @param member - Member in Guild.
     * @return a formatted Embed with all the information found above.
     */
    private EmbedBuilder buildSuccessEmbed(Guild guild, User user, Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String timeCreated = user.getTimeCreated().format(formatter);
        String timeJoined = member.getTimeJoined().format(formatter);

        return EmbedUtil.createSuccessEmbed(user.getEffectiveName())
                .setImage(user.getEffectiveAvatarUrl())
                .addField(new MessageEmbed.Field("Joined Discord", timeCreated, true))
                .addField(new MessageEmbed.Field("Joined " + guild.getName(), timeJoined, true));
    }

    /**
     * Build failure Embed if we find the Member isn't in the guild. This
     * is usually due to a discrepancy in caching because the command won't
     * allow for you to use it with Guild Members it already hasn't found.
     * @param member - Who we're searching for.
     * @return - Embed informing user the desired Member wasn't found.
     */
    private EmbedBuilder buildErrorEmbed(Member member) {
        return EmbedUtil.createErrorEmbed(
                String.format(
                    "User %s not found in this server.", member.getEffectiveName()
                )
        );
    }
}