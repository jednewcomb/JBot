package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
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
                .addChoice("voice", "VOICE")
                .addChoice("category", "CATEGORY")
                .addChoice("forum", "FORUM")
                .addChoice("news", "NEWS")
                .addChoice("media" , "MEDIA")
                .addChoice("stage", "STAGE"));
        this.commandOptionData.add(new OptionData(OptionType.CHANNEL, "category", "category for channel to be placed in", false)
                .setChannelTypes(ChannelType.CATEGORY));

    }

    // For types, we have:
    // TEXT, VOICE, FORUM?, MEDIA?, NEWS, RULES, STAGE
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        String desiredChannelName = Objects.requireNonNull(event.getOption("channelname"))
                .getAsString()
                .replace(" ", "-");

        String desiredChannelType = Objects.requireNonNull(event.getOption("channeltype")).getAsString();

        //if there's no category, lets put it in the default category
        if (event.getOption("category") == null) {

            //what channelType? - maybe getting the channel type should be its own function?
            if (desiredChannelType.equals("TEXT")) {
                handleText(guild, event, desiredChannelName);
            }
            else if (desiredChannelType.equals("VOICE")) {
                handleVoice(guild, event, desiredChannelName);
            }
            else if (desiredChannelType.equals("CATEGORY")) {
                handleCategory(guild, event, desiredChannelName);
            }
            else if (desiredChannelType.equals("FORUM")) {
                handleForum(guild, event, desiredChannelName);
            }
            else if (desiredChannelType.equals("NEWS")) {
                handleNews(guild, event, desiredChannelName);
            }
            else if (desiredChannelType.equals("MEDIA")) {
                handleMedia(guild, event, desiredChannelName);
            }
            else {
                handleStage(guild, event, desiredChannelName); //what?
            }

            //should I just pass null to all these and make one function?

        } else {
            Channel ch = event.getOption("category").getAsChannel();
            Category category = event.getOption("category").getAsChannel().asCategory();

            //no categories here because we don't want to allow a category within a category (do we?)
            if (desiredChannelType.equals("TEXT")) {
                handleText(guild, event, desiredChannelName, category);//LEFT OFF HERE
            }
            else if (desiredChannelType.equals("VOICE")) {
                handleVoice(guild, event, desiredChannelName, category);
            }
            else if (desiredChannelType.equals("FORUM")) {
                handleForum(guild, event, desiredChannelName, category);
            }
            else if (desiredChannelType.equals("NEWS")) {
                handleNews(guild, event, desiredChannelName, category);
            }
            else if (desiredChannelType.equals("MEDIA")) {
                handleMedia(guild, event, desiredChannelName, category);
            }
            else {
                handleStage(guild, event, desiredChannelName, category); //what?
            }
        }

        //this feels... gross. at the very least it should be a switch

        //TODO: Let's make sure that everything is being set to ephemeral with these messages,
        //TODO: there has got to be a better way to code this, I just liked all the typing.
        //TODO: Can probably put all the print statements in their own method.
        //TODO: Honestly, all these can probably just be replaced with their own single method
        //TODO: and just be passed null values
    }

    public void handleStage(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        if (nameExists(guild, channelName, category.getName())) {
            event.reply("There is a channel with that name in the ___ category").setEphemeral(true).queue();
            return;
        }

        guild.createStageChannel(channelName, category).queue();
        event.reply(channelName + " created in ___ category").queue();
    }

    public void handleStage(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        if (nameExists(guild, channelName, null)) {
            event.reply("There is a channel with that name in the default category").setEphemeral(true).queue();
            return;
        }

        guild.createStageChannel(channelName).queue();
        event.reply(channelName + " created in the default category").setEphemeral(true).queue();
    }

    public void handleMedia(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        //this might be a community channel type as well?
        if (nameExists(guild, channelName, category.getName())) {
            event.reply("There is a channel with that name in the ___ cateogory").setEphemeral(true).queue();
            return;
        }

        guild.createMediaChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category").setEphemeral(true).queue();
    }

    public void handleMedia(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        //this might be a community channel type as well?
        if (nameExists(guild, channelName, null)) {
            event.reply("There is a channel with that name in the default cateogory").setEphemeral(true).queue();
            return;
        }

        guild.createMediaChannel(channelName).queue();
        event.reply(channelName + " created in the default category").setEphemeral(true).queue();
    }

    public void handleNews(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        //this might be a community channel type as well?
        if (nameExists(guild, channelName, category.getName())) {
            event.reply("There is a channel with that name in the ___ cateogory").setEphemeral(true).queue();
            return;
        }

        guild.createNewsChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category").setEphemeral(true).queue();
    }

    public void handleNews(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        //this might be a community channel type as well?
        if (nameExists(guild, channelName, null)) {
            event.reply("There is a channel with that name in the default cateogory").setEphemeral(true).queue();
            return;
        }

        guild.createNewsChannel(channelName).queue();
        event.reply(channelName + " created in the default category").setEphemeral(true).queue();
    }

    public void handleForum(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        //Forum channels can only be made if the Server has Community enabled
        if (!guild.getFeatures().contains("COMMUNITY")) {
            event.reply("Server must have Community enabled to create forum channels.").queue();
            return;
        }

        if (nameExists(guild, channelName, category.getName())) {
            event.reply("already one with that name there").queue();
            return;
        }
        guild.createForumChannel(channelName, category).queue();
        event.reply(channelName + " created in ___").queue();
    }

    public void handleForum(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        //Forum channels can only be made if the Server has Community enabled
        if (!guild.getFeatures().contains("COMMUNITY")) {
            event.reply("Server must have Community enabled to create forum channels.").queue();
            return;
        }

        if (nameExists(guild, channelName, null)) {
            event.reply("already one with that name there").queue();
            return;
        }
        guild.createForumChannel(channelName).queue();
        event.reply(channelName + " created in default").queue();
    }

    public void handleVoice(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        if (nameExists(guild, channelName, category.getName())) {
            event.reply("already one with that name there").queue();
            return;
        }

        guild.createVoiceChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category").queue();
    }

    public void handleVoice(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        if (nameExists(guild, channelName, null)) {
            event.reply("already one with that name there").queue();
            return;
        }

        guild.createVoiceChannel(channelName).queue();
        event.reply(channelName + " created in the default category").queue();
    }

    public void handleText(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        if (nameExists(guild, channelName, category.getName()/*Maybe issue here*/)) {
            event.reply("There is already a channel with that name in that category.").queue();
            return;
        }

        guild.createTextChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category.").queue();
    }

    public void handleText(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        if (nameExists(guild, channelName, null)) {
            event.reply("There is already a channel with that name in the default category.").queue();
            return;
        }

        guild.createTextChannel(channelName).queue();
        event.reply(channelName + " created in the default category.").queue();
    }

    public void handleCategory(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        if (nameExists(guild, channelName, null)) {
            event.reply("There is already a category with that name.").queue();
            return;
        }

        guild.createCategory(channelName).queue();
        event.reply(channelName + " category created").queue();
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