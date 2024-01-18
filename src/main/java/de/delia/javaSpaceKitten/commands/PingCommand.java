package de.delia.javaSpaceKitten.commands;

import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@ApplicationCommand(name = "ping", description = "ping pong!")
public class PingCommand {
    @ApplicationCommandMethod
    public void onCommand(Bot bot, SlashCommandInteractionEvent event) {
        event.reply("pong!").queue();
    }

    @ApplicationCommand(name = "test", description = "test rawr")
    public static class TestCommand {
        @ApplicationCommandMethod
        public void onCommand(Bot bot, SlashCommandInteractionEvent event) {

        }
    }
}
