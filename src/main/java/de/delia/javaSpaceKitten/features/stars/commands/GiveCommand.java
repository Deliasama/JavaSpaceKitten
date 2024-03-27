package de.delia.javaSpaceKitten.features.stars.commands;

import de.delia.javaSpaceKitten.commands.ApplicationCommand;
import de.delia.javaSpaceKitten.commands.ApplicationCommandMethod;
import de.delia.javaSpaceKitten.commands.Option;
import de.delia.javaSpaceKitten.features.stars.tables.Profile;
import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@ApplicationCommand(name = "give", description = "Gebe Sterne an andere User!")
public class GiveCommand {

    @ApplicationCommandMethod
    public void onCommand(Bot bot, SlashCommandInteractionEvent event,
                          @Option(description = "User welchem du Sterne geben möchtest", isRequired = true) User user,
                          @Option(description = "Wie viele Sterne du geben möchtest") Integer stars
    ) {
        Member member = event.getMember();
        Guild guild = event.getGuild();
        if (member == null || guild == null || stars == null) return;
        Member other = guild.getMember(user);

        if (stars <= 0) {
            event.reply("Du musst mindestens ein Stern geben!").setEphemeral(true).queue();
            return;
        }

        Profile memberProfile = Profile.getTable().get(guild.getIdLong(), member.getIdLong());
        Profile otherProfile = Profile.getTable().get(guild.getIdLong(), other.getIdLong());

        if (stars > memberProfile.getStars()) {
            event.reply("Du hast nicht so viele Sterne!").setEphemeral(true).queue();
            return;
        }
        otherProfile.setStars(otherProfile.getStars() + stars);
        memberProfile.setStars(memberProfile.getStars() - stars);

        Profile.getTable().update(otherProfile);
        Profile.getTable().update(memberProfile);


        // TODO: Better message, maybe a embed :3
        event.reply(member.getEffectiveName() + " gibt " + other.getEffectiveName() + " " + stars + " Sterne!").queue();
    }
}
