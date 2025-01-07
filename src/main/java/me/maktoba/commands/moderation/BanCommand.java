package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BanCommand extends Command {

    public BanCommand(JBot jbot) {
        super(jbot);
        this.name = "ban";
        this.description = "ban specified user";
        this.type = "moderation";
        this.requiredPermission = Permission.BAN_MEMBERS;
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
    //maybe that could be added in a moderation handler or something
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        User target = Objects.requireNonNull(event.getOption("user")).getAsUser();

        // check to make sure that the user isn't banning themselves
        Member member = event.getMember();
        if (Objects.requireNonNull(member).getUser() == target) {
            event.reply("You can't ban yourself, even if you really want to.").queue();
            return;
        }

        //lets just ban them for 7 days for now, can give it more options/config later
        int numDays = 7;
        //some timer here for a moderation handler based on numDays
        //ban timer might be good in a moderation handler
        //unban could also be handled there

        String reason = Objects.requireNonNull(event.getOption("reason")).getAsString();
        String content = "You have been banned due to inappropriate conduct. Reason: " + reason;

        target.openPrivateChannel()
              .flatMap(channel -> channel.sendMessage(content))
              .queue();

        Objects.requireNonNull(event.getGuild()).ban(target, numDays, TimeUnit.DAYS).queue();
        event.reply("User " + target + " was banned").queue();
        // A bot can grant roles to other users that are of a lower position than its own highest role.
        // A bot can edit roles of a lower position than its highest role, but it can only grant permissions it has to those roles.
        // A bot can only sort roles lower than its highest role.
        // A bot can only kick,      ban,     and edit nicknames for users whose highest role is lower than the bot's highest role.
    }
}