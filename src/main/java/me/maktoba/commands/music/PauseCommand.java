package me.maktoba.commands.music;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PauseCommand extends Command {
    public PauseCommand(JBot bot) {
        super(bot);
        this.name = "pause";
        this.description = "pause song";
        this.type = "music";
    }
    /**
     * Pause the song currently playing.
     * @param event - The command
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        MusicListener listener = MusicListener.get();

        listener.getGuildMusicManager(guild).getTrackScheduler().pause();
        event.reply("paused").queue();
    }
}
