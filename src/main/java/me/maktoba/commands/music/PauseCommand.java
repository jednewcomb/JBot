package me.maktoba.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.TrackScheduler;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * This command Pauses the current playing song.
 */
public class PauseCommand extends Command {
    /**
     * Creates an instance of PauseCommand.
     * @param bot - Bot singleton to which the command is registered.
     */
    public PauseCommand(JBot bot) {
        super(bot);
        this.name = "pause";
        this.description = "pause song";
        this.type = "music";
    }
    /**
     * Pause the song currently playing. Notify user if paused already.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MusicListener music = MusicListener.get();
        if (music == null) return;

        Guild guild = event.getGuild();
        TrackScheduler scheduler = music.getGuildMusicManager(guild).getTrackScheduler();

        if (scheduler.isPaused()) {
            event.reply("Player is currently paused! Use /play to resume playback.").queue();
            return;
        }

        scheduler.pause();
        AudioTrack currentPlayingTrack = scheduler.getPlayer().getPlayingTrack();
        event.replyFormat("Playback for **%s** was paused.", currentPlayingTrack.getInfo().title).queue();
    }

}
