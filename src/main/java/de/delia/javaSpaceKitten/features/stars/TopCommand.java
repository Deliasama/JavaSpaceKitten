package de.delia.javaSpaceKitten.features.stars;

import de.delia.javaSpaceKitten.commands.ApplicationCommand;
import de.delia.javaSpaceKitten.commands.ApplicationCommandMethod;
import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.List;

@ApplicationCommand(name = "top", description = "Zeige die Top User")
public class TopCommand {
    @ApplicationCommandMethod
    public void onCommand(Bot bot, SlashCommandInteractionEvent event) {
        if(Stars.getTable() == null) {
            event.reply(":x: DB FEHLER!").setEphemeral(true).queue();
            return;
        }

        List<Stars> topList = Stars.getTable().getSorted(event.getGuild().getIdLong());
        System.out.println(topList);

        String filter = "Sterne";
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("\uD83D\uDCAB Top 10 User (Filter: " + filter +")"+" \uD83D\uDCAB")
                .setColor(new Color(96, 83, 240));

        int ownPlace = 0;

        for(int i = 0; i<topList.size(); i++) {
            Stars s = topList.get(i);
            System.out.println(s.getStars());
            if(s.getMemberId() == event.getMember().getIdLong()) ownPlace = (i+1);
            if(i<10) {
                embedBuilder.addField((i+1) + ". " + event.getGuild().getMember(UserSnowflake.fromId(s.getMemberId())).getEffectiveName(), "⭐"+s.getStars(), false);
                // TODO other infos
                if(i>=9 && ownPlace != 0)break;
            }
        }

        embedBuilder.setDescription("Du bist __" + ownPlace + ". Platz__");

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
