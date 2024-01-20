package de.delia.javaSpaceKitten.commands;

import de.delia.javaSpaceKitten.database.Table;
import de.delia.javaSpaceKitten.main.Main;

import java.time.Instant;
import java.util.List;

public class CooldownTable extends Table<Cooldown> {
    public CooldownTable() {
        super(Cooldown.class, Main.INSTANCE.entityManagerFactory);
    }

    public Cooldown get(String commandName, long guildId, long memberId) {
        return getEntityManager(m -> {
            List<Cooldown> cooldowns = m.createQuery("SELECT c from Cooldown c where c.commandName = ?1 and c.guildId = ?2 and c.memberId = ?3", Cooldown.class)
                    .setParameter(1, commandName)
                    .setParameter(2, guildId)
                    .setParameter(3, memberId)
                    .getResultList();
            if(cooldowns.isEmpty())return save(new Cooldown(commandName, guildId, memberId, Instant.EPOCH));
            return cooldowns.get(0);
        });
    }

    public Cooldown update(Cooldown cooldown) {
        return getEntityManager(m -> {
            m.getTransaction().begin();
            Cooldown c = m.find(Cooldown.class,  cooldown.getId());
            if(c == null)return null;
            c.setLastCall(cooldown.getLastCall());
            m.persist(c);
            m.getTransaction().commit();
            return c;
        });
    }
}
