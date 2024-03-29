package de.delia.javaSpaceKitten.features.comets;

import de.delia.javaSpaceKitten.features.comets.tables.Comet;
import de.delia.javaSpaceKitten.features.comets.tables.CometPrice;
import de.delia.javaSpaceKitten.features.stars.tables.Profile;
import de.delia.javaSpaceKitten.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.awt.*;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CometManager {
    public static Random random = new Random();

    public static void calculateNewPrice(Guild guild) {
        CometPrice cometPrice = CometPrice.getTable().get(guild.getIdLong());

        long oldPrice = cometPrice.getPrice();
        long traded = cometPrice.getTraded();
        long randomFactor = (random.nextInt(11) - 5);

        long newPrice = (oldPrice + Math.round(0.1d * traded) + randomFactor);

        System.out.println("Traded: " + traded + " | RandomFactor: " + randomFactor + " | PriceChange: " + (newPrice - oldPrice));

        cometPrice.setPrice(newPrice);
        cometPrice.setTraded(0L);
        CometPrice.getTable().update(cometPrice);
    }

    public static EmbedBuilder getCometEmbed(Member member) {
        CometPrice cometPrice = Main.INSTANCE.cometPriceTable.get(member.getGuild().getIdLong());
        List<Comet> comets = Main.INSTANCE.cometTable.get(member.getGuild().getIdLong(), member.getIdLong());

        StringBuilder stringBuilder = new StringBuilder();
        if (comets.isEmpty()) stringBuilder.append(":x: Du hast keine Kometen!");
        for (Comet comet : comets) {
            stringBuilder.append(TimeFormat.RELATIVE.format(comet.getBoughtTime().atZone(ZoneId.systemDefault()))).append(" | ").append(comet.getStackSize()).append("x | ").append(comet.getBoughtValue()).append("\n");
        }

        return new EmbedBuilder()
                .setTitle("☄\uFE0F Kometen")
                .setColor(new Color(96, 83, 240))
                .setAuthor(member.getEffectiveName(), null, member.getEffectiveAvatarUrl())
                .addField("Aktueller Preis:", String.valueOf(cometPrice.getPrice()), false)
                .addField("Deine Kometen:", stringBuilder.toString(), false);
    }

    public static void buy(ButtonInteractionEvent event, int amount) {
        Member member = event.getMember();

        CometPrice cometPrice = Main.INSTANCE.cometPriceTable.get(event.getGuild().getIdLong());
        Profile profile = Main.INSTANCE.profileTable.get(member.getGuild().getIdLong(), member.getIdLong());

        int price = (int) (cometPrice.getPrice() * amount);
        if (price <= profile.getStars()) {
            // pay price
            profile.setStars(profile.getStars() - price);
            Main.INSTANCE.profileTable.update(profile);

            // add comet
            Main.INSTANCE.cometTable.save(new Comet(member.getGuild().getIdLong(), member.getIdLong(), amount, Main.getInstant(), cometPrice.getPrice()));

            // change traded
            cometPrice.setTraded(cometPrice.getTraded() + amount);
            Main.INSTANCE.cometPriceTable.update(cometPrice);

            // refresh embed
            event.editMessageEmbeds(getCometEmbed(member).build()).queue();
        } else {
            event.reply("Du hast nicht so viele Sterne!").setEphemeral(true).queue();
        }
    }

    public static void sellOldest(ButtonInteractionEvent event) {
        Member member = event.getMember();

        CometPrice cometPrice = CometPrice.getTable().get(member.getGuild().getIdLong());
        List<Comet> comets = Main.INSTANCE.cometTable.get(member.getGuild().getIdLong(), member.getIdLong());
        Profile profile = Main.INSTANCE.profileTable.get(member.getGuild().getIdLong(), member.getIdLong());

        if (comets.isEmpty()) {
            event.reply("Du hast keine Kometen!").setEphemeral(true).queue();
            return;
        }

        int amount = comets.get(0).getStackSize();
        int stars = (int) (amount * cometPrice.getPrice());
        Main.INSTANCE.cometTable.remove(comets.get(0));

        profile.setStars(profile.getStars() + stars);
        Main.INSTANCE.profileTable.update(profile);

        cometPrice.setTraded(cometPrice.getTraded() - amount);
        Main.INSTANCE.cometPriceTable.update(cometPrice);

        event.editMessageEmbeds(getCometEmbed(member).build()).queue();
    }

    public static void scheduleUpdatePrice(Guild guild) {
        long delay = 3600000 - (System.currentTimeMillis() % 3600000);
        Main.getScheduledExecutorService().scheduleAtFixedRate(() -> {
            calculateNewPrice(guild);
        }, delay, 3600000, TimeUnit.MILLISECONDS);
    }
}
