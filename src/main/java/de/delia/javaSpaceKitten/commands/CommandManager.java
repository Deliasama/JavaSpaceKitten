package de.delia.javaSpaceKitten.commands;

import de.delia.javaSpaceKitten.commands.cooldown.Cooldown;
import de.delia.javaSpaceKitten.main.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    public final Bot bot;
    private final Map<String, Command<?>> applicationCommands;

    public CommandManager(Bot bot) {
        this.bot = bot;
        this.applicationCommands = new HashMap<>();
    }

    public static Cooldown getCooldown(String name, long guildId, long memberId) {
        return Cooldown.getCooldownTable().get(name, guildId, memberId);
    }

    public void executeCommand(String name, SlashCommandInteractionEvent event) {
        if (applicationCommands.containsKey(name)) {
            try {
                applicationCommands.get(name).performCommand(event);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T> void registerCommand(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(ApplicationCommand.class)) return;
        ApplicationCommand applicationCommand = clazz.getAnnotation(ApplicationCommand.class);
        String name = applicationCommand.name();
        String description = applicationCommand.description();

        if (applicationCommands.containsKey(name)) return;
        applicationCommands.put(name, new Command<T>(name, description, clazz, null, this));
    }

    public void upsertCommands(Guild guild) {
        for (Map.Entry<String, Command<?>> entry : applicationCommands.entrySet()) {
            entry.getValue().upsertCommand(guild);
        }
    }


    public <T> Object mapOption(OptionMapping mapping, T type) {
        if (type.equals(String.class)) {
            return mapping.getAsString();
        }
        if (type.equals(Integer.class)) {
            return mapping.getAsInt();
        }
        if (type.equals(Channel.class)) {
            return mapping.getAsChannel();
        }
        if (type.equals(Role.class)) {
            return mapping.getAsRole();
        }
        if (type.equals(User.class)) {
            return mapping.getAsUser();
        }
        return null;
    }

    public Object mapOption(OptionMapping mapping) {
        if (mapping == null) return null;

        switch (mapping.getType()) {
            case STRING:
                return mapping.getAsString();
            case INTEGER:
                return mapping.getAsInt();
            case CHANNEL:
                return mapping.getAsChannel();
            case ROLE:
                return mapping.getAsRole();
            case USER:
                return mapping.getAsUser();
            case BOOLEAN:
                return mapping.getAsBoolean();
            default:
                return null;
        }
    }

    public <T> OptionType mapToOption(T type) {
        if (type.equals(String.class)) {
            return OptionType.STRING;
        }
        if (type.equals(Integer.class) || type.equals(int.class)) {
            return OptionType.INTEGER;
        }
        if (type.equals(Channel.class)) {
            return OptionType.CHANNEL;
        }
        if (type.equals(Role.class)) {
            return OptionType.ROLE;
        }
        if (type.equals(User.class)) {
            return OptionType.USER;
        }
        if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return OptionType.BOOLEAN;
        }
        return null;
    }
}
