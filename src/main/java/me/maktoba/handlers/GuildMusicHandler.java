package me.maktoba.handlers;

import net.dv8tion.jda.api.entities.Guild;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicHandler {
    private TrackHandler trackScheduler;
    private MusicHandler musicHandler;

    public GuildMusicHandler(AudioPlayerManager manager, Guild guild) {
        AudioPlayer player = manager.createPlayer();
        trackScheduler = new TrackHandler(player);
        player.addListener(trackScheduler);
        musicHandler = new MusicHandler(player);
    }

    public TrackHandler getTrackScheduler() {
        return trackScheduler;
    }

    public MusicHandler getAudioForwarder() {
        return musicHandler;
    }


}