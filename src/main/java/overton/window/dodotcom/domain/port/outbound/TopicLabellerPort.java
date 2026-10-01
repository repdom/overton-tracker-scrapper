package overton.window.dodotcom.domain.port.outbound;

/**
 * Outbound port: assigns a preliminary topic label based on source metadata.
 * Rule-based in this service; ML-based labeling is done by the external AI service.
 */
public interface TopicLabellerPort {
    String assignTopic(String title, String cleanText, String ideologyHint);
}
