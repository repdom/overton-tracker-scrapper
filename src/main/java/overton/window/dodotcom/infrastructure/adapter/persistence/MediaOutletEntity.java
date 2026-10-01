package overton.window.dodotcom.infrastructure.adapter.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "media_outlets")
public class MediaOutletEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String name;

    @Column(unique = true, nullable = false)
    public String domain;

    @Column(name = "source_type")
    public String sourceType;

    @Column(name = "base_ideology_score", precision = 3, scale = 2)
    public BigDecimal baseIdeologyScore;

    @Column(name = "country_code", length = 2)
    public String countryCode = "US";
}
