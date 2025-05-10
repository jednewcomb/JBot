package me.maktoba.handlers;

import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * CommandCooldownHandler marks when a Guild Member has used a Command,
 *
 */
public class CommandCooldownHandler {
    static final Logger log = LoggerFactory.getLogger(CommandCooldownHandler.class);
    private static final Map<Member, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_DURATION_MS = 5000;

    public static void startCooldown(Member member) {
        cooldowns.put(member, System.currentTimeMillis() + COOLDOWN_DURATION_MS);
        log.info("Cooldown started for " + member.getEffectiveName());
    }

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