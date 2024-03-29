package de.delia.javaSpaceKitten.features.comets.tables;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@Table(name = "comet")
public class Comet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long guildId;

    @Column
    private Long memberId;

    @Column
    private int stackSize;

    @Column
    private Instant boughtTime;

    @Column
    private Long boughtValue;

    public Comet(long guildId, long memberId, int stackSize, Instant boughtTime, long boughtValue) {
        this.guildId = guildId;
        this.memberId = memberId;
        this.stackSize = stackSize;
        this.boughtTime = boughtTime;
        this.boughtValue = boughtValue;
    }
}
