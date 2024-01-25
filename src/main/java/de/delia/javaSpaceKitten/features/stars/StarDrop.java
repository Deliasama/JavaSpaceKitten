package de.delia.javaSpaceKitten.features.stars;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StarDrop {
    private final static int minIndex = 80;
    private final static int rangeIndex = 20;
    private final static Map<Long, Integer> starIndex = new HashMap<>();
    private final static Random random = new Random();

    public static void setStarIndex(Guild guild) {
        starIndex.put(guild.getIdLong(), random.nextInt(rangeIndex+1) + minIndex);
    }

    public static boolean addMessage(Guild guild) {
        Integer index = starIndex.get(guild.getIdLong());
        if(index == null) {
            setStarIndex(guild);
            return false;
        }
        if (index <= 1) {
            // SEND MESSAGE
            return true;
        }
        starIndex.put(guild.getIdLong(), index-1);
        return false;
    }

    public static void receiveMessage(MessageReceivedEvent event) {
        if(addMessage(event.getGuild())) {
            // TODO: Logic
            event.getChannel().sendMessage("AHHHHHHHH").queue();
        }
    }
}
