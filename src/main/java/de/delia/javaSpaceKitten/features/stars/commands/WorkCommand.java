package de.delia.javaSpaceKitten.features.stars.commands;

import de.delia.javaSpaceKitten.commands.ApplicationCommand;
import de.delia.javaSpaceKitten.commands.ApplicationCommandMethod;
import de.delia.javaSpaceKitten.commands.CommandManager;
import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;

@ApplicationCommand(name = "work", description = "Arbeite um Sterne zu erhalten!")
public class WorkCommand {
    @ApplicationCommandMethod
    public void onCommand(Bot bot, SlashCommandInteractionEvent event) {
        if(CommandManager.getCooldown("work", event.getGuild().getIdLong(), event.getMember().getIdLong()).ifCooldown(i -> {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor(event.getMember().getEffectiveName(), null, event.getMember().getEffectiveAvatarUrl())
                    .setDescription(":failure_icon: Du hast schon vor kurzem gearbeitet, ruh dich ein wenig aus!\n" +
                                    " \n" +
                                    ":information_source: Du kannst" + TimeFormat.RELATIVE.format(i) +" wieder arbeiten");

            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }, Duration.ofHours(4)))return;

        CommandManager.getCooldown("work", event.getGuild().getIdLong(), event.getMember().getIdLong()).setCooldown();
    }
}
