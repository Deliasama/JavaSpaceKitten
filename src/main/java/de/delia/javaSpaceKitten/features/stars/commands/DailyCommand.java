package de.delia.javaSpaceKitten.features.stars.commands;

import de.delia.javaSpaceKitten.commands.ApplicationCommand;
import de.delia.javaSpaceKitten.commands.ApplicationCommandMethod;
import de.delia.javaSpaceKitten.commands.CommandManager;
import de.delia.javaSpaceKitten.features.stars.tables.Profile;
import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.awt.*;
import java.time.*;
import java.util.Random;

@ApplicationCommand(name = "daily", description = "Hole dir deine Tägliche Belohnung ab!")
public class DailyCommand {

    private final Random random = new Random();

    @ApplicationCommandMethod
    public void onCommand(Bot bot, SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        if (guild == null || member == null) return;

        Instant time = CommandManager.getCooldown("daily", guild.getIdLong(), member.getIdLong()).getLastCall();
        LocalDate date = time.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        if (currentDate.isAfter(date)) {
            Profile profile = Profile.getTable().get(guild.getIdLong(), member.getIdLong());
            int earnStars = 0;
            int newStreak = 1;

            if (currentDate.isEqual(date.plusDays(1))) {
                newStreak = profile.getDailyStreak() + 1;
            }
            earnStars = (random.nextInt(101) + 50);

            // TODO: Daily streak Bonus!

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor(member.getEffectiveName(), null, member.getEffectiveAvatarUrl())
                    .setColor(new Color(96, 83, 240))
                    .setDescription(":success_icon: Du hast jetzt 83 Sterne mehr!\n" +
                            "\n" +
                            "✨ Deine nächsten Bonus-Sterne bekommst du bei einer Daily-Streak von 5!\n" +
                            "\n" +
                            ":star: Sterne: " + profile.getStars() + " → +" + earnStars + " → " + (profile.getStars() + earnStars) + "\n" +
                            ":fire: Streak: " + profile.getDailyStreak() + " → " + (newStreak - profile.getDailyStreak()) + " → " + newStreak);

            profile.setStars(profile.getStars() + earnStars);
            profile.setDailyStreak(newStreak);
            Profile.getTable().update(profile);

            event.replyEmbeds(embedBuilder.build()).queue();
        } else {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor(event.getMember().getEffectiveName(), null, event.getMember().getEffectiveAvatarUrl())
                    .setColor(Color.RED)
                    .setDescription(":failure_icon: Du hast deine Daily-Sterne schon abgeholt" + "\n \n" + ":information_source: Versuche es in " +
                            TimeFormat.RELATIVE.format(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()) + " erneut!");

            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }
    }
}
