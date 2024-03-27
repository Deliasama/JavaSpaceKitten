package de.delia.javaSpaceKitten.features.stars;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StarDrop {
    private final static int minIndex = 80;
    private final static int rangeIndex = 20;
    private final static Map<Long, Integer> starIndex = new HashMap<>();
    private final static Random random = new Random();
    private static final ArrayList<Emoji> emojiList;

    static {
        emojiList = new ArrayList<>();
        emojiList.add(Emoji.fromUnicode("⭐"));
        emojiList.add(Emoji.fromUnicode("\uD83C\uDF1F"));
        emojiList.add(Emoji.fromUnicode("✨"));
    }

    public static void setStarIndex(Guild guild) {
        starIndex.put(guild.getIdLong(), random.nextInt(rangeIndex + 1) + minIndex);
    }

    public static boolean addMessage(Guild guild) {
        Integer index = starIndex.get(guild.getIdLong());
        if (index == null) {
            setStarIndex(guild);
            return false;
        }

        if (index <= 1) {
            setStarIndex(guild);
            return true;
        }
        starIndex.put(guild.getIdLong(), index - 1);
        return false;
    }

    public static void receiveMessage(MessageReceivedEvent event) {
        if (addMessage(event.getGuild())) {
            Emoji emoji = emojiList.get(random.nextInt(emojiList.size()));

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("\uD83C\uDF20 Sternschnuppe incomming! \uD83C\uDF20")
                    .setDescription("Klicke " + emoji.getFormatted() + " an um sie zu fangen!")
                    .setColor(new Color(96, 83, 240));

            List<Button> buttons = new ArrayList<>();
            for (Emoji e : emojiList) {
                if (e.equals(emoji)) {
                    buttons.add(Button.primary("starDrop.x", e));
                } else {
                    buttons.add(Button.primary("starDrop." + e.getName(), e));
                }
            }
            event.getChannel().sendMessageEmbeds(embedBuilder.build())
                    .setActionRow(buttons)
                    .queue(m -> {
                        m.editMessageComponents().setActionRow(m.getActionRows().get(0).asDisabled().getComponents()).queueAfter(15, TimeUnit.MINUTES);
                    });
        }
    }
}
