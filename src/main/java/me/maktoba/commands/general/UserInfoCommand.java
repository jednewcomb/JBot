package me.maktoba.commands.general;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
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
            event.replyEmbeds(buildEmbed(guild, user, member).build()).setEphemeral(true).queue();
        }
        else {
            event.reply("User not found in this server. Check for typos?")
                    .setEphemeral(true)
                    .queue();
        }
    }

    /**
     * Helper method to build Embed. User and Member are both used on the
     * same member as they each have useful methods associated with them.
     * @param guild - The Guild.
     * @param user - User in Guild
     * @param member - Member in Guild
     * @return a formatted Embed with all the information found above.
     */
    private EmbedBuilder buildEmbed(Guild guild, User user, Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String timeCreated = user.getTimeCreated().format(formatter);
        String timeJoined = member.getTimeJoined().format(formatter);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(user.getEffectiveName());
        builder.setImage(user.getEffectiveAvatarUrl());
        builder.addField(new MessageEmbed.Field("Joined Discord", timeCreated, true));
        builder.addField(new MessageEmbed.Field("Joined " + guild.getName(), timeJoined, true));

        return builder;
    }
}