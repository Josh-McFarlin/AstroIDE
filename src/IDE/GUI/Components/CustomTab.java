package IDE.GUI.Components;

import IDE.Utils.Permissions;
import javafx.scene.control.Tab;

public abstract class CustomTab extends Tab {
    public boolean Enabled(Permissions p) {
        return false;
    }
}