package de.delia.javaSpaceKitten.commands;

import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@ApplicationCommand(name = "ping", description = "ping pong!")
public class PingCommand {
    @ApplicationCommandMethod
    public void onCommand(Bot bot, SlashCommandInteractionEvent event) {
        event.reply("pong!").queue();
    }

    @ApplicationCommand(name = "test", description = "test rawr")
    @ApplicationCommandCooldown(time = 10, timeUnit = TimeUnit.MINUTES)
    public static class TestCommand {
        @ApplicationCommandMethod
        public void onCommand(Bot bot, SlashCommandInteractionEvent event) {
            Cooldown cooldown = CommandManager.getCooldown("ping.test", event.getGuild().getIdLong(), event.getMember().getIdLong());

            if(cooldown.ifCooldown(i -> {
                event.reply("Du kannst den Befehl erst wieder " + TimeFormat.RELATIVE.format(i.toEpochMilli()) + " ausführen!").queue();
            }, Duration.ofMillis(30*1000))) {
                return;
            }

            event.reply("aaaaaaaaaa").queue();
            cooldown.setCooldown();
        }
    }
}
