package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.attribute.ICategorizableChannel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
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
                .addChoice("voice", "VOICE")
                .addChoice("category", "CATEGORY"));
        this.commandOptionData.add(new OptionData(OptionType.CHANNEL, "category", "category for channel to be placed in", false)
                .setChannelTypes(ChannelType.CATEGORY));

    }

    // For types, we have:
    // TEXT, VOICE, FORUM?, MEDIA?, NEWS, RULES, STAGE
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        //TODO: Once text and voice channels are taken care off, consider adding the others too
        Guild guild = event.getGuild();

        String desiredChannelName = Objects.requireNonNull(event.getOption("channelname"))
                .getAsString()
                .replace(" ", "-");

        String desiredChannelType = Objects.requireNonNull(event.getOption("channeltype")).getAsString();

        //if theres no category, lets put it in the default category
        if (event.getOption("category") == null) {

            //what channelType? - maybe getting the channel type should be its own function?
            if (desiredChannelType.equals("TEXT")) {
                handleText(guild, event, desiredChannelName);
            } else if (desiredChannelType.equals("CATEGORY")) {
                handleCategory(guild, event, desiredChannelName);
            }

        } else {
            Channel ch = event.getOption("category").getAsChannel();
            Category category = event.getOption("category").getAsChannel().asCategory();

            if (desiredChannelType.equals("TEXT")) {
                handleText(guild, event, desiredChannelName, category);//LEFT OFF HERE
            }
        }
    }

    public void handleCategory(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        if (nameExists(guild, channelName, null)) {
            event.reply("There is already a category with that name.").queue();
            return;
        }

        guild.createCategory(channelName).queue();
        event.reply(channelName + " category created").queue();
    }

    public void handleText(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        if (nameExists(guild, channelName, null)) {
            event.reply("There is already a channel with that name in the default category.").queue();
            return;
        }

        guild.createTextChannel(channelName).queue();
        event.reply(channelName + " created in the default category.").queue();
    }

    public void handleText(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        if (nameExists(guild, channelName, null)) {
            event.reply("There is already a channel with that name in that category.").queue();
            return;
        }

        guild.createTextChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category.").queue();
    }

    public boolean nameExists(Guild guild, String newChannelName, String category) {
        for (Channel ch : guild.getChannels()) {

            if (category == null) {

                if (ch.getType() == ChannelType.CATEGORY) {
                    return false;
                }

                if (ch.getName().equalsIgnoreCase(newChannelName)) {
                    return true;
                }

            } else {

                if (ch.getType() == ChannelType.CATEGORY) {

                    for (Category cg : guild.getCategories()) {
                        if (cg.getName().equalsIgnoreCase(newChannelName)) {
                            return true;
                        }
                    }
                }
                if (ch.getName().equalsIgnoreCase(newChannelName)) {
                    return true;
                }
            }

        }

        return false;

    }

}