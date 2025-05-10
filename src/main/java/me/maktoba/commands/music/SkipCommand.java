package me.maktoba.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.TrackHandler;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * This command allows user's to skip the current playing track.
 */
public class SkipCommand extends Command {

    /**
     * Creates and instance of SkipCommand.
     * @param bot - Bot singleton.
     */
    public SkipCommand(JBot bot) {
        super(bot);
        this.name = "skip";
        this.description = "skip to the next song";
        this.type = "music";
    }

    /**
     * Skip the current track if there is one.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MusicListener music = MusicListener.get();
        if (music == null) return;

        Guild guild = event.getGuild();
        TrackHandler scheduler = music.getGuildMusicManager(guild).getTrackScheduler();

        if (!scheduler.isPlaying()) {
            event.reply("Player is not currently playing a track.").setEphemeral(true).queue();
            return;
        }
        
        AudioTrack currentPlayingTrack = scheduler.getPlayer().getPlayingTrack();
        event.replyFormat("**%s** was skipped.", currentPlayingTrack.getInfo().title)
                .setEphemeral(true)
                .queue();
        scheduler.skip();
    }
}
