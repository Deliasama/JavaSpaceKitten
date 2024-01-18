package de.delia.javaSpaceKitten.commands;

import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@ApplicationCommand(name = "hey", description = "hey ho hey hoo")
public class HeyCommand {
    @ApplicationCommandMethod
    public void execute(Bot bot, SlashCommandInteractionEvent event, @Option(description = "Typ") User user) {
        Member member = event.getGuild().getMember(user);
        bot.jda.getUserById(1).openPrivateChannel().flatMap(c ->
                c.sendMessage("aaaaaaaaaaaaa")
        ).queue();

        bot.jda.retrieveUserById(user.getId()).queue(u ->
                user.openPrivateChannel().queue(c ->
                        c.sendMessage("AHHHHH").queue()
                )
        );
        event.reply("hiiiiiiiiiiii " + member.getEffectiveName()).queue();
    }
}
