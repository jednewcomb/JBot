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

    //TODO: Functions for:
    //TODO:  - toStringDefault()
    //TODO:  - toStringCategory()
    //TODO:  - checkCommunity()
    //TODO:

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        String desiredChannelName = Objects.requireNonNull(event.getOption("channelname"))
                .getAsString()
                .replace(" ", "-");

        String desiredChannelType = Objects.requireNonNull(event.getOption("channeltype")).getAsString();

        if (checkCategory(event)) {
            createDefault(desiredChannelType, guild, event, desiredChannelName);
        }
        else {
            createCategory(desiredChannelType, guild, event, desiredChannelName);
        }
    }

    public void createDefault(String desiredChannelType, Guild guild, SlashCommandInteractionEvent event, String desiredChannelName) {
        switch(desiredChannelType) {
            case "TEXT":
                handleText(guild, event, desiredChannelName);
                break;
            case "VOICE":
                handleVoice(guild ,event, desiredChannelName);
                break;
            case "CATEGORY":
                handleCategory(guild, event, desiredChannelName);
                break;
            case "FORUM":
                handleForum(guild, event, desiredChannelName);
                break;
            case "NEWS":
                handleNews(guild, event, desiredChannelName);
                break;
            case "MEDIA":
                handleMedia(guild, event, desiredChannelName);
                break;
            case "STAGE":
                handleStage(guild, event, desiredChannelName);
                break;
        }
    }

    public void createCategory(String desiredChannelType, Guild guild, SlashCommandInteractionEvent event, String desiredChannelName) {
        Category cg = event.getOption("category").getAsChannel().asCategory();
        switch (desiredChannelType) {
            case "TEXT":
                handleText(guild, event, desiredChannelName, cg);
                break;
            case "VOICE":
                handleVoice(guild ,event, desiredChannelName, cg);
                break;
            case "FORUM":
                handleForum(guild, event, desiredChannelName, cg);
                break;
            case "NEWS":
                handleNews(guild, event, desiredChannelName, cg);
                break;
            case "MEDIA":
                handleMedia(guild, event, desiredChannelName, cg);
                break;
            case "STAGE":
                handleStage(guild, event, desiredChannelName, cg);
                break;
        }
    }

    public String reply() {

    }

    public boolean checkCategory(SlashCommandInteractionEvent event) {
        return event.getOption("category") == null;
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