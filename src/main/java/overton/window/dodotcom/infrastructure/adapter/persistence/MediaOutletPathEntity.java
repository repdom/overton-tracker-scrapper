package overton.window.dodotcom.infrastructure.adapter.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "media_outlet_paths")
public class MediaOutletPathEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "media_id", nullable = false)
    public Long mediaId;

    @Column(name = "path_prefix", nullable = false)
    public String pathPrefix;

    @Column(name = "is_active")
    public boolean isActive = true;

    public static java.util.List<MediaOutletPathEntity> findActiveByMediaId(Long mediaId) {
        return list("mediaId = ?1 and isActive = true", mediaId);
    }
}
