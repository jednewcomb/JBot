package me.maktoba.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.TrackScheduler;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class NowPlayingCommand extends Command {

    public NowPlayingCommand(JBot jbot) {
        super(jbot);
        this.name = "nowplaying";
        this.description = "Show info on currently playing song";
        this.type = "music";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        Guild guild = event.getGuild();
        MusicListener  music = MusicListener.get();
        TrackScheduler ts = music.getGuildMusicManager(guild).getTrackScheduler();

        AudioTrackInfo info = ts.getPlayer().getPlayingTrack().getInfo();

        embed.setTitle(info.title);
        embed.setDescription(ts.getPlayer().getPlayingTrack().getInfo().uri);
        embed.setColor(Color.RED);
        event.replyEmbeds(embed.build()).queue();

    }
}
