package me.maktoba.commands.general;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * PingCommand serves as an example command to illustrate how the bot would respond to a user.
 *
 * PingCommand responds to the user "Pong!" along with a long representing the bot's latency.
 */
public class PingCommand extends Command {

    /**
     * Creates an instance of the PingCommand.
      * @param bot - Bot singleton to which the command is registered.
     */
    public PingCommand(JBot bot) {
        super(bot);
        this.name = "ping";
        this.description = "Responds with \"Pong!\" and displays the bot latency.";
        this.type = "general";
    }

    /**
     * When triggered, bot responds with "Pong!" and a long representing the bot latency.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        long time = event.getJDA().getGatewayPing();
        event.replyFormat("Pong! %d ms", time).queue();
    }
}