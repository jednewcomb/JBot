package me.maktoba.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.AudioPlayerSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

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
        manager.setSendingHandler(new AudioPlayerSendHandler(playerManager.createPlayer()));

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


Connecting to a VoiceChannel
    It seems best is to use event.getMember().getVoiceState().getChannel();

Retrieve the AudioManager
    AudioManager audioManager = guild.getAudioManager();
    Best audiomanager looks like its found in the MusicListener class in listeners package


Open an audio connection audioManager.openAudioConnection()
    audioManager.openAudioConnection(myChannel);
    This is also found in the MusicListener

Register your AudioSendHandler
    This also looks like its found in music listener

You may only use one AudioSendHandler per Guild and not use the same instance on another Guild!


    EXAMPLE
 public class MusicBot extends ListenerAdapter
{
    public static void main(String[] args)
    throws IllegalArgumentException, LoginException, RateLimitedException
    {
        JDABuilder.createDefault(args[0]) // Use token provided as JVM argument
            .addEventListeners(new MusicBot()) // Register new MusicBot instance as EventListener
            .build(); // Build JDA - connect to discord
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        // Make sure we only respond to events that occur in a guild
        if (!event.isFromGuild()) return;
        *
        // This makes sure we only execute our code when someone sends a message with "!play"
        *
        if (!event.getMessage().getContentRaw().startsWith("!play")) return;
        *
        // Now we want to exclude messages from bots since we want to avoid command loops in chat!
        // this will include own messages as well for bot accounts
        // if this is not a bot make sure to check if this message is sent by yourself!
        *
        if (event.getAuthor().isBot()) return;
        Guild guild = event.getGuild();
        // This will get the first voice channel with the name "music"
        // matching by voiceChannel.getName().equalsIgnoreCase("music")
        *
        VoiceChannel channel = guild.getVoiceChannelsByName("music", true).get(0);
        AudioManager manager = guild.getAudioManager();

        // MySendHandler should be your AudioSendHandler implementation
        manager.setSendingHandler(new MySendHandler());
        // Here we finally connect to the target voice channel
        // and it will automatically start pulling the audio from the MySendHandler instance

        *********manager.openAudioConnection(channel);********
    }
}

 */
