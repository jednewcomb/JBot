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

/**
 * PlayCommand adds music tracks to our queue. and then more words here later.
 */
public class PlayCommand extends Command {
    public PlayCommand(JBot bot) {
        super(bot);
        this.name = "play";
        this.description = "play song";
        this.type = "music";
        this.commandOptionData.add(new OptionData(OptionType.STRING, "song", "song to search or URL to play"));
    }

    /**
     * Play Command gets the guild and member
     * uses the member to get the voice state and channel of user who input command
     * null checks if user is in a voice channel, prints message if not
     * uses guild to get the audio manager
     * manager opens the audio connection
     * music listener created
     * prints message
     * adds the track
     *
     * @param event - The slash command (This command).
     */
    public void execute(SlashCommandInteractionEvent event)   {
        MusicListener music = MusicListener.get();
        if (music == null) return;

        AudioChannel userChannel = event.getMember().getVoiceState().getChannel();
        if (userChannel == null) {
            event.reply("You are not in a voice channel").queue();
            return;
        }
        AudioManager manager = event.getGuild().getAudioManager();
        manager.openAudioConnection(userChannel);

        TrackScheduler trackScheduler = music.getGuildMusicManager(event.getGuild()).getTrackScheduler();

        if (trackScheduler.getQueue().size() > 100) {
            event.reply("Queue can be no greater than 100 songs.").queue();
            return;
        }

        String song = (event.getOption("song")).getAsString();
        String url;
        try {
            if (checkPaused(trackScheduler)) {
                //its paused, with no args, continue playing
                if (commandOptionData == null) {
                    trackScheduler.resume();
                }
            }
            url = new URL(song).toString();

        } catch (MalformedURLException e) {
            //its not a url, its song name
            url = "ytsearch: " + song;
            //maybe add setlogchannel here? if we want to have our
            //song queries be in a specific channel.
            music.loadTrack(event, event.getGuild(), url);
        }

        if (url.contains("https://soundcloud.com/")) {
            String[] contents = url.split("/");
            url = "ytsearch:" + contents[3] + "/" + contents[4];
        }
        music.loadTrack(event, event.getGuild(), url);
    }

    private boolean checkPaused(TrackScheduler trackScheduler) {
        return trackScheduler.isPaused();
    }
}