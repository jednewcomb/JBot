package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Objects;

public class NicknameCommand extends Command {

    public NicknameCommand(JBot bot) {
        super(bot);
        this.name = "nickname";
        this.description = "give specified user a nickname for this server";
        this.type = "moderation";
        this.requiredPermission = Permission.NICKNAME_MANAGE;
        this.commandOptionData.add(new OptionData(OptionType.USER, "user", "user's name or current nickname to change", true));
        this.commandOptionData.add(new OptionData(OptionType.STRING, "new", "desired new nick name", true));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = Objects.requireNonNull(event.getOption("user")).getAsMember();
        String newNickName = Objects.requireNonNull(event.getOption("new")).getAsString();
        // to avoid null nicknames, which could cause problems in the future,
        // maybe we just assign everyones nickname as their effectiveName(member) on guild start up

        member.modifyNickname(newNickName).queue();
        event.reply(member.getEffectiveName() + "'s nickname was changed to " + newNickName).setEphemeral(true).queue();
    }

}