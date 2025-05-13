package me.maktoba.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.clients.TvHtml5Embedded;
import io.github.cdimascio.dotenv.Dotenv;
import me.maktoba.handlers.GuildMusicHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * The MusicListener class is responsible for managing the music playback for different guilds.
 * It acts as a singleton INSTANCE and uses Lavaplayer to load and play tracks from various audio sources.
 * As
 *
 * This class registers audio source managers (e.g., YouTube) and allows tracks to be queued and played
 * in Discord voice channels.
 */
public class MusicListener extends ListenerAdapter {
    static final Logger logger = LoggerFactory.getLogger(MusicListener.class);
    private static MusicListener INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicHandler> guildMusicManagers;
    public final YoutubeAudioSourceManager ytSourceManager;

    //TODO: We need to make this work for all the sources we've registered.
    /**
     * Registers audio source managers for YouTube, Vimeo, BandCamp, and SoundCloud.
     */
    private MusicListener() {
        playerManager = new DefaultAudioPlayerManager();
        VimeoAudioSourceManager vimeoSourceManager = new VimeoAudioSourceManager();
        BandcampAudioSourceManager bandCampAudioSourceManager = new BandcampAudioSourceManager();
        ytSourceManager = new YoutubeAudioSourceManager(true, new TvHtml5Embedded());

        playerManager.registerSourceManager(ytSourceManager);
        playerManager.registerSourceManager(vimeoSourceManager);
        playerManager.registerSourceManager(bandCampAudioSourceManager);
        //playerManager.registerSourceManager(soundCloudAudioSourceManager);;

        guildMusicManagers = new HashMap<>();

        Dotenv oAuthToken = Dotenv.configure().load();
        String ytToken = oAuthToken.get("YOUTUBETOKEN");

//        ytSourceManager.useOauth2(null, false);
        ytSourceManager.useOauth2(ytToken, true);

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    /**
     * Retrieves the instance of MusicListener.
     *
     * @return the singleton instance of MusicListener.
     */
    public static MusicListener get() {
        if (INSTANCE == null) {
            INSTANCE = new MusicListener();
        }
        return INSTANCE;
    }

    /**
     * Retrieves the GuildMusicManager for the specified guild, creating it if it does not exist.
     * Each guild has a unique GuildMusicManager that manages its music playback independently.
     *
     * @param guild the guild for which the music manager is requested.
     * @return the GuildMusicManager for the given guild.
     */
    public GuildMusicHandler getGuildMusicManager(Guild guild) {
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicHandler musicManager = new GuildMusicHandler(playerManager, guild);

            guild.getAudioManager().setSendingHandler(musicManager.getAudioForwarder());
            return musicManager;
        });
    }

    /**
     * Adds a track to the queue for the specified guild by its URL. If the track is part of a playlist,
     * all tracks in the playlist are added to the queue.
     *
     * @param guild the guild where the track will be queued.
     * @param trackURL the URL of the track or playlist to be loaded.
     */
    public void loadTrack(SlashCommandInteractionEvent event, Guild guild, String trackURL) {
        GuildMusicHandler guildMusicManager = getGuildMusicManager(guild);

        playerManager.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler() {

            /**
             * Fires upon a track successfully loading.
             * @param track The loaded track.
             */
            @Override
            public void trackLoaded(AudioTrack track) {
                guildMusicManager.getTrackScheduler().queue(track);
                event.replyFormat("Queued **%s**.", track.getInfo().title).queue();
            }

            /**
             * Search queries on YouTube that include "ytsearch:" load as playlists
             * because a single query can result in multiple tracks (I assume due to
             * similarly named Songs or Artists).
             */
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.isSearchResult()) {
                    AudioTrack track = playlist.getTracks().get(0);
                    guildMusicManager.getTrackScheduler().queue(track);
                    event.replyFormat("Queued **%s**.", track.getInfo().title).queue();
                } else {

                    for (AudioTrack track : playlist.getTracks()) {
                        guildMusicManager.getTrackScheduler().queue(track);
                    }
                    event.replyFormat("Playlist queued: **%s**.", playlist.getName()).queue();
                }
            }

            /**
             *
             */
            @Override
            public void noMatches() {
                logger.info("No matches found!");
                event.reply("No matches found.").setEphemeral(true).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                logger.info("Failed to load track " + trackURL + ".");
                event.reply("Failed to load track.").setEphemeral(true).queue();
            }
        });
    }
}