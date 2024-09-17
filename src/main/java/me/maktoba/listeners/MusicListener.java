package me.maktoba.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import me.maktoba.handlers.MusicHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class MusicListener extends ListenerAdapter {

    private AudioPlayerManager playerManager;

    public MusicListener() {
        //register manager
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    public MusicHandler getMusic(SlashCommandInteractionEvent event, boolean b) {
        Guild guild = event.getGuild();
        AudioManager manager = guild.getAudioManager();

        AudioChannel myChannel = Objects.requireNonNull(event.getMember().getVoiceState()).getChannel();
        manager.openAudioConnection(myChannel);

        MusicHandler mh = new MusicHandler(playerManager.createPlayer());//i might want to not do it this way.

        manager.setSendingHandler(mh);

        manager.openAudioConnection(myChannel);

        return mh;
    }


}


