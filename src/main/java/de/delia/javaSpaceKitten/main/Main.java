package de.delia.javaSpaceKitten.main;

import io.github.cdimascio.dotenv.Dotenv;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;

public class Main {
    public static Bot INSTANCE;
    public static Dotenv DOTENV;
    private static ScheduledExecutorService scheduler;

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate.*").setLevel(Level.SEVERE);
        java.util.logging.Logger.getLogger("jakarta.persistence").setLevel(Level.SEVERE);

        DOTENV = Dotenv.configure().filename("credentials").load();
        String token = DOTENV.get("DISCORD_TOKEN");

        if (token == null) {
            System.out.println("Kein Token angegeben!");
            return;
        }

        INSTANCE = new Bot(token);
        INSTANCE.initTables();

        scheduler = Executors.newScheduledThreadPool(1);
    }

    public static Instant getInstant() {
        return Instant.now();
    }

    public static ScheduledExecutorService getScheduledExecutorService() {
        return scheduler;
    }
}