package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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
        this.commandOptionData.add(new OptionData(OptionType.USER, "user", "user to ban", true));
        this.commandOptionData.add(new OptionData(OptionType.STRING, "reason", "reason to ban user", true)
                .addChoice("hate-speech", "hate-speech")
                .addChoice("spam", "spam")
                .addChoice("harassment", "harassment")
                .addChoice("inappropriate conduct", "inappropriate")
                .addChoice("trolling", "trolling")
                .addChoice("doxxing", "doxxing"));
        this.botPermissions = Permission.BAN_MEMBERS;
    }

    //would be cool if we could get some funny photo to put in channel the command was used
    //like "bye felicia" or something
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        //I think we need to check for permissions

        Member member = event.getMember();
        Guild guild = event.getGuild();

        if (member.getPermissions().contains(Permission.BAN_MEMBERS) && botPermissions == Permission.BAN_MEMBERS) {

            guild.ban(event.getOption("user").getAsUser(), 7, TimeUnit.DAYS).queue();
            event.reply("banned").queue();
        }
    }
}