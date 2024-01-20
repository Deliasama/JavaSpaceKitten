package de.delia.javaSpaceKitten.features.stars.commands;

import de.delia.javaSpaceKitten.commands.ApplicationCommand;
import de.delia.javaSpaceKitten.commands.ApplicationCommandMethod;
import de.delia.javaSpaceKitten.commands.Option;
import de.delia.javaSpaceKitten.features.stars.tables.Profile;
import de.delia.javaSpaceKitten.features.stars.tables.ProfileTable;
import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

@ApplicationCommand(name = "stars", description = "Show how many stars you have")
public class StarsCommand {
    @ApplicationCommandMethod
    public void onCommand(Bot bot, SlashCommandInteractionEvent event, @Option(description = "Member dessen Sterne du sehen möchtest", isRequired = false) User other) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        if (guild == null || member == null) return;

        ProfileTable table = Profile.getTable();
        if (table == null) {
            event.reply(":x: DB FEHLER!").setEphemeral(true).queue();
            return;
        }
        if (other != null) {
            member = guild.getMember(other);
        }

        Profile profile = table.get(guild.getIdLong(), member.getIdLong());

        String text = ":star: Sterne: **" + profile.getStars() + "**\n" +
                ":fire:⠀Daily-Streak: **" + profile.getDailyStreak() + "**\n" +
                ":briefcase:⠀Gearbeitet: **" + profile.getWorked() + "**x\n" +
                ":arrow_up:⠀Votes: **" + profile.getVotes() + "**x\n";

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(event.getMember().getEffectiveName(), null, member.getEffectiveAvatarUrl())
                .setTitle(":bust_in_silhouette:⠀Account von " + member.getEffectiveName() + " (" + member.getUser().getName() + ")")
                .setColor(new Color(96, 83, 240))
                .setDescription(text);

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
