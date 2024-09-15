package me.maktoba.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.MusicHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand extends Command {

    public PlayCommand(JBot bot) {
        super(bot);
        this.name = "play";
        this.description = "play song";
    }

    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        AudioChannel myChannel = member.getVoiceState().getChannel();

        if (myChannel == null) {
            event.reply("You are not in a voice channel!").queue();
        }

        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);

        AudioManager manager = guild.getAudioManager();
        manager.setSendingHandler(new MusicHandler(playerManager.createPlayer()));

        //event.reply("Joining voice channel: " + myChannel.getName()).queue();

        manager.openAudioConnection(myChannel);
    }

}
/*
************************************************************************************************************************
Implement an AudioSendHandler
    Mine: In its own class found in "handlers" package - very simple
    Best: In "MusicHandler" found in "handlers" package.
        MusicHandler music = bot.musicListener.getMusic(event, true);


        It looks like:
          PlayCommand   ->  in execute method creates instance of MusicHandler(adps handler)
          PlayCommand   ->  also connects to the VoiceChannel using getVoiceState().getChannel();
          MusicListener ->  retrieves the audio manager, Registers it and opens audio connection


Retrieve the AudioManager
    AudioManager audioManager = guild.getAudioManager();
    Best audiomanager looks like its found in the MusicListener class in listeners package


Open an audio connection audioManager.openAudioConnection()
    audioManager.openAudioConnection(myChannel);
    This is also found in the MusicListener

Register your AudioSendHandler
    This also looks like its found in music listener

You may only use one AudioSendHandler per Guild and not use the same instance on another Guild!

 */
