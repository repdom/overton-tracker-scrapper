package overton.window.dodotcom.infrastructure.adapter.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import overton.window.dodotcom.domain.model.MediaOutlet;
import overton.window.dodotcom.domain.port.outbound.MediaOutletRepositoryPort;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MediaOutletRepositoryAdapter implements MediaOutletRepositoryPort {

    @Override
    public Optional<MediaOutlet> findById(Long id) {
        return MediaOutletEntity.<MediaOutletEntity>findByIdOptional(id).map(this::toDomain);
    }

    @Override
    public List<MediaOutlet> findAll() {
        return MediaOutletEntity.<MediaOutletEntity>listAll().stream().map(this::toDomain).toList();
    }

    private MediaOutlet toDomain(MediaOutletEntity e) {
        return new MediaOutlet(e.id, e.name, e.domain,
                MediaOutlet.SourceType.valueOf(e.sourceType),
                e.baseIdeologyScore, e.countryCode);
    }
}
