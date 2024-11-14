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

public class UserInfoCommand extends Command {

    public UserInfoCommand(JBot jbot) {
        super(jbot);
        this.name = "userinfo";
        this.description = "Displays information about a given user";
        this.commandOptionData.add(new OptionData(OptionType.USER, "user", "user to display info for", true));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        //Probably need to check permissions because we might not want everyone to have access to this command?

        //A user should be able to display their own info, if their curious? mayhaps?

        //Will want to check if guild even has the person. If not, jsut display "not found" ro something

        Guild guild = event.getGuild();

        //User and Member both have different methods associated with them. So I guess we just use both?
        User user = event.getOption("user").getAsUser();
        Member member = event.getOption("user").getAsMember();

        if (guild.isMember(user)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd, yyyy");

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(user.getGlobalName());
            builder.setDescription("Joined Discord: " + user.getTimeCreated().format(formatter) +
                                 "\nJoined " + guild.getName() + ": " + member.getTimeJoined().format(formatter) +
                                   "\n" + member.getRoles()); //this will require formatting

            builder.setImage(user.getEffectiveAvatarUrl()); //YOU CAN PUT THIS LOGIC IN NOWPLAYINGCOMMAND MAYBE?

            event.replyEmbeds(builder.build()).queue();
        }
        else {
            event.reply("User not found in this server. Check for typos?").queue();
        }
    }
}
