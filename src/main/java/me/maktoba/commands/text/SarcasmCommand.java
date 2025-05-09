package me.maktoba.commands.text;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * SarcasmCommand is just a little text command I don't see very often which
 * takes user-given text and capitalizes every other character (starting with
 * the second character) to give it that "meme-like" spin to the message. Great
 * for poking fun at your friends or memeing silly quotes.
 */
public class SarcasmCommand extends Command {
    public SarcasmCommand(JBot bot) {
        super(bot);
        this.name = "sarcasm";
        this.description = "capitalizes every other letter to portray sarcasm";
        this.type = "text";
        this.commandOptionData.add(new OptionData(OptionType.STRING, "text", "text to be sarcasted", true));
    }

    /**
     * Create the mutated String.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String text = event.getOption("text").getAsString();
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(text.charAt(0)));

        for (int i = 1; i < text.length(); i++) {
            sb.append((i % 2) != 0 ? Character.toUpperCase(text.charAt(i)) : text.charAt(i));
        }

        event.reply(sb.toString()).queue();
    }
}