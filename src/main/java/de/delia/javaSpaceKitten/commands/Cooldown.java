package de.delia.javaSpaceKitten.commands;

import de.delia.javaSpaceKitten.main.Main;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

@Data
@Entity
@Table(name = "Cooldown")
public class Cooldown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String commandName;

    @Column
    private Long guildId;

    @Column
    private Long memberId;

    @Column
    private Instant lastCall;

    public Cooldown() {

    }

    public Cooldown(String commandName, Long guildId, long memberId, Instant lastcall) {
        this.commandName = commandName;
        this.guildId = guildId;
        this.memberId = memberId;
        this.lastCall = lastcall;
    }

    public static CooldownTable getCooldownTable() {
        return Main.INSTANCE.cooldownTable;
    }

    public boolean ifCooldown(Consumer<Instant> consumer, Duration duration) {
        Instant nextCallMin = lastCall.plus(duration);
        if (nextCallMin.isBefore(Instant.now())) {
            return false;
        } else {
            consumer.accept(nextCallMin);
            return true;
        }
    }

    public Cooldown setCooldown() {
        lastCall = Instant.now();
        getCooldownTable().update(this);
        return this;
    }

    public Instant availableIn(Duration duration) {
        return lastCall.plus(duration);
    }
}
