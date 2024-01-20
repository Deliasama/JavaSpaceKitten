package de.delia.javaSpaceKitten.features.stars.tables;

import de.delia.javaSpaceKitten.database.Table;
import de.delia.javaSpaceKitten.main.Main;

import java.util.List;

public class ProfileTable extends Table<Profile> {
    public ProfileTable() {
        super(Profile.class, Main.INSTANCE.entityManagerFactory);
    }

    public Profile get(long guildId, long memberId) {
        return getEntityManager(m -> {
            List<Profile> profiles = m.createQuery("SELECT p from Profile p where p.guildId = ?1 and p.memberId = ?2", Profile.class)
                    .setParameter(1, guildId)
                    .setParameter(2, memberId)
                    .getResultList();
            if(profiles.isEmpty())return save(new Profile(guildId, memberId, 0, 0, 0, 0));
            return profiles.get(0);
        });
    }

    public List<Profile> getSortedByStars(long guildId) {
        return getEntityManager(m -> {
            return m.createQuery("SELECT p from Profile p where p.guildId = ?1 order by p.stars DESC ", Profile.class)
                    .setParameter(1, guildId)
                    .getResultList();
        });
    }

    public Profile update(Profile profile) {
        return getEntityManager(m -> {
            m.getTransaction().begin();
            Profile p = m.find(Profile.class,  profile.getId());
            if(p == null)return null;
            p.setStars(profile.getStars());
            p.setDailyStreak(p.getDailyStreak());
            p.setWorked(p.getWorked());
            p.setVotes(profile.getVotes());
            m.persist(p);
            m.getTransaction().commit();
            return p;
        });
    }
}
