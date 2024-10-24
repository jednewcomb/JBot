package me.maktoba.commands.text;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class HelpCommand extends Command {

    public HelpCommand(JBot jbot) {
        super(jbot);
        this.name = "help";
        this.description = "display information on commands";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Help Menu");

        embed.setDescription("Clear - Clear the current queue\n" +
                             "NowPlaying - Show info on currently playing song\n" +
                             "Pause - Pause song\n" +
                             "Play - Play song/Add to queue\n" +
                             "Replay - Replay the current playing song\n" +
                             "Skip - Skip to the next song\n" +
                             "Stop - Stops currently playing song and removes it from queue");

        event.replyEmbeds(embed.build()).queue();
    }


}
