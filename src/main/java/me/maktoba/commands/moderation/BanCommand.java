package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.TimeUnit;

public class BanCommand extends Command {

    public BanCommand(JBot jbot) {
        super(jbot);
        this.name = "ban";
        this.description = "ban specified user";
        this.type = "moderation";
        this.botPermissions = Permission.BAN_MEMBERS; // not sure if i need this? maybe it should be a list the whole bot shares
        this.commandOptionData.add(new OptionData(OptionType.USER, "user", "user to ban", true));
        this.commandOptionData.add(new OptionData(OptionType.STRING, "reason", "reason to ban user", true)
                .addChoice("hate-speech", "hate-speech")
                .addChoice("spam", "spam")
                .addChoice("harassment", "harassment")
                .addChoice("trolling", "trolling")
                .addChoice("doxxing", "doxxing")
                .addChoice("other inappropriate conduct", "inappropriate"));
    }

    //think about maybe adding a timer or something to bans that aren't permas.
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        User toBan = event.getOption("user").getAsUser();

        // I think we need to add more checks in here to ensure that we're not banning someone with higher
        // permissions than the command user or bot has. PER THE DOCUMENTATION:

        // A bot can grant roles to other users that are of a lower position than its own highest role.
        // A bot can edit roles of a lower position than its highest role, but it can only grant permissions it has to those roles.
        // A bot can only sort roles lower than its highest role.
        // A bot can only kick, ban, and edit nicknames for users whose highest role is lower than the bot's highest role.

        // Otherwise, permissions do not obey the role hierarchy. For example, a user has two roles: A and B. A denies
        // the VIEW_CHANNEL permission on a #coolstuff channel. B allows the VIEW_CHANNEL permission on the same
        // #coolstuff channel. The user would ultimately be able to view the #coolstuff channel, regardless of the role positions.

        // check to make sure that the user isn't banning themselves
        if (member.getUser() == toBan) {
            event.reply("You can't ban yourself, even if you really want to.").queue();
            return;
        }

        //check to see if the bot has the correct permissions:
        if (botPermissions != Permission.BAN_MEMBERS) {
            event.reply("I lack the correct permissions to use this command").queue();
        }

        // check to make sure the member using the command
        if (member.getPermissions().contains(Permission.BAN_MEMBERS)) {

            //send a private message to the user to tell them why they've been banned. <- giving error right now
            toBan.openPrivateChannel().queue((channel) -> {
                channel.sendMessage("You have been banned because of " + event.getOption("reason")).queue();
            });

            guild.ban(toBan,7, TimeUnit.DAYS).queue();
            event.reply("User " + toBan.getGlobalName() + " was banned").queue();
        }
        else {
            event.reply("You lack the required permissions to use that command.").queue();
        }
    }
}