package me.maktoba.commands.general;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.format.DateTimeFormatter;

/**
 * This command creates an embed containing info on a user-selected member of the current guild.
 */
public class UserInfoCommand extends Command {

    /**
     * Creates an instance of the UserInfoCommand.
     * @param jbot - Bot singleton to which the command is registered.
     */
    public UserInfoCommand(JBot jbot) {
        super(jbot);
        this.name = "userinfo";
        this.description = "Displays information about a given user";
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd, yyyy");

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(user.getGlobalName());
            builder.setDescription("Joined Discord: " + user.getTimeCreated().format(formatter) +
                                 "\nJoined " + guild.getName() + ": " + member.getTimeJoined().format(formatter) +
                                   "\n" + member.getRoles()); //this will require formatting

            builder.setImage(user.getEffectiveAvatarUrl());

            event.replyEmbeds(builder.build()).queue();
        }
        else {
            event.reply("User not found in this server. Check for typos?").queue();
        }
    }
}
