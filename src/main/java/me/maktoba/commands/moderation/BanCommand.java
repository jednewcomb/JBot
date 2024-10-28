package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
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
    }

    //would be cool if we could get some funny photo to put in channel the command was used
    //like "bye felicia" or something
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member toBan = event.getOption("user").getAsMember();

        toBan.ban(7, TimeUnit.DAYS);

        event.reply("User" + toBan + "was banned from " + event.getGuild().getName());
    }
}
