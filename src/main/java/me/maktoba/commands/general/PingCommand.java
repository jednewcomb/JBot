package me.maktoba.commands.general;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand extends Command {

    public PingCommand(JBot jbot) {
        super(jbot);
        this.name = "ping";
        this.description = "Responds with \"Pong!\" and displays the bot's latency.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        long time = event.getJDA().getGatewayPing();
        event.replyFormat("Pong! %d ms", time).queue();
    }
}