package me.maktoba.commands.music;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public class PlayCommand extends Command {

    public PlayCommand(JBot bot) {
        super(bot);
        this.name = "play";
        this.description = "play song";
    }

    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        AudioChannel myChannel = member.getVoiceState().getChannel();

        if (myChannel == null) {
            event.reply("You are not currently in a voice channel!");
        }

        event.getGuild().getAudioManager().openAudioConnection(myChannel);
        event.reply("Joining voice channel: " + myChannel.getName());

    }


}
//I think the best way to register commands is a separate class.
//Command has given us our blueprint for commands but registering them is another story
//CommandRegistry should be your next task for this I think.

//As far as steps go for musicbot via jda, you are at:
//"Sending Audio to an Open Audio Connection"