package me.maktoba.commands;

import me.maktoba.JBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

/**
 * The blueprint for Commands.
 */
public abstract class Command {
    public JBot bot;
    public String name;
    public String description;
    public String type;
    public List<OptionData> optionData;
    public Permission requiredPermission; //this might need to be a list eventually
    public List<OptionData> commandOptionData;
    public List<SubcommandData> subCommands;


    public Command(JBot bot) {
        this.bot = bot;
        this.commandOptionData = new ArrayList<>();
        this.subCommands = new ArrayList<>();
    }

    public abstract void execute(SlashCommandInteractionEvent event);

}
