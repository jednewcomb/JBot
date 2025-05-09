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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BanCommand extends Command {

    public BanCommand(JBot bot) {
        super(bot);
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

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        User target = Objects.requireNonNull(event.getOption("user")).getAsUser();
        Guild guild = event.getGuild();

        // check that command user isn't banning themselves
        Member member = event.getMember();
        if (Objects.requireNonNull(member).getUser() == target) {
            event.reply("You can't ban yourself, even if you really want to.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        // check that ban target is in guild
        Member targetMember = Objects.requireNonNull(event.getOption("user")).getAsMember();
        if (!Objects.requireNonNull(event.getGuild()).getMembers().contains(targetMember)) {
            event.reply("User not found in server.").setEphemeral(true).queue();
            return;
        }

        int numDays = 7;
        String reason = Objects.requireNonNull(event.getOption("reason")).getAsString();
        String content = "You have been banned from " + event.getGuild().getName()
                       + " due to inappropriate conduct. Reason: " + reason;

        // Try to send a user a private message upon ban notifying them.
        target.openPrivateChannel()
                  .flatMap(channel -> channel.sendMessage(content))
                  .queue(/*success vs failure lambda code could go here?*/);

        Objects.requireNonNull(event.getGuild()).ban(target, numDays, TimeUnit.DAYS).queue();
        event.reply("User " + target.getGlobalName() + " was banned")
                .setEphemeral(true)
                .queue();
    }
}