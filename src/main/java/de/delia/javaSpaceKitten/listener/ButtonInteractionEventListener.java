package de.delia.javaSpaceKitten.listener;

import de.delia.javaSpaceKitten.features.stars.tables.Profile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class ButtonInteractionEventListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(event.getMember() == null)return;
        Member member = event.getMember();

        if(Objects.requireNonNull(event.getButton().getId()).startsWith("starDrop")) {

            Profile profile = Profile.getTable().get(member.getGuild().getIdLong(), member.getIdLong());

            if(event.getButton().getId().equals("starDrop.x")) {
                int stars = new Random().nextInt(41)+20;

                profile.setStars(profile.getStars()+stars);

                Profile.getTable().update(profile);

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("\uD83C\uDF20 Sternschnuppe eingesammelt! \uD83C\uDF20")
                        .setColor(new Color(96, 83, 240))
                        .setDescription(member.getAsMention() + " hat **" + stars + " Sterne** gefunden!");

                event.editMessageEmbeds(embedBuilder.build())
                        .setActionRow(event.getMessage().getActionRows().get(0).asDisabled().getComponents())
                        .queue();
                return;
            }

            event.reply("Du bist zu dumm!").queue();

        }
    }
}
