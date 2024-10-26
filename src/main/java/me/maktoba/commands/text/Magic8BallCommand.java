package me.maktoba.commands.text;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Magic8BallCommand extends Command {

    private final List<String> responseList;

    public Magic8BallCommand(JBot jbot) {
        super(jbot);
        this.name = "magic8ball";
        this.description = "Ask a question to the mighty magic 8 ball!";
        this.commandOptionData.add(new OptionData(OptionType.STRING, "question", "question you'd like an answer to", true));
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

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply(event.getOption("question").getAsString()
                + "\n" + this.responseList.get(new Random().nextInt(this.responseList.size()) + 1)).queue();
    }
}
