package overton.window.dodotcom.domain.model;

public record NamedEntity(
        Long id,
        String name,
        String normalizedName,
        EntityType entityType
) {
    public enum EntityType { PERSON, ORGANIZATION, LOCATION, MISC }
}
