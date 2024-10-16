package me.maktoba.commands.music;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.TrackScheduler;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SkipCommand extends Command {

    public SkipCommand(JBot bot) {
        super(bot);
        this.name = "skip";
        this.description = "skip to the next song";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

    }


}
