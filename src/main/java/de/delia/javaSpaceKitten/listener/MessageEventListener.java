package de.delia.javaSpaceKitten.listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEventListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMember().getUser().isBot())return;
        event.getChannel().sendMessage("rawr").queue();
    }
}
