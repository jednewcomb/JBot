package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Objects;

public class CreateChannelCommand extends Command {

    public CreateChannelCommand(JBot bot) {
        super(bot);
        this.name = "newchannel";
        this.description = "Creates a new channel in the server in the desired subcategory, or the default one";
        this.requiredPermission = Permission.MANAGE_CHANNEL;
        this.commandOptionData.add(new OptionData(OptionType.STRING, "channelname", "desired name for new channel", true));
        this.commandOptionData.add(new OptionData(OptionType.CHANNEL, "category", "optional sub channel to be placed in", false));
    }


    //so now we need to make sure that we're checking for audio channels and text channels
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        //get the channel name, can't be null
        String channelName = Objects.requireNonNull(event.getOption("channelname"))
                                        .getAsString().replace(" ", "-");

        //get a ref to the user
        String memberName = Objects.requireNonNull(member).getEffectiveName();

        //if there is a category
        if (event.getOption("category") != null) {
            //initialize it
            String category = Objects.requireNonNull(event.getOption("category")).getAsString();

            //lets get a ref to the channel too
            Channel ch = Objects.requireNonNull(event.getOption("category").getAsChannel());

            if (ch.getType() == ChannelType.CATEGORY) {
                //DO SOMETHING
            }

            if (Objects.requireNonNull(guild).getChannels().toString().contains(channelName)) {
                event.reply("A text channel of that name already exists in category (" + category + ")").setEphemeral(true).queue();
                return;
            }

            guild.createTextChannel(channelName, guild.getCategoryById(category)).queue();
            event.reply(memberName + " created a new text channel in \""
                    + guild.getCategoryById(category) + "\", \"" + channelName + "\"").queue();
        }
        else {

            if (Objects.requireNonNull(guild).getTextChannels().toString().contains(channelName)) {
                event.reply("A text channel of that name already exists in this category (Default).").setEphemeral(true).queue();
                return;
            }

            guild.createTextChannel(channelName).queue();
            event.reply(memberName + " created a new text channel, \"" + channelName + "\".").queue();
        }

    }
}