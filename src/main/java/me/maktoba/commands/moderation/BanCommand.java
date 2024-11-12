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
        this.botPermissions = Permission.BAN_MEMBERS;
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

        // check to make sure that the user isn't banning themselves
        if (member.getUser() == toBan) {
            event.reply("You can't ban yourself, even if you really want to.").queue();
            return;
        }

        // check to make sure the member using the command and the bot have the correct permissions/roles
        if (member.getPermissions().contains(Permission.BAN_MEMBERS) && botPermissions == Permission.BAN_MEMBERS) {

            //send a private message to the user to tell them why they've been banned. <- giving error right now
            toBan.openPrivateChannel().queue((channel) -> {
                channel.sendMessage("You have been banned because of " + event.getOption("reason")).queue();
            });

            guild.ban(toBan,7, TimeUnit.DAYS).queue();
            event.reply("User " + toBan.getGlobalName() + " was banned").queue();
        }
        else {
            event.reply("You or JBot lack the required permissions to use that command.").queue();
        }
    }
}