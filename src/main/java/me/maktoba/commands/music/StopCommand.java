package me.maktoba.commands.music;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.TrackScheduler;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class StopCommand extends Command {

    public StopCommand(JBot jbot) {
        super(jbot);
        this.name = "stop";
        this.description = "stops the current playing song and removes it from the queue";
        this.type = "music";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        MusicListener music = MusicListener.get();
        TrackScheduler ts = music.getGuildMusicManager(guild).getTrackScheduler();

        ts.stopTrack();
    }
}
