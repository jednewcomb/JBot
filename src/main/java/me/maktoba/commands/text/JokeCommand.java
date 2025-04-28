package me.maktoba.commands.text;

import com.github.codeboy.jokes4j.Jokes4J;
import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * This command returns a joke from the JokeAPI, using the Java wrapper written by the-codeboy.
 * Info on the JokeAPI can be found at
 * https://sv443.net/jokeapi/v2/
 * <p>
 * Info on the Jokes4J wrapper can be found here:
 * https://github.com/the-codeboy/Jokes4J?tab=readme-ov-file
 */
public class JokeCommand extends Command {

    /**
     * Creates an instance of the JokeCommand.
     * @param jbot - Bot singleton to which the command is registered.
     */
    public JokeCommand(JBot jbot) {
        super(jbot);
        this.name = "joke";
        this.description = "tells you a joke";
        this.type = "text";
    }

    /**
     * Return the text of a Joke from the Jokes4J API.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply(Jokes4J.getJokeString()).queue();
    }
}
