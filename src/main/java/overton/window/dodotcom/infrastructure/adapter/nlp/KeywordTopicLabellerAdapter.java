package overton.window.dodotcom.infrastructure.adapter.nlp;

import jakarta.enterprise.context.ApplicationScoped;
import overton.window.dodotcom.domain.port.outbound.TopicLabellerPort;

import java.util.Map;

/**
 * Rule-based preliminary topic labeller using keyword matching.
 * ML-based classification is delegated to the external AI service.
 */
@ApplicationScoped
public class KeywordTopicLabellerAdapter implements TopicLabellerPort {

    private static final Map<String, String[]> TOPIC_KEYWORDS = Map.of(
            "healthcare",       new String[]{"health", "medicare", "medicaid", "insurance", "hospital", "drug"},
            "economy",          new String[]{"economy", "gdp", "inflation", "trade", "tariff", "wage", "labor"},
            "foreign_policy",   new String[]{"nato", "china", "russia", "ukraine", "iran", "diplomacy", "sanctions"},
            "defense",          new String[]{"military", "pentagon", "defense", "army", "navy", "nuclear", "weapon"},
            "immigration",      new String[]{"immigration", "border", "asylum", "migrant", "deportation"},
            "climate",          new String[]{"climate", "carbon", "emission", "renewable", "fossil", "epa"},
            "technology",       new String[]{"ai", "artificial intelligence", "tech", "cyber", "data", "algorithm"},
            "social_policy",    new String[]{"abortion", "gun", "lgbtq", "race", "equity", "diversity"}
    );

    @Override
    public String assignTopic(String title, String cleanText, String ideologyHint) {
        String combined = (title + " " + cleanText).toLowerCase();
        return TOPIC_KEYWORDS.entrySet().stream()
                .filter(e -> matchesAny(combined, e.getValue()))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse("general");
    }

    private boolean matchesAny(String text, String[] keywords) {
        for (String kw : keywords) {
            if (text.contains(kw)) return true;
        }
        return false;
    }
}
