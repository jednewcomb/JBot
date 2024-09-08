package me.maktoba.commands;

import me.maktoba.JBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    public JBot bot;
    public String name;
    public String description;
    public List<OptionData> commandList;
    public List<SubcommandData> subCommands;


    public Command(JBot bot) {
        this.bot = bot;
        this.commandList = new ArrayList<>();
        this.subCommands = new ArrayList<>();
    }

    public abstract void execute(SlashCommandInteractionEvent event);

}
