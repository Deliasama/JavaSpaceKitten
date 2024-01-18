package de.delia.javaSpaceKitten.commands;

import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

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

        }
    }
}
