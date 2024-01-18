package de.delia.javaSpaceKitten.listener;

import de.delia.javaSpaceKitten.main.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandInteractionEventListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String name = event.getName();
        System.out.println(event.getFullCommandName());
        Main.INSTANCE.commandManager.executeCommand(name, event);
    }
}
