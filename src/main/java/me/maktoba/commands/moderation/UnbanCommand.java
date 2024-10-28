package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class UnbanCommand extends Command {

    public UnbanCommand(JBot jbot) {
        super(jbot);
        this.name = "unban";
        this.description = "unban user";
        this.type = "moderation";
        this.commandOptionData.add(new OptionData(OptionType.USER, "user", "user to unban", true));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member toUnban = event.getOption("user").getAsMember();
        guild.unban(toUnban);
    }
}
