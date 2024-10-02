package me.maktoba;

import java.io.IOException;

import me.maktoba.commands.CommandRegistry;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class JBot {

    //Environmental Variable for privacy
    private final Dotenv config;

    /**
     * ShardManager is basically the builder which dictates
     * what our bot does. It could be replaced by a JDA builder in
     * older versions of JDA I believe.
     */
    private final ShardManager shardManager;

    public JBot() throws LoginException {
        config = Dotenv.configure().load();

        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("YOU"));

        builder.enableIntents(GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES);

        builder.addEventListeners(new CommandRegistry(this));

        //before we had this set to only online users
        //so for some reason, when we were calling
        //member.getAudioChannel in PlayCommand
        //with that policy, it kept returning null.
        //it returns null because we weren't cacheing
        //users correctly I guess. This will need to be
        //changed and addressed in the future
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        //cache all users on startup
        builder.setChunkingFilter(ChunkingFilter.ALL);

        shardManager = builder.build();

    }

    public Dotenv getConfig() {
        return config;
    }

    public static void main(String[] args) throws IOException {
        try {
            new JBot();
        } catch(Exception e) {
            System.err.println("invalid token received");
        }
    }
}