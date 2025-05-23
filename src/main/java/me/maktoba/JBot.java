package me.maktoba;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import me.maktoba.commands.CommandRegistry;
import io.github.cdimascio.dotenv.Dotenv;
import me.maktoba.listeners.GuildListener;
import me.maktoba.listeners.JoinLeaveListener;
import me.maktoba.listeners.MessageReactionListener;
import me.maktoba.listeners.ModerationListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class JBot {
    private final Dotenv config;
    private final ShardManager shardManager;

    public JBot() throws LoginException {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token)
                .setAudioSendFactory(
                        new NativeAudioSendFactory()
                );

        builder.enableIntents(GatewayIntent.GUILD_MEMBERS,
                              GatewayIntent.GUILD_MESSAGES,
                              GatewayIntent.GUILD_PRESENCES,
                              GatewayIntent.GUILD_VOICE_STATES,
                              GatewayIntent.GUILD_MODERATION,
                              GatewayIntent.DIRECT_MESSAGES);

        //cache all users on startup
        builder.setMemberCachePolicy(MemberCachePolicy.ONLINE);
        builder.setChunkingFilter(ChunkingFilter.ALL);

        //add EventListeners to builder from registry
        builder.addEventListeners(new CommandRegistry(this),
                                  new GuildListener(),
                                  new JoinLeaveListener(),
                                  new ModerationListener(),
                                  new MessageReactionListener());

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("JBot"));
        shardManager = builder.build();
    }

    public Dotenv getConfig() {
        return config;
    }

    public static void main(String[] args) {
        try {
            new JBot();
        }

        catch(LoginException ex) {
            System.err.printf("Ensure that you are using the correct token! %s.", ex.getMessage());
            System.exit(1);
        }

        catch(IllegalArgumentException ex) {
            System.err.printf("Configuration issue. " +
                    "JBot requires no args to be used %s.", ex.getMessage());
            System.exit(1);
        }

        catch(ErrorResponseException ex) {
            System.err.printf("Invalid response returned. Check internet connection %s", ex.getMessage());
            System.exit(1);
        }
    }

}