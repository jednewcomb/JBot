package me.maktoba.commands.music;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.handlers.TrackHandler;
import me.maktoba.listeners.MusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

//TODO: Maybe we could add some options for this to clear sections
//TODO: of the Queue rather than just the whole thing at once.
//TODO: Could that be its own command?

/**
 * This command Clears the current queue.
 */
public class ClearCommand extends Command {

    /**
     * Creates an Instance of ClearCommand.
     * @param bot - Bot singleton.
     */
    public ClearCommand(JBot bot) {
        super(bot);
        this.name = "clear";
        this.description = "clear the current queue";
        this.type = "music";
    }

    /**
     * Clear the queue if it is not already empty.
     * @param event - Event trigger.
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MusicListener music = MusicListener.get();
        if (music == null) return;

        Guild guild = event.getGuild();
        TrackHandler scheduler = music.getGuildMusicManager(guild).getTrackScheduler();

        if (scheduler.getQueue().isEmpty()) {
            event.reply("Queue is currently empty.")
                    .setEphemeral(true)
                    .queue();
        }

        scheduler.clear();
        event.reply("Queue cleared.").setEphemeral(true).queue();
    }
}
