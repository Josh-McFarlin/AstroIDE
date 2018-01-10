package main.java.me.joshmcfarlin.AstroIDE.Utils;

import java.io.File;

public class FileUtils {
    public static String appName = "Astro";
    private static String homeDir = System.getProperty("user.home");

    public static File mainDirectory() {
        File dir;
        if (System.getProperty("os.name").startsWith("Windows")) {
            dir = new File(System.getProperty("APPDATA") + "/" + appName);
        } else if (System.getProperty("os.name").startsWith("Mac OS X")) {
            dir = new File(homeDir + "/Library/Application Support/" + appName);
        } else {
            dir = new File(homeDir + "/." + appName);
        }
        return dir;
    }

    public static File settingsDirectory() {
        return new File(mainDirectory().getAbsolutePath() + "/Settings");
    }

    public static File recentFilesStore() {
        return new File(settingsDirectory().getAbsolutePath() + "/recent.txt");
    }

    public static File pluginsDirectory() {
        return new File(mainDirectory().getAbsolutePath() + "/Plugins");
    }

    public static File styleDirectory() {
        return new File(mainDirectory().getAbsolutePath() + "/Style");
    }

    public static void setupConfig() {
        File mainDir = mainDirectory();
        if (!mainDir.exists()) {
            mainDir.mkdir();
            System.out.println("Created app directory.");
        } else {
            System.out.println("App directory already exists at: " + mainDir);
        }

        File settingsDir = settingsDirectory();
        if (!settingsDir.exists()) {
            settingsDir.mkdir();
            System.out.println("Created settings directory.");
        } else {
            System.out.println("Settings directory already exists at: " + settingsDir);
        }

        File pluginsDir = pluginsDirectory();
        if (!pluginsDir.exists()) {
            pluginsDir.mkdir();
            System.out.println("Created plugins directory.");
        } else {
            System.out.println("Plugins directory already exists at: " + pluginsDir);
        }

        File styleDir = styleDirectory();
        if (!styleDir.exists()) {
            styleDir.mkdir();
            System.out.println("Created style directory.");
        } else {
            System.out.println("Settings directory already exists at: " + styleDir);
        }
    }
}