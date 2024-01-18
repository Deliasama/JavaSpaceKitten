package de.delia.javaSpaceKitten.features.stars;

import de.delia.javaSpaceKitten.main.Main;
import jakarta.persistence.*;

@Entity
@Table(name = "Stars")
public class Stars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "guild_id")
    private long guildId;
    @Column(name = "member_id")
    private long memberId;
    @Column(name = "stars")
    private int stars;

    public Stars() {

    }

    public Stars(long guildId, long memberId, int stars) {
        this.guildId = guildId;
        this.memberId = memberId;
        this.stars = stars;
    }

    public static StarsTable getTable() {
        return Main.INSTANCE.starsTable;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
    public long getGuildId() {
        return guildId;
    }

    public long getMemberId() {
        return memberId;
    }

    public int getStars() {
        return stars;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
