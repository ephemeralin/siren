package com.ephemeralin.siren;

import com.ephemeralin.siren.util.PropertiesStorage;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * The type Checker.
 */
@lombok.extern.log4j.Log4j2
public class Checker {
    private PropertiesStorage props;

    /**
     * Instantiates a new Checker.
     *
     * @param props the props
     */
    public Checker(PropertiesStorage props) {
        this.props = props;
    }

    /**
     * Check ns category b.
     */
    public void checkNSCategoryB() {
        boolean achtung = false;
        String url = props.getProperty("web.address");
        try {
            String html = Jsoup.connect(url).get().outerHtml();
            if (!html.contains("Category B is now closed")) {
                achtung = true;
                log.warn("!!! Category B is probably OPENED !!!");
            } else {
                log.info("Check complete: nothing to do");
            }
            if (achtung) {
                String subject = "Category B is probably OPENED!";
                String body = "Please, follow the link immediately and checkNSCategoryB: " + url;
                new Mailer().sendEmail(subject, body, props);
            }
        } catch (IOException e) {
            log.error("Cannot get the page");
        }
    }
}
