package pl.edu.pwr.database.administrativedivisionofpoland.initializers.api;

import javafx.scene.control.TabPane;

public interface IListenerInitializer {
    void initialize();
    void setUpTabPaneListener(TabPane TabPane, int viewOrManage);
}
