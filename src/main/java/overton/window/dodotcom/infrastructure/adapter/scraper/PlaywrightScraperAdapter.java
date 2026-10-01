package overton.window.dodotcom.infrastructure.adapter.scraper;

import com.microsoft.playwright.*;
import jakarta.enterprise.context.ApplicationScoped;
import overton.window.dodotcom.domain.model.RawDocument;
import overton.window.dodotcom.domain.port.outbound.ScraperPort;

/**
 * Uses Playwright for JavaScript-heavy pages (SPAs, paywalled sites).
 * Priority: lower than JsoupScraperAdapter — only used when explicitly needed.
 */
@ApplicationScoped
public class PlaywrightScraperAdapter implements ScraperPort {

    @Override
    public boolean supports(String url, String mimeType) {
        // Use Playwright for known JS-heavy domains; fallback logic can be expanded
        return url.contains("foxnews.com") || url.contains("cnn.com")
                || url.contains("huffpost.com") || url.contains("dailykos.com");
    }

    @Override
    public RawDocument fetch(String url) {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
             Page page = browser.newPage()) {

            page.navigate(url, new Page.NavigateOptions().setTimeout(30_000));
            page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);

            String html = page.content();
            return new RawDocument(url, page.url(), html.getBytes(), "text/html", html);
        }
    }
}
