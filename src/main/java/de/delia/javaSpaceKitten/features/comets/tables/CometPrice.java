package de.delia.javaSpaceKitten.features.comets.tables;

import de.delia.javaSpaceKitten.main.Main;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "cometPrice")
public class CometPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long guildId;

    @Column
    private Long price;

    @Column
    private Long traded;

    public CometPrice(Long guildId, long price, long traded) {
        this.guildId = guildId;
        this.price = price;
        this.traded = traded;
    }

    public static CometPriceTable getTable() {
        return Main.INSTANCE.cometPriceTable;
    }
}
