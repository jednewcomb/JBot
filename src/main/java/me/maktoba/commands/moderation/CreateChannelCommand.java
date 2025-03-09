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
        if (nameExists(guild, desiredChannelName, null)) {
            event.reply(replyDefault()).setEphemeral(true).queue();
            return;
        }

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
        if (nameExists(guild, desiredChannelName, cg.getName())) {
            event.reply(replyCategory(cg)).setEphemeral(true).queue();
            return;
        }

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

    public String replyDefault() {
        return "There is already a channel with that name in the default category.";
    }

    public String replyCategory(Category category) {
        return "There is already a channel with that name in the " + category.getName() + " category.";
    }

    public boolean checkCommunity(Guild guild) {
        return guild.getFeatures().contains("COMMUNITY");
    }

    public boolean checkCategory(SlashCommandInteractionEvent event) {
        return event.getOption("category") == null;
    }

    public void handleStage(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        guild.createStageChannel(channelName, category).queue();
        event.reply(channelName + " created in ___ category").setEphemeral(true).queue();
    }

    public void handleStage(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createStageChannel(channelName).queue();
        event.reply(channelName + " created in the default category").setEphemeral(true).queue();
    }

    public void handleMedia(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        guild.createMediaChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category").setEphemeral(true).queue();
    }

    public void handleMedia(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createMediaChannel(channelName).queue();
        event.reply(channelName + " created in the default category").setEphemeral(true).queue();
    }

    public void handleNews(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        guild.createNewsChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category").setEphemeral(true).queue();
    }

    public void handleNews(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createNewsChannel(channelName).queue();
        event.reply(channelName + " created in the default category").setEphemeral(true).queue();
    }

    public void handleForum(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        //Forum channels can only be made if the Server has Community enabled
        if (!checkCommunity(guild)) {
            event.reply("Server must have Community enabled to create forum channels.").queue();
            return;
        }
        guild.createForumChannel(channelName, category).queue();
        event.reply(channelName + " created in ___").setEphemeral(true).queue();
    }

    public void handleForum(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        //Forum channels can only be made if the Server has Community enabled
        if (!guild.getFeatures().contains("COMMUNITY")) {
            event.reply("Server must have Community enabled to create forum channels.").queue();
            return;
        }
        guild.createForumChannel(channelName).queue();
        event.reply(channelName + " created in default").queue();
    }

    public void handleVoice(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        guild.createVoiceChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category").setEphemeral(true).queue();
    }

    public void handleVoice(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createVoiceChannel(channelName).queue();
        event.reply(channelName + " created in the default category").setEphemeral(true).queue();
    }

    public void handleText(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        guild.createTextChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category.").setEphemeral(true).queue();
    }

    public void handleText(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createTextChannel(channelName).queue();
        event.reply(channelName + " created in the default category.").setEphemeral(true).queue();
    }

    public void handleCategory(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createCategory(channelName).queue();
        event.reply(channelName + " category created").setEphemeral(true).queue();
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