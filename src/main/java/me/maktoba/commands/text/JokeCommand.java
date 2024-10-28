package me.maktoba.commands.text;

import com.github.codeboy.jokes4j.Jokes4J;
import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class JokeCommand extends Command {

    public JokeCommand(JBot jbot) {
        super(jbot);
        this.name = "joke";
        this.description = "tells you a joke";
        this.type = "text";
    }

    /**
     * Wow these jokes are kinda nuts
     * @param event
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply(Jokes4J.getJokeString()).queue();
    }
}
