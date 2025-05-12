package me.maktoba.commands.text;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class mimics the Magic 8 Ball toy, which holds creative
 * responses to yes or no questions.
 */
public class Magic8BallCommand extends Command {

    private final List<String> responseList;

    /**
     * Creates an instance of Magic8BallCommand.
     * @param bot - Bot singleton.
     */
    public Magic8BallCommand(JBot bot) {
        super(bot);
        this.name = "magic8ball";
        this.description = "Ask a question to the mighty magic 8 ball!";
        this.type = "text";
        this.commandOptionData.add(new OptionData(OptionType.STRING,
                                                 "question",
                                                 "yes or no question you'd like an answer to",
                                                  true));

        this.responseList = Arrays.asList("It is certain",
                                          "It is decidedly so",
                                          "Without a doubt",
                                          "Yes definitely",
                                          "You may rely on it",
                                          "As I see it, yes",
                                          "Most likely",
                                          "Outlook good",
                                          "Yes",
                                          "Signs point to yes",
                                          "Reply hazy, try again",
                                          "Ask again later",
                                          "Better not tell you now",
                                          "Cannot predict now",
                                          "Concentrate and ask again",
                                          "Don't count on it",
                                          "My reply is no",
                                          "My sources say no",
                                          "Outlook not so good",
                                          "Very doubtful");
    }

    /**
     * Respond to user's yes or no question with classic responses found in Magic8Ball toys.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String reply = responseList.get(new Random().nextInt(responseList.size()) + 1);

        EmbedBuilder builder = EmbedUtil.createSuccessEmbed(reply);
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }
}