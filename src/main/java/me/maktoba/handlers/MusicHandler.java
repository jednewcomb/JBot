package me.maktoba.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;


//You may only use one AudioSendHandler per Guild and not use the same instance on another Guild!
public class MusicHandler implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private TrackScheduler trackScheduler;
    private AudioFrame lastFrame;

    public MusicHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.trackScheduler = new TrackScheduler(audioPlayer);
    }

    @Override
    public boolean canProvide() {
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return ByteBuffer.wrap(lastFrame.getData());
    }

    @Override
    public boolean isOpus() {
        return true;
    }


}
