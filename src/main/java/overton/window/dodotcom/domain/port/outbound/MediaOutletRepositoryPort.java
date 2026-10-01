package overton.window.dodotcom.domain.port.outbound;

import overton.window.dodotcom.domain.model.MediaOutlet;

import java.util.List;
import java.util.Optional;

public interface MediaOutletRepositoryPort {
    Optional<MediaOutlet> findById(Long id);
    List<MediaOutlet> findAll();
}
