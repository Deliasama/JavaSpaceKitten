package de.delia.javaSpaceKitten.features.comets.tables;

import de.delia.javaSpaceKitten.database.Table;
import de.delia.javaSpaceKitten.main.Main;

import java.util.List;

public class CometPriceTable extends Table<CometPrice> {
    public CometPriceTable() {
        super(CometPrice.class, Main.INSTANCE.entityManagerFactory);
    }

    public CometPrice get(long guildId) {
        return getEntityManager(m -> {
            List<CometPrice> cometPrices = m.createQuery("SELECT c from CometPrice c where c.guildId = ?1", CometPrice.class)
                    .setParameter(1, guildId)
                    .getResultList();
            if (cometPrices.isEmpty()) return save(new CometPrice(guildId, 100L, 0));
            return cometPrices.get(0);
        });
    }

    public CometPrice update(CometPrice cometPrice) {
        return getEntityManager(m -> {
            m.getTransaction().begin();
            CometPrice c = m.find(CometPrice.class, cometPrice.getId());
            if (c == null) return null;
            c.setPrice(cometPrice.getPrice());
            c.setTraded(cometPrice.getTraded());
            m.persist(c);
            m.getTransaction().commit();
            return c;
        });
    }
}
