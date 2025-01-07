package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Objects;

public class UnbanCommand extends Command {

    public UnbanCommand(JBot jbot) {
        super(jbot);
        this.name = "unban";
        this.description = "unban specified user";
        this.type = "moderation";
        this.requiredPermission = Permission.BAN_MEMBERS;//this might be right
        this.commandOptionData.add(new OptionData(OptionType.USER, "banned", "banned user", true));

    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        User bannedUser = Objects.requireNonNull(event.getOption("banned")).getAsUser();

        //eventually you should make sure that the user is banned in the first place

        Objects.requireNonNull(event.getGuild()).unban(bannedUser).queue();
        event.reply("User " + bannedUser.getGlobalName() + " was unbanned").queue();


        //you need to make sure that you are cacheing new users upon joining the guild
        //not just the ones who are already there upon initialization
    }
}
