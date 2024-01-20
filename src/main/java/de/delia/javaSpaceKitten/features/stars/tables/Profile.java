package de.delia.javaSpaceKitten.features.stars.tables;

import de.delia.javaSpaceKitten.main.Main;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long guildId;

    @Column
    private Long memberId;

    @Column
    private int stars;

    @Column
    private int dailyStreak;

    @Column
    private int votes;

    @Column
    private int worked;

    public Profile(long guildId, long memberId, int stars, int dailyStreak, int votes, int worked) {
        this.guildId = guildId;
        this.memberId = memberId;
        this.stars = stars;
        this.dailyStreak = dailyStreak;
        this.votes = votes;
        this.worked = worked;
    }

    public static ProfileTable getTable() {
        return Main.INSTANCE.profileTable;
    }

    // TODO other setters
}
