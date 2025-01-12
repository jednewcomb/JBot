package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
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
        this.commandOptionData.add(new OptionData(OptionType.STRING, "channeltype", "desired type for new channel", true)
                .addChoice("text", "TEXT")
                .addChoice("voice", "VOICE"));
        this.commandOptionData.add(new OptionData(OptionType.CHANNEL, "category", "category for channel to be placed in", false)
                .setChannelTypes(ChannelType.CATEGORY));

    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        //TODO: Once text and voice channels are taken care off, consider adding the others too

        Guild guild = event.getGuild();
        Member member = event.getMember();
        String memberName = Objects.requireNonNull(member).getEffectiveName();

        String desiredChannelName = Objects.requireNonNull(event.getOption("channelname"))
                .getAsString()
                .replace(" ", "-");

        //theres gotta be a way to use the channel class here right? ... RIGHT?
        String desiredChannelType = Objects.requireNonNull(event.getOption("channeltype")).getAsString();

        //if theres a category, put it in that category, otherwise put it in the default
        if (event.getOption("category") != null) {
            Channel ch = event.getOption("category").getAsChannel();
            Category cg = event.getOption("category").getAsChannel().asCategory();
            //idk why this wasn't working before but it is now,
            //TODO: CG USE???
            if (desiredChannelType.equals("TEXT")) {
                //this is making channels, its bad, fix it. two generals in text channels
                if (!event.getOption("category").getAsChannel().asCategory().getChannels().toString().contains(desiredChannelType)) {
                    guild.createTextChannel(desiredChannelName,
                                    Objects.requireNonNull(event.getOption("category"))
                                            .getAsChannel()
                                            .asCategory())
                            .queue();

                    event.reply(desiredChannelName + " made in " + ch.getName()).queue();
                } else {
                    event.reply("A channel with that name exists in that category").queue();
                }

            } else {

                if (!event.getOption("category").getAsChannel().asCategory().getChannels().toString().contains(desiredChannelType)) {
                    guild.createVoiceChannel(desiredChannelName,
                                    Objects.requireNonNull(event.getOption("category"))
                                            .getAsChannel()
                                            .asCategory())
                            .queue();

                    event.reply(desiredChannelName + " made in " + ch.getName()).queue();
                } else {
                    event.reply("A channel with that name exists in that category").queue();
                }

            }

        } else {

            if (desiredChannelType.equals("TEXT")) {

                //TODO: as of now, this isn't allowing creation of a guild in default if one of the categories
                //TODO: has a guild with the same name as desiredChannelName, even if default doesn't have it
                //TODO: might need some null checks on the channels before we loop through them
                //TODO: if get channels is null, we have nothing to worry about, create the channel

                if (existsInDefault(guild, desiredChannelName)) {
                    event.reply(desiredChannelName + " already exists in the default category").queue();
                    return;
                }
                guild.createTextChannel(desiredChannelName).queue();
                event.reply(desiredChannelName + " created").queue();

            } else {

                if (existsInDefault(guild, desiredChannelName)) {
                    event.reply(desiredChannelName + " already exists in the default category").queue();
                    return;
                }
                guild.createVoiceChannel(desiredChannelName).queue();
                event.reply(desiredChannelName + " created").queue();

            }
        }

    }

    public boolean existsInDefault(Guild guild, String chanName) {
        for (Channel ch : guild.getChannels()) {
            //if its of type category, it must be a channel
            if (ch.getType() == ChannelType.CATEGORY) {
                return false;
            }

            if (ch.getName().toLowerCase().equals(chanName.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}