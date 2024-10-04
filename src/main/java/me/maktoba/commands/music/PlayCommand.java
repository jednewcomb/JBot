package me.maktoba.commands.music;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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
        this.commandOptionData.add
                (new OptionData(OptionType.STRING, "link", "Youtube link with desired audio", true));
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
        Guild guild = event.getGuild();
        Member member = event.getMember();

        //PlayCommand   ->  connects to the VoiceChannel using getVoiceState().getChannel();
        AudioChannel myChannel = member.getVoiceState().getChannel();

        if (myChannel == null) {
            event.reply("You are not in a voice channel!").queue();
        }

        String trackName = event.getOption("link").getAsString();

        AudioManager manager = guild.getAudioManager();

        manager.openAudioConnection(myChannel);

        MusicListener music = MusicListener.get();
        event.reply("Playing").queue();

        if (music.getGuildMusicManager(guild).getTrackScheduler().isPaused()) {
            music.getGuildMusicManager(guild).getTrackScheduler().unpause();
        }
        else {
            music.addTrack(guild, trackName);
        }


    }

    public void checkPaused() {
        //we should see if we can:
            //add this.commandOptionData isRequired to true based on whether or not the current track is paused
    }

}
