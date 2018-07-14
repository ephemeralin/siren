package com.ephemeralin.siren;

import com.ephemeralin.siren.util.PropertiesStorage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The type Application.
 */
public class Application extends TimerTask {
    /**
     * Properties storage instance.
     */
    private PropertiesStorage propertiesStorage;
    /**

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        PropertiesStorage propertiesStorage = PropertiesStorage.getInstance("/siren.properties");
        new Mailer().sendEmail("NS Checker started",
                "NS Checker started successfully", propertiesStorage);
        Application app = new Application(propertiesStorage);
        Timer timer = new Timer();
        timer.schedule(app, 100, Long.parseLong(propertiesStorage.getProperty("app.checkingPeriod")));
    }

    /**
     * Constructor of the app with properties parameter.
     * @param propertiesStorage properties parameter
     */
    public Application(PropertiesStorage propertiesStorage) {
        this.propertiesStorage = propertiesStorage;
    }

    /**
     * Start checker.
     */
    private void startChecker() {
        new Checker(propertiesStorage).checkNSCategoryB();
    }

    @Override
    public void run() {
        startChecker();
    }
}
