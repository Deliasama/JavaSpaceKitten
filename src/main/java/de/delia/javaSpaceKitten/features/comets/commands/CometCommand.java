package de.delia.javaSpaceKitten.features.comets.commands;

import de.delia.javaSpaceKitten.commands.ApplicationCommand;
import de.delia.javaSpaceKitten.commands.ApplicationCommandMethod;
import de.delia.javaSpaceKitten.features.comets.CometManager;
import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

@ApplicationCommand(name = "comet", description = "Trade with comets!")
public class CometCommand {
    @ApplicationCommandMethod
    public void onCommand(Bot bot, SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (member == null) return;

        EmbedBuilder embedBuilder = CometManager.getCometEmbed(member);

        Button buy1Button = Button.success("comet.buy1", "Buy 1x");
        Button buy5Button = Button.success("comet.buy5", "Buy 5x");

        Button sellButton = Button.danger("comet.sell", "Sell first");

        event.replyEmbeds(embedBuilder.build()).addActionRow(buy1Button, buy5Button, sellButton).queue();
    }
}
