package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.ModerationHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Objects;

public class BanCommand extends Command {

    ModerationHandler handler = new ModerationHandler();

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

    /**
     *
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        User target = event.getOption("user").getAsUser();

        // check that command user isn't banning themselves
        Member member = event.getMember();
        if (Objects.requireNonNull(member).getUser() == target) {
            event.reply("You can't ban yourself, even if you really want to.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        Guild guild = event.getGuild();
        Member targetMember = Objects.requireNonNull(event.getOption("user")).getAsMember();
        if (!Objects.requireNonNull(guild).getMembers().contains(targetMember)) {
            handler.handleNotFound(event, guild, targetMember);
            return;
        }

        handler.carryOutBan(event, guild, target);
    }
}