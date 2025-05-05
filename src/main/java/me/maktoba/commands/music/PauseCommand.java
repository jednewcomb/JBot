package me.maktoba.commands.music;

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
            event.reply("Player is already paused! Use /play to resume playback.").queue();
            return;
        }

        scheduler.pause();
        event.replyFormat("Player was paused." + getCurrentTrackTitle(scheduler)).queue();
    }

    //TODO: FIX?
    private String getCurrentTrackTitle(TrackScheduler scheduler) {
        //Peek here is returning null - I think we need to
        //look at how the Queue is mutating between each Play/Pause action.

        //we're getting the same track twice in the queue for some reason.
        AudioTrackInfo trackInfo = scheduler.getQueue().peek().getInfo();
        return trackInfo.title;
    }
}
