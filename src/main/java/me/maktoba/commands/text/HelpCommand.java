package me.maktoba.commands.text;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class HelpCommand extends Command {

    public HelpCommand(JBot jbot) {
        super(jbot);
        this.name = "help";
        this.description = "display information on commands";
        this.commandOptionData.add(new OptionData
                (OptionType.STRING, "commandtype", "the type of command to see info for", true));
    }

    /**
     * @param event
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String command = event.getOption("commandtype").getAsString();

        //this is so ugly I've got to find a better way. Probably like pop up list window or something
        if (command.equals("music")) {
            event.replyEmbeds(music(command)).queue();
        }
        else if (command.equals("text")) {
            event.replyEmbeds(text(command)).queue();
        }
        else {
            event.reply("no valid command type added");
        }


    }

    public MessageEmbed music(String commandType) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Music Commands");
        embed.addField("/play", "play a song/add it to queue", false);
        embed.addField("/pause", "pause song", false);
        embed.addField("/stop", "stop current song and remove from queue", false);
        embed.addField("/skip", "skip to next song", false);
        embed.addField("/replay", "replay current song", false);
        embed.addField("/nowplaying", "show info on current song", false);
        embed.addField("/clear", "clear the queue", false);

        return embed.build();
    }

    public MessageEmbed text (String commandType) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Text Commands");
        embed.addField("/help", "get information on JBot commands", false);
        embed.addField("/magic8ball", "ask the magic 8 ball a yes or no question", false);
        embed.addField("/reverse", "reverse the given string", false);
        embed.addField("/sarcasm", "meme your text", false);

        return embed.build();
    }
}
