package me.maktoba.commands.music;

import com.sedmelluq.discord.lavaplayer.tools.JsonBrowser;
import com.sedmelluq.discord.lavaplayer.tools.ThumbnailTools;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.TrackScheduler;
import me.maktoba.listeners.MusicListener;
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
        TrackScheduler scheduler = music.getGuildMusicManager(guild).getTrackScheduler();

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
    private EmbedBuilder buildEmbed(TrackScheduler scheduler) {
        AudioTrackInfo info = scheduler.getPlayer().getPlayingTrack().getInfo();
        String url = info.uri;
        String thumbnail = ThumbnailTools.getYouTubeThumbnail(JsonBrowser.NULL_BROWSER, info.identifier);

        return new EmbedBuilder()
                .setTitle(info.title)
                .setDescription(url)
                .setImage(thumbnail)
                .setColor(Color.RED);
    }
}
