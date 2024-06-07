package pl.edu.pwr.database.administrativedivisionofpoland.handlers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.edu.pwr.database.administrativedivisionofpoland.Main;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;
import pl.edu.pwr.database.administrativedivisionofpoland.controllers.*;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IResultFetcher;

import java.io.IOException;
import java.util.Objects;

public class NavigationHandler {
    MainController mainController;
    IResultFetcher resultFetcher;
    IDataSender dataSender;

    public NavigationHandler(MainController mainController, IResultFetcher resultFetcher, IDataSender dataSender) {
        this.mainController = mainController;
        this.resultFetcher = resultFetcher;
        this.dataSender = dataSender;
    }

    public void setupButtons(){
        mainController.voivodeshipTabAddUnitButton.setOnAction(actionEvent -> {
            try {
                openAddVoivodeshipPopup(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mainController.countyTabAddUnitButton.setOnAction(actionEvent -> {
            try {
                openAddCountyPopup(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mainController.communeTabAddUnitButton.setOnAction(actionEvent -> {
            try {
                openAddCommunePopup(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mainController.voivodeshipTabEditUnitButton.setOnAction(actionEvent -> {
            try {
                openEditVoivodeshipPopup(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mainController.countyTabEditUnitButton.setOnAction(actionEvent -> {
            try {
                openEditCountyPopup(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mainController.communeTabEditUnitButton.setOnAction(actionEvent -> {
            try {
                openEditCommunePopup(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void openAddVoivodeshipPopup(ActionEvent ignoredActionEvent) throws IOException {
        System.out.println("Adding voivodeship");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    AddVoivodeshipPopupController.class
                            .getResource("add-voivodeship-popup.fxml")
            );
            fxmlLoader.setControllerFactory(c -> new AddVoivodeshipPopupController(resultFetcher, dataSender));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(
                    Objects.requireNonNull(
                            AddVoivodeshipPopupController.class
                                    .getResource(("pop-up-style.css"))
                    ).toExternalForm()
            );
            stage.setScene(scene);
            stage.setTitle("Podaj dane nowego województwa");
            Image icon = new Image(
                    Objects.requireNonNull(
                            AddVoivodeshipPopupController.class
                                    .getResourceAsStream("icon.png")
                    )
            );
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            mainController.changeView(1);
        } catch(Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void openAddCountyPopup(ActionEvent ignoredActionEvent) throws IOException {
        System.out.println("Adding county");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    AddCountyPopupController.class
                            .getResource("add-county-popup.fxml")
            );
            fxmlLoader.setControllerFactory(c -> new AddCountyPopupController(resultFetcher, dataSender));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(
                    Objects.requireNonNull(
                            AddCountyPopupController.class
                                    .getResource(("pop-up-style.css"))
                    ).toExternalForm()
            );
            stage.setScene(scene);
            stage.setTitle("Podaj dane nowego powiatu");
            Image icon = new Image(
                    Objects.requireNonNull(
                            AddCountyPopupController.class
                                    .getResourceAsStream("icon.png")
                    )
            );
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch(Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void openAddCommunePopup(ActionEvent ignoredActionEvent) throws IOException {
        System.out.println("Adding commune");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    AddCommunePopupController.class
                            .getResource("add-commune-popup.fxml")
            );
            fxmlLoader.setControllerFactory(c -> new AddCommunePopupController(resultFetcher, dataSender));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(
                    Objects.requireNonNull(
                            AddCommunePopupController.class
                                    .getResource(("pop-up-style.css"))
                    ).toExternalForm()
            );
            stage.setScene(scene);
            stage.setTitle("Podaj dane nowej gminy");
            Image icon = new Image(
                    Objects.requireNonNull(
                            AddCommunePopupController.class
                                    .getResourceAsStream("icon.png")
                    )
            );
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch(Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void openEditVoivodeshipPopup(ActionEvent ignoredActionEvent) throws IOException {
        if(mainController.voivodeshipForEditionOrDeletion.getId() == -1){
            return;
        }
        System.out.println("Editing voivodeship");
        try {
            UserInput.unit = mainController.voivodeshipForEditionOrDeletion;
            FXMLLoader fxmlLoader = new FXMLLoader(
                    EditVoivodeshipPopupController.class
                            .getResource("edit-voivodeship-popup.fxml")
            );
            fxmlLoader.setControllerFactory(c -> new EditVoivodeshipPopupController(resultFetcher, dataSender));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(
                    Objects.requireNonNull(
                            EditVoivodeshipPopupController.class
                                    .getResource(("pop-up-style.css"))
                    ).toExternalForm()
            );
            stage.setScene(scene);
            stage.setTitle("Podaj dane województwa");
            Image icon = new Image(
                    Objects.requireNonNull(
                            EditVoivodeshipPopupController.class
                                    .getResourceAsStream("icon.png")
                    )
            );
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            mainController.changeView(1);
        } catch(Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void openEditCountyPopup(ActionEvent ignoredActionEvent) throws IOException {
        if(mainController.countyForEditionOrDeletion.getId() == -1){
            return;
        }
        System.out.println("Editing county");
        try {
            UserInput.unit = resultFetcher.getCountyById(mainController.countyForEditionOrDeletion.getId());
            FXMLLoader fxmlLoader = new FXMLLoader(
                    EditCountyPopupController.class
                            .getResource("edit-county-popup.fxml")
            );
            fxmlLoader.setControllerFactory(c -> new EditCountyPopupController(resultFetcher, dataSender));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(
                    Objects.requireNonNull(
                            EditCountyPopupController.class
                                    .getResource(("pop-up-style.css"))
                    ).toExternalForm()
            );
            stage.setScene(scene);
            stage.setTitle("Podaj dane powiatu");
            Image icon = new Image(
                    Objects.requireNonNull(
                            EditCountyPopupController.class
                                    .getResourceAsStream("icon.png")
                    )
            );
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            mainController.changeView(1);
        } catch(Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void openEditCommunePopup(ActionEvent ignoredActionEvent) throws IOException {
        if(mainController.communeForEditionOrDeletion.getId() == -1){
            return;
        }
        System.out.println("Editing commune");
        try {
            UserInput.unit = resultFetcher.getCommuneById(mainController.communeForEditionOrDeletion.getId());
            FXMLLoader fxmlLoader = new FXMLLoader(
                    EditCommunePopupController.class
                            .getResource("edit-commune-popup.fxml")
            );
            fxmlLoader.setControllerFactory(c -> new EditCommunePopupController(resultFetcher, dataSender));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(
                    Objects.requireNonNull(
                            EditCommunePopupController.class
                                    .getResource(("pop-up-style.css"))
                    ).toExternalForm()
            );
            stage.setScene(scene);
            stage.setTitle("Podaj dane gminy");
            Image icon = new Image(
                    Objects.requireNonNull(
                            EditCommunePopupController.class
                                    .getResourceAsStream("icon.png")
                    )
            );
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            mainController.changeView(1);
        } catch(Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void goBackInTabDepth(ActionEvent ignoredActionEvent) {
        if(mainController.tabPaneDepthLevels[0] > 0) {
            mainController.tabPaneDepthLevels[0]--;
            mainController.showOrHideCountyCommuneChoiceBoxInViewTab();
        }
        System.out.println("Wrócono do: " +
                mainController.administrativeUnitHierarchyChain[mainController.tabPaneDepthLevels[0]]);
        mainController.changeView(0);
    }

    public void onLoginButtonClick(ActionEvent ignoredActionEvent) {
        System.out.println("przycisk wciśnięty");
        String login = mainController.loginTextField.getText();
        String password = mainController.passwordTextField.getText();

        Runnable authenticate = () -> {
            if (mainController.authenticationService.authenticate(login, password)) {
                mainController.listenerInitializer
                        .setUpTabPaneListener(mainController.manageUnitsTabPane, 1);
                mainController.voivodeshipsTableManage.setEditable(true);
                mainController.countiesTableManage.setEditable(true);
                mainController.communesTable.setEditable(true);

                Platform.runLater(() -> {
                    mainController.loginFeedbackLabel
                            .setText("Zalogowano jako " + mainController.loginTextField.getText());
                    mainController.loginFeedbackLabel.setVisible(true);
                    mainController.mainTabPane.getTabs().add(mainController.manageTab);
                });

                mainController.changeTableListener(-1, 0, 1);
                mainController.changeView(1);
            }else{
                Platform.runLater(() -> {
                    mainController.loginFeedbackLabel.setText("Błędne dane logowania!");
                    mainController.loginFeedbackLabel.setVisible(true);
                });

            }
        };
        Thread authenticator = new Thread(authenticate);
        authenticator.start();

    }

    public void goBackInManageTabDepth(ActionEvent ignoredActionEvent) {
        if(mainController.tabPaneDepthLevels[1] > 0) {
            mainController.tabPaneDepthLevels[1]--;
            mainController.showOrHideCountyCommuneChoiceBoxInManageTab();
            mainController.uiInteractionHandler.setAddButton();
            mainController.uiInteractionHandler.setEditButton();
        }

        System.out.println("Wrócono do: " +
                mainController.administrativeUnitHierarchyChain[mainController.tabPaneDepthLevels[1]]);
        mainController.changeView(1);
    }

    public void refreshView(ActionEvent ignoredActionEvent) {
        mainController.changeView(0);
    }

    public void refreshManageView(ActionEvent ignoredActionEvent) {
        mainController.changeView(1);
    }

    public void displayUnitsWithAddresses(ActionEvent ignoredActionEvent) {
        if (mainController.registeredOfficesCheckBox.isSelected()) {
            mainController.addressesAreChecked = 4;
        } else {
            mainController.addressesAreChecked = 0;
        }
        mainController.changeView(0);
    }

    public void openHistoricalDataWindow(ActionEvent ignoredActionEvent) {
        mainController.showHistoricalDataButton.setDisable(true);
        System.out.println("Showing history");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    HistoryController.class
                            .getResource("history-view.fxml")
            );
            fxmlLoader.setControllerFactory(c -> new HistoryController(resultFetcher));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().addAll(
                    Objects.requireNonNull(
                            Main.class
                                    .getResource("style.css")
                    ).toExternalForm()
            );
            stage.setScene(scene);
            stage.setTitle("Dane historyczne");
            Image icon = new Image(
                    Objects.requireNonNull(
                            HistoryController.class
                                    .getResourceAsStream("icon.png")
                    )
            );
            stage.getIcons().add(icon);
            stage.setOnCloseRequest(windowEvent -> mainController.showHistoricalDataButton.setDisable(false));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void openSendReportProblemWindow(ActionEvent ignoredActionEvent) {
        mainController.reportProblemButton.setDisable(true);
        System.out.println("Report problem");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    SendReportController.class
                            .getResource("send-report-popup.fxml")
            );
            fxmlLoader.setControllerFactory(c -> new SendReportController(resultFetcher, dataSender));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().addAll(
                    Objects.requireNonNull(
                            Main.class
                                    .getResource("style.css")
                    ).toExternalForm()
            );
            stage.setScene(scene);
            stage.setTitle("Zgłoś problem");
            Image icon = new Image(
                    Objects.requireNonNull(
                            SendReportController.class
                                    .getResourceAsStream("icon.png")
                    )
            );
            stage.getIcons().add(icon);
            stage.setOnCloseRequest(windowEvent -> mainController.reportProblemButton.setDisable(false));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}