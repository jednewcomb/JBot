package me.maktoba.commands.music;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.TrackScheduler;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

//TODO: -Search for a "music" text channel, and setLogChannel() to it
//TODO:     If there isn't one, we should create it.
//TODO:     OnGuildStartUp or OnGuildJoin is where it should happen
//TODO: -We eventually will want to be able to handle more audio sources
//TODO:     Vimeo, Bandcamp, etc could probably just all be sent to youtube
//TODO:     with some formatting, and would be the easiest way.

/**
 * This command plays a desired song from a link or search query or adds music tracks to our queue.
 */
public class PlayCommand extends Command {
    /**
     * Creates an instance of the PlayCommand.
     * @param bot - Bot singleton to which the command is registered.
     */
    public PlayCommand(JBot bot) {
        super(bot);
        this.name = "play";
        this.description = "play song";
        this.type = "music";
        this.commandOptionData.add(
                new OptionData(OptionType.STRING, "song", "song to search or URL to play", false)
        );
    }

    /**
     * Given that our Music processor is valid, Join the channel the user is in,
     * and add the track to the queue to be played. If we have no arguments, that
     * means our music is currently paused and to just unpause without adding anything.
     * @param event - Event trigger.
     */
    public void execute(SlashCommandInteractionEvent event)   {
        MusicListener music = MusicListener.get();
        if (music == null) return;

        AudioChannel userChannel = Optional.ofNullable(event.getMember())
                .map(member -> member.getVoiceState().getChannel())
                .orElse(null);

        if (userChannel == null) {
            event.reply("Join a Voice Channel to use music commands!").queue();
            return;
        }

        AudioManager manager = event.getGuild().getAudioManager();
        manager.openAudioConnection(userChannel);

        TrackScheduler scheduler = music.getGuildMusicManager(event.getGuild()).getTrackScheduler();
        if (scheduler.getQueue().size() > 100) {
            event.reply("Queue can be no greater than 100 songs.").queue();
            return;
        }

        if (checkPaused(scheduler)) {
            //its paused, continue playing
            if (event.getOption("song") == null) {
                scheduler.resume();
                event.reply("Playback resumed.").setEphemeral(true).queue();
                return;
            }
        }

        music.loadTrack(event, event.getGuild(), getSongURL(event));
    }

    /**
     * Format a correct URL or search query.
     * @param event - Event trigger.
     * @return - The Formatted URL or search query.
     */
    private String getSongURL(SlashCommandInteractionEvent event) {
        String url;
        String song = (event.getOption("song")).getAsString();

        try {

            url = new URL(song).toString();
            if (url.contains("https://soundcloud.com/")) {
                url = handleSoundCloud(url, event);
            }

        } catch (MalformedURLException e) {
            //its likely a song name, so we create a youtube search query
            return "ytsearch: " + song;
        }
        return url;
    }

    /**
     * Handle SoundCloud links by sending them to YouTube.
     * @param url - A soundcloud url.
     * @param event - Event trigger.
     * @return - A formatted URL.
     */
    private String handleSoundCloud(String url, SlashCommandInteractionEvent event) {
        String[] contents = url.split("/");
        try {
            url = "ytsearch:" + contents[3] + "/" + contents[4];
        } catch (IndexOutOfBoundsException e) {
            event.reply("The soundcloud link could not be loaded, incorrect url formatting")
                    .setEphemeral(true)
                    .queue();
        }
        return url;
    }

    /**
     * Check if the music is paused.
     * @param scheduler - TrackScheduler which controls the flow of music.
     * @return - boolean telling is whether the music is paused or not.
     */
    private boolean checkPaused(TrackScheduler scheduler) {
        return scheduler.isPaused();
    }
}