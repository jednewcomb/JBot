package me.maktoba.commands;

import me.maktoba.JBot;
import me.maktoba.commands.general.HelpCommand;
import me.maktoba.commands.general.PingCommand;
import me.maktoba.commands.general.ServerInfoCommand;
import me.maktoba.commands.general.UserInfoCommand;
import me.maktoba.commands.moderation.BanCommand;
import me.maktoba.commands.moderation.ChannelCreateCommand;
import me.maktoba.commands.moderation.NicknameCommand;
import me.maktoba.commands.moderation.UnbanCommand;
import me.maktoba.commands.text.*;
import me.maktoba.commands.music.*;
import me.maktoba.handlers.CommandCooldownHandler;
import me.maktoba.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The CommandRegistry class manges and registers slash commands for the bot. It
 * maintains a list and map of commands, allowing for easy retrieval and execution
 * based on user interactions. This class simplifies the process of adding commands
 * to the bot and ensures that commands are registered on guild startup.
 */
public class CommandRegistry extends ListenerAdapter {

    public static List<Command> commandList = new ArrayList<>();
    public static Map<String, Command> commandMap = new HashMap<>();
    private CommandCooldownHandler cdHandler;

    /**
     * Sends each of our Commands to be Mapped to the List and Map for later retrieval.
     *
     * @param bot - Our original Bot.
     */
    public CommandRegistry(JBot bot) {
        mapCommand(new PlayCommand(bot),
                new PauseCommand(bot),
                new StopCommand(bot),
                new SkipCommand(bot),
                new ReplayCommand(bot),
                new ClearCommand(bot),
                new NowPlayingCommand(bot),

                new SarcasmCommand(bot),
                new ReverseCommand(bot),
                new Magic8BallCommand(bot),
                new JokeCommand(bot),

                new HelpCommand(bot),
                new PingCommand(bot),
                new UserInfoCommand(bot),
                new ServerInfoCommand(bot),

                new BanCommand(bot),
                new UnbanCommand(bot),
                new ChannelCreateCommand(bot),
                new NicknameCommand(bot));
    }

    /**
     * Maps the given commands to the commandList and commandMap for easy retrieval
     * and execution. The command's name is used as the key in the map.
     *
     * @param commands the commands to be mapped and registered
     */
    private void mapCommand(Command... commands) {
        for (Command cmd : commands) {
            commandMap.put(cmd.name, cmd);
            commandList.add(cmd);
        }
    }

    /**
     * Once we have the name and description of our desired command, we can execute it,
     * provided that the bot and member have the correct permissions (which is usually
     * dictated by their respective Roles in the guild)
     *
     * @param event - The SlashCommand event.
     */
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Command cmd = commandMap.get(event.getName());
        Guild guild = event.getGuild();
        Member member = event.getMember();

        //check that bot has correct permissions to carry out command
        if (cmd.requiresPermission()) {
            if (!Objects.requireNonNull(guild).getBotRole().hasPermission(cmd.requiredPermission)) {

                EmbedBuilder builder =
                        EmbedUtil.createErrorEmbed("I don't have the necessary permissions to use that command.");
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();

                return;
            }

            //check that member has correct permissions/role if that command requires it
            if (!Objects.requireNonNull(member).hasPermission(cmd.requiredPermission)) {

                EmbedBuilder builder =
                        EmbedUtil.createErrorEmbed("You don't have the necessary permissions to use that command.");
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();

                return;
            }
        }

        if (CommandCooldownHandler.memberHasCooldown(member)) {

            EmbedBuilder builder =
                    EmbedUtil.createErrorEmbed("Wait a moment to user more commands.");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        cmd.execute(event);
        CommandCooldownHandler.startCooldown(member);
    }

    /**
     * Generates a list of CommandData for all registered commands. This data is used to register
     * the commands with the Discord API when the bot starts or when a guild is ready.
     *
     * @return a list of CommandData for the commands, which will be registered on guild startup
     */
    public static List<CommandData> unpackCommandData() {
        List<CommandData> commandData = new ArrayList<>();
        for (Command command : commandList) {
            SlashCommandData slashCommand
                    = Commands.slash(command.name, command.description).addOptions(command.commandOptionData);

            if (!command.subCommands.isEmpty()) {
                commandData.add(slashCommand);
            }

            commandData.add(slashCommand);
        }

        return commandData;
    }

    /**
     * Called when a guild is ready to process commands. This method registers the bot commands
     * with the guild once the guild has set up.
     *
     * @param event the GuildReadyEvent signaling that the guild is ready for command registration
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(unpackCommandData()).queue();
    }

}