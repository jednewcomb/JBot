package me.maktoba.commands.music;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.TrackScheduler;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * This command Stops playback and clears the current queue.
 */
public class StopCommand extends Command {
    /**
     * Creates an instance of StopCommand
     * @param bot - Bot singleton to which the command is registered.
     */
    public StopCommand(JBot bot) {
        super(bot);
        this.name = "stop";
        this.description = "stops the current playing song and removes it from the queue";
        this.type = "music";
    }

    /**
     * If playback occurring, stop it and clear the queue.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MusicListener music = MusicListener.get();
        if (music == null) return;

        Guild guild = event.getGuild();
        TrackScheduler scheduler = music.getGuildMusicManager(guild).getTrackScheduler();

        if (!scheduler.isPlaying()) {
            event.reply("Player is not currently playing a track.").setEphemeral(true).queue();
            return;
        }

        scheduler.stopTrack();
        event.reply("Playback stopped, queue cleared.").setEphemeral(true).queue();
    }
}
