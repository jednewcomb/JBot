package me.maktoba.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private AudioPlayer player;
    private BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.player = audioPlayer;
    }

    public AudioPlayer getPlayer() {
        return this.player;
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    /**
     *
     * @param player
     * @param track
     * @param endReason
     *
     * endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
     * endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
     * endReason == STOPPED: The player was stopped.
     * endReason == REPLACED: Another track started playing while this had not finished
     * endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
     *                       clone of this back to your queue
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        player.startTrack(queue.poll(), false);
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void skip() {
        stopTrack();
        queue.poll();
    }

    public void pause() {
        player.setPaused(true);
    }

    public void resume() {
        player.setPaused(false);
    }

    public boolean isPlaying() {
        return player.getPlayingTrack() != null;
    }

    public boolean isPaused() {
        return player.isPaused();
    }

    public void stopTrack() {
        player.stopTrack();
    }

    public void clear() {
        stopTrack();
        queue.clear();
    }

    public void replay() {
        queue.offer(player.getPlayingTrack().makeClone());
    }
}
