package de.delia.javaSpaceKitten.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApplicationCommandCooldown {
    long time();

    TimeUnit timeUnit();

    String cooldownMessage() default "Du musst noch warten bevor du den Command erneut auslösen kannst!";
}
