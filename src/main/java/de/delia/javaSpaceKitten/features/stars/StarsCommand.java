package de.delia.javaSpaceKitten.features.stars;

import de.delia.javaSpaceKitten.commands.ApplicationCommand;
import de.delia.javaSpaceKitten.commands.ApplicationCommandMethod;
import de.delia.javaSpaceKitten.commands.Option;
import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

@ApplicationCommand(name = "stars", description = "Show how many stars you have")
public class StarsCommand {
    @ApplicationCommandMethod
    public void onCommand(Bot bot, SlashCommandInteractionEvent event, @Option(description = "Member dessen Sterne du sehen möchtest", isRequired = false) User other) {
        StarsTable table = bot.starsTable;
        if(table == null) {
            event.reply(":x: DB FEHLER!").setEphemeral(true).queue();
            return;
        }
        Member member = event.getMember();
        if(other != null) {
            member = event.getGuild().getMember(other);
        }

        int stars = table.get(event.getGuild().getIdLong(), member.getIdLong()).getStars();

        StringBuilder text = new StringBuilder()
                .append("⭐ Sterne: **").append(stars).append("**\n")
                ; // TODO Daily streak and other statistics

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(event.getMember().getEffectiveName(), null, member.getEffectiveAvatarUrl())
                .setTitle("\uD83D\uDC64⠀Account von " + member.getEffectiveName() + " (" + member.getUser().getName() + ")")
                .setColor(new Color(96, 83, 240))
                .setDescription(text.toString());

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
