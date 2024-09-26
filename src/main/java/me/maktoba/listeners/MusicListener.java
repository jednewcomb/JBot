package me.maktoba.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import me.maktoba.handlers.GuildMusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class MusicListener extends ListenerAdapter {

    private static MusicListener INSTANCE;

    private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private final YoutubeAudioSourceManager ytSourceManager;


    //I believe we need this map in order to keep track of multiple music managers, as each guild
    //can only use its own. Currently, it'll just contain one.
    private final Map<Long, GuildMusicManager> guildMusicManagers = new HashMap<>();

    private MusicListener() {
        //I believe doing it this way has only registered the yt manager
        //eventually we might want to register more of them that lavaplayer allows
        this.ytSourceManager = new dev.lavalink.youtube.YoutubeAudioSourceManager();
        playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static MusicListener get() {
        if (INSTANCE == null) {
            INSTANCE = new MusicListener();
        }
        return INSTANCE;
    }

    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager musicManager = new GuildMusicManager(playerManager, guild);

            guild.getAudioManager().setSendingHandler(musicManager.getAudioForwarder());

            return musicManager;
        });
    }

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

