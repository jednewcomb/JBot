package me.maktoba.commands.music;

import me.maktoba.commands.Command;
import me.maktoba.JBot;
import me.maktoba.handlers.TrackScheduler;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ReplayCommand extends Command {

    public ReplayCommand(JBot jbot) {
        super(jbot);
        this.name = "replay";
        this.description = "replay the current playing song";
        this.type = "music";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        MusicListener music = MusicListener.get();
        TrackScheduler ts = music.getGuildMusicManager(guild).getTrackScheduler();

        ts.replay();
    }


}
