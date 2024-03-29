package de.delia.javaSpaceKitten.commands;

import de.delia.javaSpaceKitten.commands.cooldown.ApplicationCommandCooldown;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Command<T> {

    @NotNull
    public final Class<T> clazz;
    public final List<OptionData> options;
    private final CommandManager commandManager;
    @NotNull
    private final String name;
    @NotNull
    private final String description;
    @NotNull
    private final String fullName;
    private final List<Command<?>> subCommands;

    private Method method = null;

    private Duration cooldownLength = null;
    private String coolDownMessage = null;

    // T commandClass;

    public Command(@NotNull String name, @NotNull String description, Class<T> clazz, String commandParentName, CommandManager manager) {
        this.name = name;
        this.description = description;
        this.clazz = clazz;
        this.options = new ArrayList<>();
        this.commandManager = manager;
        this.subCommands = new ArrayList<>();
        if (commandParentName == null) {
            fullName = name;
        } else {
            fullName = commandParentName + "." + name;
        }


        if (clazz.isAnnotationPresent(ApplicationCommandCooldown.class)) {
            ApplicationCommandCooldown commandCooldown = clazz.getAnnotation(ApplicationCommandCooldown.class);
            cooldownLength = Duration.ofMillis(commandCooldown.timeUnit().toMillis(commandCooldown.time()));
            coolDownMessage = commandCooldown.cooldownMessage();
        }

        for (Class<?> c : clazz.getClasses()) {
            if (c.isAnnotationPresent(ApplicationCommand.class)) {
                ApplicationCommand applicationCommand = c.getAnnotation(ApplicationCommand.class);
                String n = applicationCommand.name();
                String d = applicationCommand.description();

                subCommands.add(new Command<>(n, d, c, name, manager));
            }
        }
        for (Method m : clazz.getMethods()) {
            if (m.isAnnotationPresent(ApplicationCommandMethod.class)) {
                if (method == null) {
                    method = m;

                    for (Parameter p : m.getParameters()) {
                        if (p.isAnnotationPresent(Option.class)) {
                            options.add(new OptionData(commandManager.mapToOption(p.getType()), p.getName(), p.getAnnotation(Option.class).description(), p.getAnnotation(Option.class).isRequired()));
                        }
                    }
                }
            }
        }
    }

    public void performCommand(SlashCommandInteractionEvent event) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (method == null) return;

        if (event.getSubcommandName() != null) {
            for (Command<?> c : subCommands) {
                try {
                    c.performCommand(event);
                    return;
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Check cooldown
        if (cooldownLength != null) {
            if (CommandManager.getCooldown(fullName, event.getGuild().getIdLong(), event.getMember().getIdLong()).ifCooldown(i -> {
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setAuthor(event.getMember().getEffectiveName(), null, event.getMember().getEffectiveAvatarUrl())
                        .setColor(Color.RED)
                        .setDescription(coolDownMessage + "\n \n" + ":information_source: Versuche es in " + TimeFormat.RELATIVE.format(i.toEpochMilli()) + " erneut!");
                event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            }, cooldownLength)) return;
        }

        Object o = clazz.getDeclaredConstructor().newInstance();

        if (options.isEmpty()) {
            method.invoke(o, commandManager.bot, event);
        } else {
            Object[] params = new Object[options.size() + 2];
            params[0] = commandManager.bot;
            params[1] = event;

            for (int i = 2; i < (options.size() + 2); i++) {
                params[i] = commandManager.mapOption(event.getOption(options.get(i - 2).getName()));
            }
            method.invoke(o, params);
        }

        // set new cooldown
        CommandManager.getCooldown(fullName, event.getGuild().getIdLong(), event.getMember().getIdLong()).setCooldown();
    }

    public void upsertCommand(Guild guild) {
        CommandCreateAction action = guild.upsertCommand(clazz.getAnnotation(ApplicationCommand.class).name(),
                clazz.getAnnotation(ApplicationCommand.class).description()
        );
        action = action.addOptions(options);
        for (Command<?> subCommand : subCommands) {
            action = action.addSubcommands(new SubcommandData(subCommand.name, subCommand.getDescription())
                    .addOptions(subCommand.options)
            );
        }
        action.queue();
    }
}