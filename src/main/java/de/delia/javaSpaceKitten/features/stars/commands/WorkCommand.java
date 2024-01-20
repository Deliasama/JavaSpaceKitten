package de.delia.javaSpaceKitten.features.stars.commands;

import de.delia.javaSpaceKitten.commands.ApplicationCommand;
import de.delia.javaSpaceKitten.commands.ApplicationCommandMethod;
import de.delia.javaSpaceKitten.commands.CommandManager;
import de.delia.javaSpaceKitten.features.stars.tables.Profile;
import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;

@ApplicationCommand(name = "work", description = "Arbeite um Sterne zu erhalten!")
public class WorkCommand {

    private final Random random = new Random();

    @ApplicationCommandMethod
    public void onCommand(Bot bot, SlashCommandInteractionEvent event) {
        // Cooldown
        if(CommandManager.getCooldown("work", event.getGuild().getIdLong(), event.getMember().getIdLong()).ifCooldown(i -> {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor(event.getMember().getEffectiveName(), null, event.getMember().getEffectiveAvatarUrl())
                    .setColor(Color.RED)
                    .setDescription(":failure_icon: Du hast schon vor kurzem gearbeitet, ruh dich ein wenig aus!\n" +
                                    " \n" +
                                    ":information_source: Du kannst" + TimeFormat.RELATIVE.format(i) +" wieder arbeiten");

            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }, Duration.ofHours(4)))return;
        Instant nextTime = CommandManager.getCooldown("work", event.getGuild().getIdLong(), event.getMember().getIdLong()).setCooldown().availableIn(Duration.ofHours(4));

        int earnedStars = random.nextInt(31)+20;

        Profile profile = Profile.getTable().get(event.getGuild().getIdLong(), event.getMember().getIdLong());

        String text1 = ":success_icon: Gut gemacht! Du hast für deine Arbeit " + earnedStars + " Sterne bekommen\n" +
                       "⠀⠀ Du hast bisher " + (profile.getWorked()+1) + " mal gearbeitet!\n";

        String text2 = ":star: Sterne: `" + profile.getStars() + " → " + earnedStars + " → " + (profile.getStars()+earnedStars) + "`\n";

        String text3 = ":information_source: Du kannst" + TimeFormat.RELATIVE.format(nextTime) +" wieder arbeiten";

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(event.getMember().getEffectiveName(), null, event.getMember().getEffectiveAvatarUrl())
                .setColor(new Color(96, 83, 240))
                .setDescription(text1 + " \n" + text2 + " \n" + text3);

        // save data
        profile.setStars(profile.getStars()+earnedStars);
        profile.setWorked(profile.getWorked()+1);

        // DON'T FORGET TO UPDATE THE TABLE!
        Profile.getTable().update(profile);

        // send Message
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
