package me.maktoba.commands.music;

import com.sedmelluq.discord.lavaplayer.tools.JsonBrowser;
import com.sedmelluq.discord.lavaplayer.tools.ThumbnailTools;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.TrackHandler;
import me.maktoba.listeners.MusicListener;
import me.maktoba.util.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

/**
 * This command responds to the user with an Embed containing info
 * on the track that is playing currently.
 */
public class NowPlayingCommand extends Command {

    /**
     * Creates an instance of NowPlayingCommand.
     * @param bot - Bot singleton.
     */
    public NowPlayingCommand(JBot bot) {
        super(bot);
        this.name = "nowplaying";
        this.description = "Show info on currently playing song";
        this.type = "music";
    }

    /**
     * If a song is playing, fetch the title, url, and thumbnail and
     * respond in an Embed.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MusicListener  music = MusicListener.get();
        if (music == null) return;

        Guild guild = event.getGuild();
        TrackHandler scheduler = music.getGuildMusicManager(guild).getTrackScheduler();

        if (!scheduler.isPlaying()) {
            event.reply("Player is not currently playing a track.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        event.replyEmbeds(buildEmbed(scheduler).build()).queue();

    }

    //TODO: Might need this not to only do yt links

    /**
     * Fetch the url, thumbnail, and title of the youtube video.
     * @param scheduler - TrackScheduler which holds our MusicPlayer.
     * @return - EmbedBuilder with info on the current track.
     */
    private EmbedBuilder buildEmbed(TrackHandler scheduler) {
        AudioTrack track = scheduler.getPlayer().getPlayingTrack();
        AudioTrackInfo info = track.getInfo();
        String url = info.uri;
        String thumbnail = ThumbnailTools.getYouTubeThumbnail(JsonBrowser.NULL_BROWSER, info.identifier);
        String formatted = TimeUtil.formatMillis(track.getDuration());

        return new EmbedBuilder()
                .setTitle(info.title)
                .setDescription(url + "duration: '" + formatted + "'")
                .setImage(thumbnail)
                .setColor(Color.RED);
    }
}
