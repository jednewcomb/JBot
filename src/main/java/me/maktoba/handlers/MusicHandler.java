package me.maktoba.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class MusicHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final MutableAudioFrame frame = new MutableAudioFrame();
    static final Logger logger = LoggerFactory.getLogger(MusicHandler.class);
    //private int nullFrameCount = 0;

    public MusicHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.frame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        /*
        Audio Lagging Test
        if (!provided) {
            nullFrameCount++;
            logger.info("Null frame count:" + nullFrameCount);
        }
         */
        return audioPlayer.provide(frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return buffer.flip();
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}