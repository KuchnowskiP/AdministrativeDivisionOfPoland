package pl.edu.pwr.database.administrativedivisionofpoland.Initializers;

import javafx.scene.control.TabPane;

public interface IListenerInitializer {
    void initialize();
    void setUpTabPaneListener(TabPane TabPane, int viewOrManage);
}
