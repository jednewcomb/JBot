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

/**
 * PlayCommand adds music tracks to our queue. and then more words here later.
 */
public class PlayCommand extends Command {
    public PlayCommand(JBot bot) {
        super(bot);
        this.name = "play";
        this.description = "play song";
        this.type = "music";
        this.commandOptionData.add
                (new OptionData(OptionType.STRING, "link", "Youtube link with desired audio"));
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
    public void execute(SlashCommandInteractionEvent event) {
        AudioChannel myChannel = event.getMember().getVoiceState().getChannel();
        AudioManager manager = event.getGuild().getAudioManager();
        MusicListener music = MusicListener.get();
        TrackScheduler trackScheduler = music.getGuildMusicManager(event.getGuild()).getTrackScheduler();

        if (myChannel == null) {
            event.reply("You are not in a voice channel!").queue();
        }

        manager.openAudioConnection(myChannel);

        if (trackScheduler.isPaused()) {
            trackScheduler.resume();
        }

        try {
            String trackName = event.getOption("link").getAsString();
            music.addTrack(event.getGuild(), trackName);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        event.reply("Playing ").queue();
    }
}
