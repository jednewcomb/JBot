package me.maktoba;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import me.maktoba.commands.CommandRegistry;
import io.github.cdimascio.dotenv.Dotenv;
import me.maktoba.listeners.GuildListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
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
                              GatewayIntent.GUILD_MODERATION);

        //cache all users on startup
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);

        //add EventListeners to builder from registry
        builder.addEventListeners(new CommandRegistry(this), new GuildListener());

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
        } catch(Exception e) {
            System.err.println("invalid token received -> " + e.getMessage());
        }
    }
}