package de.delia.javaSpaceKitten.main;

import de.delia.javaSpaceKitten.commands.CommandManager;
import de.delia.javaSpaceKitten.commands.PingCommand;
import de.delia.javaSpaceKitten.features.stars.StarsCommand;
import de.delia.javaSpaceKitten.features.stars.StarsTable;
import de.delia.javaSpaceKitten.features.stars.TopCommand;
import de.delia.javaSpaceKitten.listener.GuildReadyEventListener;
import de.delia.javaSpaceKitten.listener.MessageEventListener;
import de.delia.javaSpaceKitten.listener.SlashCommandInteractionEventListener;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.HashMap;
import java.util.Map;

public class Bot {
    public final JDA jda;
    public final CommandManager commandManager;
    public final EntityManagerFactory entityManagerFactory;

    public StarsTable starsTable;



    public Bot(String token) {

        entityManagerFactory = initDB();

        jda = JDABuilder.createDefault(token)
                .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                .setBulkDeleteSplittingEnabled(false)
                .setActivity(Activity.playing("nya~"))
                .build();

        commandManager = new CommandManager(this);

        commandManager.registerCommand(PingCommand.class);
        commandManager.registerCommand(StarsCommand.class);
        commandManager.registerCommand(TopCommand.class);

        jda.addEventListener(new GuildReadyEventListener());
        jda.addEventListener(new MessageEventListener());
        jda.addEventListener(new SlashCommandInteractionEventListener());

        // register stuff

    }

    // Init Tables
    public void initTables() {
        if(entityManagerFactory == null) {
            System.out.println("Daten Bank Fehler!");
            return;
        }
        starsTable = new StarsTable();
    }

    public EntityManagerFactory initDB() {
        Dotenv dotenv = Main.DOTENV;
        String dbUrl = dotenv.get("DATABASE_HOST");
        String dbUsername = dotenv.get("DATABASE_USER");
        String dbPassword = dotenv.get("DATABASE_PASSWORD");

        if(dbUrl == null || dbUsername == null || dbPassword == null)return null;

        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", dbUrl);
        properties.put("javax.persistence.jdbc.user", dbUsername);
        properties.put("javax.persistence.jdbc.password", dbPassword);

        return Persistence.createEntityManagerFactory("PU2", properties);
    }
}
