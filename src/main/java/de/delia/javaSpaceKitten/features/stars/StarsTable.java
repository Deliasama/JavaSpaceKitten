package de.delia.javaSpaceKitten.features.stars;

import de.delia.javaSpaceKitten.database.Table;
import de.delia.javaSpaceKitten.main.Main;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class StarsTable extends Table<Stars> {
    public StarsTable() {
        super(Stars.class, Main.INSTANCE.entityManagerFactory);
    }

    public Stars get(long guildId, long memberId) {
        return getEntityManager(m -> {
            List<Stars> stars = m.createQuery("SELECT s from Stars s where s.guildId = ?1 and s.memberId = ?2", Stars.class)
                    .setParameter(1, guildId)
                    .setParameter(2, memberId)
                    .getResultList();
            if(stars.isEmpty())return save(new Stars(guildId, memberId, 0));
            return stars.get(0);
        });
    }

    public List<Stars> getSorted(long guildId) {
        return getEntityManager(m -> {
            return m.createQuery("SELECT s from Stars s where s.guildId = ?1 order by s.stars DESC ", Stars.class)
                    .setParameter(1, guildId)
                    .getResultList();
        });
    }

    public Stars update(Stars stars) {
        return getEntityManager(m -> {
            m.getTransaction().begin();
            Stars s = m.find(Stars.class, stars.getId());
            if(s == null)return null;
            s.setStars(stars.getStars());
            m.persist(s);
            m.getTransaction().commit();
            return s;
        });
    }
}
