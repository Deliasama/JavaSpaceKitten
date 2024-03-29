package de.delia.javaSpaceKitten.features.comets.tables;

import de.delia.javaSpaceKitten.database.Table;
import de.delia.javaSpaceKitten.main.Main;

import java.util.List;

public class CometTable extends Table<Comet> {
    public CometTable() {
        super(Comet.class, Main.INSTANCE.entityManagerFactory);
    }

    public List<Comet> get(long guildId, long memberId) {
        return getEntityManager(m -> {
            return m.createQuery("SELECT c from Comet c where c.guildId = ?1 and c.memberId = ?2 order by c.boughtTime ASC ", Comet.class)
                    .setParameter(1, guildId)
                    .setParameter(2, memberId)
                    .getResultList();
        });
    }

    public void delete(Comet comet) {
        getById(comet.getId()).ifPresent(this::remove);
    }
}
