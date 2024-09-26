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

import java.net.URI;
import java.net.URISyntaxException;

public class PlayCommand extends Command {

    public PlayCommand(JBot bot) {
        super(bot);
        this.name = "play";
        this.description = "play song";
        this.commandOptionData.add(new OptionData
                (OptionType.STRING,
                            "link",
                        "Youtube link with desired audio",
                         true));
    }

    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        //PlayCommand   ->  connects to the VoiceChannel using getVoiceState().getChannel();
        AudioChannel myChannel = member.getVoiceState().getChannel();

        if (myChannel == null) {
            event.reply("You are not in a voice channel!").queue();
        }

        String trackName = event.getOption("link").getAsString();

        try {
            new URI(trackName);
        } catch(URISyntaxException e) {
            trackName = "ytsearch:" + trackName;
        }

        AudioManager manager = guild.getAudioManager();

        manager.openAudioConnection(myChannel);

        MusicListener music = MusicListener.get();
        event.reply("Playing").queue();
        music.addTrack(guild, trackName);

    }

}
