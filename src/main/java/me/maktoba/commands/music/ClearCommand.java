package me.maktoba.commands.music;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.TrackScheduler;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ClearCommand extends Command {
    public ClearCommand(JBot bot) {
        super(bot);
        this.name = "clear";
        this.description = "clear the current queue";
        this.type = "music";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        MusicListener music = MusicListener.get();
        TrackScheduler ts = music.getGuildMusicManager(guild).getTrackScheduler();

        ts.clear();
    }
}
