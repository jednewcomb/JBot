package me.maktoba.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.maktoba.commands.Command;
import me.maktoba.JBot;
import me.maktoba.handlers.TrackHandler;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * This command takes the current song and replays it.
 */
public class ReplayCommand extends Command {

    /**
     * Creates and instance of ReplayCommand.
     * @param bot - Bot singleton.
     */
    public ReplayCommand(JBot bot) {
        super(bot);
        this.name = "replay";
        this.description = "replay the current playing song";
        this.type = "music";
    }

    /**
     * Queue the song currently playing to play again next.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MusicListener music = MusicListener.get();
        if (music == null) return;

        Guild guild = event.getGuild();
        TrackHandler scheduler = music.getGuildMusicManager(guild).getTrackScheduler();

        if (!scheduler.isPlaying()) {
            event.reply("Player is not currently playing a track.")
                    .setEphemeral(true)
                    .queue();
        }

        scheduler.replay();
        AudioTrack currentPlayingTrack = scheduler.getPlayer().getPlayingTrack();
        event.replyFormat("**%s** was queued again!", currentPlayingTrack.getInfo().title)
                .setEphemeral(true)
                .queue();
    }
}