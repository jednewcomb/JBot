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

/**
 * This command allows users with administrative permissions to create new channels
 * or categories for channels in the current server.
 */
public class ChannelCreateCommand extends Command {

    /**
     * Creates an instance of the CreateChannelCommand.
     * @param bot - Bot singleton to which the command is registered.
     */
    public ChannelCreateCommand(JBot bot) {
        super(bot);
        this.name = "newchannel";
        this.description = "Creates a new channel in the server in the desired subcategory, or the default one";
        this.type = "moderation";
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

    /**
     * Retrieves the user's desired channel name and type, as well as the category if desired.
     * If no category is provided, the channel is created in the "default" category.
     * @param event - Event Trigger.
     */
    //TODO:    - Take care of print statement issues.
    //TODO:    - Maybe ChannelHandler Class, this class is a bit thick.
    //TODO:    - Enums instead of Strings for Categories?
    //TODO:    - Do a bit more reading up on channels to see if we can improve nameExists()
    //TODO:    - Community checks
    //TODO:    - Maybe if the name already exists we just ask the user if they're sure they want
    //TODO:      two channels with the same name in the same server.
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        String desiredChannelName =
                Objects.requireNonNull(event.getOption("channelname"))
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

    /**
     * Create channel in the default category.
     * @param desiredChannelType - The type of channel the user wants to create.
     * @param guild              - The current server.
     * @param event              - Event Trigger.
     * @param desiredChannelName - The name the user wants to give their new channel.
     */
    public void createDefault(String desiredChannelType, Guild guild,
                                    SlashCommandInteractionEvent event, String desiredChannelName) {

        if (nameExists(guild, desiredChannelName, null)) {
            event.reply(replyTakenDefault()).setEphemeral(true).queue();
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

    /**
     * Create channel in the specified category.
     * @param desiredChannelType - The type of channel the user wants to create.
     * @param guild              - The current server.
     * @param event              - Event Trigger.
     * @param desiredChannelName - The name the user wants to give their new channel.
     */
    public void createCategory(String desiredChannelType, Guild guild,
                                    SlashCommandInteractionEvent event, String desiredChannelName) {

        Category cg = event.getOption("category").getAsChannel().asCategory();
        if (nameExists(guild, desiredChannelName, cg.getName())) {
            event.reply(replyTakenCategory(cg)).setEphemeral(true).queue();
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

    /**
     * Reply to user that there is already a channel with that name in the default category.
     * @return - A String which informs the user there's already a channel with that name.
     */
    public String replyTakenDefault() {
        return "There is already a channel with that name in the default category.";
    }

    /**
     * Reply to user that there is already a channel with that name in the specified category.
     * @param category - The user specified category.
     * @return         - A String which informs the user there's already a channel with that
     *                   name in the specified category.
     */
    public String replyTakenCategory(Category category) {
        return "There is already a channel with that name in the " + category.getName() + " category.";
    }

    /**
     * Some channel types require that the server has the "Community" feature enabled.
     * @param guild - The current server.
     * @return      - A boolean which specifies if community is enabled or not.
     */
    public boolean checkCommunity(Guild guild) {
        return guild.getFeatures().contains("COMMUNITY");
    }

    /**
     * Checks if the user has specified a category.
     * @param event - Event trigger.
     * @return      - A boolean which specifies if the event options included
     *                a category.
     */
    public boolean checkCategory(SlashCommandInteractionEvent event) {
        return event.getOption("category") == null;
    }

    /**
     * Handles the creation of a channel with type "Stage" in the specified category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     * @param category    - User specified category in the server.
     */
    public void handleStage(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        guild.createStageChannel(channelName, category).queue();
        event.reply(channelName + " created in ___ category").setEphemeral(true).queue();
    }

    /**
     * Handles the creation of a channel with the type "Stage" in the default category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     */
    public void handleStage(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createStageChannel(channelName).queue();
        event.reply(channelName + " created in the default category").setEphemeral(true).queue();
    }

    /**
     * Handles the creation of a channel with type "Media" in the specified category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     * @param category    - User specified category in the server.
     */
    public void handleMedia(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        guild.createMediaChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category").setEphemeral(true).queue();
    }

    /**
     * Handles the creation of a channel with the type "Media" in the default category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     */
    public void handleMedia(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createMediaChannel(channelName).queue();
        event.reply(channelName + " created in the default category").setEphemeral(true).queue();
    }

    /**
     * Handles the creation of a channel with type "News" in the specified category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     * @param category    - User specified category in the server.
     */
    public void handleNews(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        guild.createNewsChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category").setEphemeral(true).queue();
    }

    /**
     * Handles the creation of a channel with the type "News" in the default category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     */
    public void handleNews(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createNewsChannel(channelName).queue();
        event.reply(channelName + " created in the default category").setEphemeral(true).queue();
    }

    /**
     * Handles the creation of a channel with type "Forum" in the specified category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     * @param category    - User specified category in the server.
     */
    public void handleForum(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        //Forum channels can only be made if the Server has Community enabled
        if (!checkCommunity(guild)) {
            event.reply("Server must have Community enabled to create forum channels.").queue();
            return;
        }
        guild.createForumChannel(channelName, category).queue();
        event.reply(channelName + " created in ___").setEphemeral(true).queue();
    }

    /**
     * Handles the creation of a channel with the type "Forum" in the default category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     */
    public void handleForum(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        //Forum channels can only be made if the Server has Community enabled
        if (checkCommunity(guild)) {
            event.reply("Server must have Community enabled to create forum channels.").queue();
            return;
        }
        guild.createForumChannel(channelName).queue();
        event.reply(channelName + " created in default").queue();
    }

    /**
     * Handles the creation of a channel with type "Voice" in the specified category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     * @param category    - User specified category in the server.
     */
    public void handleVoice(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        guild.createVoiceChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category").setEphemeral(true).queue();
    }

    /**
     * Handles the creation of a channel with the type "Voice" in the default category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     */
    public void handleVoice(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createVoiceChannel(channelName).queue();
        event.reply(channelName + " created in the default category").setEphemeral(true).queue();
    }

    /**
     * Handles the creation of a channel with type "Text" in the specified category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     * @param category    - User specified category in the server.
     */
    public void handleText(Guild guild, SlashCommandInteractionEvent event, String channelName, Category category) {
        guild.createTextChannel(channelName, category).queue();
        event.reply(channelName + " created in the ___ category.").setEphemeral(true).queue();
    }

    /**
     * Handles the creation of a channel with the type "Text" in the default category.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     */
    public void handleText(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createTextChannel(channelName).queue();
        event.reply(channelName + " created in the default category.").setEphemeral(true).queue();
    }

    /**
     * Handles the creation of a Category for channels.
     * @param guild       - The current server.
     * @param event       - Event trigger.
     * @param channelName - User desired channel name.
     */
    public void handleCategory(Guild guild, SlashCommandInteractionEvent event, String channelName) {
        guild.createCategory(channelName).queue();
        event.reply(channelName + " category created").setEphemeral(true).queue();
    }

    /**
     * This...needs addressing.
     * @param guild
     * @param newChannelName
     * @param category
     * @return
     */

    //TODO: This approach is not great.
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