package me.maktoba.commands.moderation;

import me.maktoba.JBot;
import me.maktoba.commands.Command;
import me.maktoba.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Objects;

public class UnbanCommand extends Command {

    public UnbanCommand(JBot bot) {
        super(bot);
        this.name = "unban";
        this.description = "unban specified user";
        this.type = "moderation";
        this.requiredPermission = Permission.BAN_MEMBERS;//this might be right
        this.commandOptionData.add(new OptionData(OptionType.USER, "banned", "banned user", true));
    }

    //TODO: We may need to address caching new users that are joining
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        User bannedUser = Objects.requireNonNull(event.getOption("banned")).getAsUser();

        //Make sure they're a banned user
        Member bannedUserAsMember = event.getOption("banned").getAsMember();
        if (event.getGuild().getMembers().contains(bannedUserAsMember)) {
            EmbedBuilder builder = EmbedUtil.createErrorEmbed("User is not banned!");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        //TODO: This should go in moderationhandler probably
        Objects.requireNonNull(event.getGuild()).unban(bannedUser).queue();

        EmbedBuilder builder = EmbedUtil.createSuccessEmbed(
                String.format("User %s was unbanned", bannedUser.getEffectiveName())
        );
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }
}
