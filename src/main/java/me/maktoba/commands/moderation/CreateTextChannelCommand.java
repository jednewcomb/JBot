package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CreateTextChannelCommand extends Command {

    public CreateTextChannelCommand(JBot bot) {
        super(bot);
        this.name = "textchannel";
        this.description = "Creates a new text channel in the server";
        this.commandOptionData.add(new OptionData(OptionType.STRING, "channelname", "desired name for new channel", true));

        //this is showing all of the channels and not just the categories. maybe can change?
        this.commandOptionData.add(new OptionData(OptionType.CHANNEL, "category", "desired category for new channel", true));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (!guild.getBotRole().hasPermission(Permission.MANAGE_CHANNEL)) {
            event.reply("I do not have the correct role or permissions to use that command").queue();
        }

        if (!member.hasPermission(Permission.MANAGE_CHANNEL)) {
            event.reply("You do not have the correct role or permissions to use that command").queue();
        }

        String textChannelName = event.getOption("channelname").getAsString();
        String category = event.getOption("category").getAsString();

        //something needs to be fixed here, because nothing shows up if its already a name of a text channel
        if (guild.getTextChannels().toString().contains(textChannelName)) {
            event.reply("A text channel of that name already exists in this server").queue();
        }

        guild.createTextChannel(textChannelName, guild.getCategoryById(category)).queue();

        event.reply(member.getEffectiveName() + " created new text channel \"" + textChannelName + "\"").queue();
    }

}
