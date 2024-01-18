package de.delia.javaSpaceKitten.listener;

import de.delia.javaSpaceKitten.main.Main;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildReadyEventListener extends ListenerAdapter {
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        Main.INSTANCE.commandManager.upsertCommands(event.getGuild());
    }
}
