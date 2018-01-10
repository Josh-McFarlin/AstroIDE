package main.java.me.joshmcfarlin.AstroIDE.GUI.Components;

import main.java.me.joshmcfarlin.AstroIDE.Utils.Permissions;
import javafx.scene.control.Tab;

public abstract class CustomTab extends Tab {
    public boolean Enabled(Permissions p) {
        return false;
    }
}