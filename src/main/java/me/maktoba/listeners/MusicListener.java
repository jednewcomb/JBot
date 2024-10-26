package me.maktoba.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import io.github.cdimascio.dotenv.Dotenv;
import me.maktoba.handlers.GuildMusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
    private static MusicListener INSTANCE;
    private final AudioPlayerManager playerManager;
    private final YoutubeAudioSourceManager ytSourceManager;
    private final Map<Long, GuildMusicManager> guildMusicManagers;
    private final Dotenv oAuthToken;

    /**
     * Registers the YouTube audio source manager and local audio sources with the AudioPlayerManager.
     */
    private MusicListener() {
        playerManager = new DefaultAudioPlayerManager();
        ytSourceManager = new dev.lavalink.youtube.YoutubeAudioSourceManager();
        playerManager.registerSourceManager(ytSourceManager);

        guildMusicManagers = new HashMap<>();

        oAuthToken = Dotenv.configure().load();
        String ytToken = oAuthToken.get("YOUTUBETOKEN");
        ytSourceManager.useOauth2(ytToken, true);

        AudioSourceManagers.registerLocalSource(playerManager);
    }

    /**
     * Retrieves the instance of MusicListener
     *
     * @return the singleton instance of MusicListener
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
     * @param guild the guild for which the music manager is requested
     * @return the GuildMusicManager for the given guild
     */
    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager musicManager = new GuildMusicManager(playerManager, guild);

            guild.getAudioManager().setSendingHandler(musicManager.getAudioForwarder());

            return musicManager;
        });
    }

    /**
     * Adds a track to the queue for the specified guild by its URL. If the track is part of a playlist,
     * all tracks in the playlist are added to the queue.
     *
     * @param guild the guild where the track will be queued
     * @param trackURL the URL of the track or playlist to be loaded
     */
    public void addTrack(Guild guild, String trackURL) {
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
        playerManager.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                guildMusicManager.getTrackScheduler().queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    guildMusicManager.getTrackScheduler().queue(track);
                }
            }

            @Override
            public void noMatches() {
                System.out.println("no matches");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                System.out.println(exception.getMessage() + " " + exception.severity);
            }
        });
    }
}