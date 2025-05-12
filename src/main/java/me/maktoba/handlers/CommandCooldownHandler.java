package me.maktoba.handlers;

import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * CommandCooldownHandler marks when a Guild Member has used a Command
 * by taking the current time and adding 5 seconds. If that time hasn't
 * yet been reached and that Member has attempted to run another command,
 * we ping them they have a "cooldown".
 */
public class CommandCooldownHandler {
    static final Logger log = LoggerFactory.getLogger(CommandCooldownHandler.class);
    private static final Map<Member, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_DURATION_MS = 3000;

    /**
     * Map the Member and the current time plus 5 seconds for later comparison.
     * @param member - Member who used the command.
     */
    public static void startCooldown(Member member) {
        cooldowns.put(member, System.currentTimeMillis() + COOLDOWN_DURATION_MS);
        log.info("Cooldown started for " + member.getEffectiveName());
    }

    /**
     * Check if 5 seconds have passed since the Member has used a command.
     * @param member - Member who used the command.
     * @return - boolean denoting whether this member is on cooldown.
     */
    public static boolean memberHasCooldown(Member member) {
        Long end = cooldowns.get(member);
        if (end == null) return false;

        if (System.currentTimeMillis() >= end) {
            cooldowns.remove(member);
            return false;
        }
        return true;
    }

}