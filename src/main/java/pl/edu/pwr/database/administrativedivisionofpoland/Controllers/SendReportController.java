package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.CommuneService;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.CountyService;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.VoivodeshipService;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;
import pl.edu.pwr.database.administrativedivisionofpoland.Utils.Utils;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SendReportController implements Initializable {
    @FXML private Button sendButton;
    @FXML private Label responseLabel;
    @FXML private TextField topicTextField;
    @FXML private ChoiceBox voivodeshipReportChoiceBox;
    @FXML private ChoiceBox countyReportChoiceBox;
    @FXML private ChoiceBox communeReportChoiceBox;
    @FXML private TextArea reportContentTextArea;
    VoivodeshipService voivodeshipDataService = new VoivodeshipService();
    CountyService countyService = new CountyService();
    CommuneService communeService = new CommuneService();
    VoivodeshipDto reportSelectedVoivodeship = new VoivodeshipDto(-1,"","","");
    CountyDto reportSelectedCounty = new CountyDto(-1,-1,"","",false,"","");
    CommuneDto reportSelectedCommune = new CommuneDto(-1,-1,"","",-1,-1.0,"","");
    PageResult<VoivodeshipDto> requestVoivodeships;
    PageResult<CountyDto> requestCounties;
    PageResult<CommuneDto> requestCommunes;
    private final DataSender requestSender = new DataSender();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setReportTab();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        choiceBoxListeners();
    }

    public void setReportTab() throws Exception {
        requestVoivodeships = Utils.getVoivodeshipResult(voivodeshipReportChoiceBox);
    }

    public void choiceBoxListeners() {
        voivodeshipReportChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue){
                if (newValue != null) {
                    System.out.println(newValue);
                    if (!Objects.equals(String.valueOf(newValue), "-")) {
                        countyReportChoiceBox.getItems().clear();
                        countyReportChoiceBox.setDisable(false);
                        countyReportChoiceBox.getItems().add("-");
                        countyReportChoiceBox.setValue("-");
                        reportSelectedVoivodeship = requestVoivodeships.getItems().stream()
                                .filter(voivodeshipDto -> newValue.equals(voivodeshipDto.getName())).findAny().get();
                        try {
                            requestCounties = countyService.getDto(reportSelectedVoivodeship.getId(), 1, Integer.MAX_VALUE);
                            for (int i = 0; i < requestCounties.getItems().size(); i++) {
                                countyReportChoiceBox.getItems().add(requestCounties.getItems().get(i).getName());
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        reportSelectedVoivodeship.setId(-1);
                        countyReportChoiceBox.setValue("-");
                        countyReportChoiceBox.setDisable(true);
                    }
                }
            }
        });
        countyReportChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    if (!Objects.equals(String.valueOf(newValue), "-")) {
                        communeReportChoiceBox.getItems().clear();
                        communeReportChoiceBox.setDisable(false);
                        communeReportChoiceBox.getItems().add("-");
                        communeReportChoiceBox.setValue("-");
                        reportSelectedCounty = requestCounties.getItems().stream()
                                .filter(countyDto -> newValue.equals(countyDto.getName())).findAny().get();

                        try {
                            requestCommunes = communeService.get(reportSelectedCounty.getId(), 1, Integer.MAX_VALUE);
                            for (int i = 0; i < requestCommunes.getItems().size(); i++) {
                                communeReportChoiceBox.getItems().add(requestCommunes.getItems().get(i).getName() + " (" + requestCommunes.getItems().get(i).getCommuneType() + ")");
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (Objects.equals(String.valueOf(newValue), "-")) {
                        reportSelectedCounty.setId(-1);
                        communeReportChoiceBox.getItems().clear();
                        communeReportChoiceBox.setDisable(true);
                    }
                }
            }
        });
        communeReportChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue != null) {
                    if (!Objects.equals(String.valueOf(newValue), "-")) {
                        String[] name = newValue.toString().split("\\(");
                        reportSelectedCommune = requestCommunes.getItems().stream()
                                .filter(communeDto -> name[0].trim().equals(communeDto.getName())).findAny().get();
                    } else {
                        reportSelectedCommune.setId(-1);
                    }
                }
            }
        });
    }
    public void onSendButtonClick(ActionEvent actionEvent) throws Exception {
        System.out.println("Click");
        if(topicTextField.getText().isBlank()){
            responseLabel.setText("Zgłoszenie musi zawierać temat!");
            responseLabel.setVisible(true);
            return;
        }else if(voivodeshipReportChoiceBox.getSelectionModel().selectedItemProperty().get().equals("-")){
            responseLabel.setText("Zgłoszenie musi dotyczyć którejś z jednostek administraycjnych!");
            responseLabel.setVisible(true);
            return;
        }else if(reportContentTextArea.getText().isBlank()){
            responseLabel.setText("Zgłoszenie musi mieć jakąś treść!");
            responseLabel.setVisible(true);
            return;
        }
        UserInput.prompt ="\nwysłać zgłoszenie?";
        UserInput.getConfirmation();
        if(UserInput.confirmed) {
            sendButton.setDisable(true);
            AddReportRequest addReportRequest = new AddReportRequest();
            addReportRequest.setTopic(topicTextField.getText().trim());
            addReportRequest.setContent(reportContentTextArea.getText().trim());
            if (reportSelectedVoivodeship.getId() != -1) {
                addReportRequest.setVoivodeshipId(reportSelectedVoivodeship.getId());
            }
            if (reportSelectedCounty.getId() != -1) {
                addReportRequest.setCountyId(reportSelectedCounty.getId());
            }
            if (reportSelectedCommune.getId() != -1) {
                addReportRequest.setCommuneId(reportSelectedCommune.getId());
            }
            requestSender.addReport(addReportRequest);

            responseLabel.setText("Wysłano!");
            responseLabel.setVisible(true);

            System.out.println("Wysłano");


            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                int seconds = 3;
                @Override
                public void handle(ActionEvent actionEvent) {
                    responseLabel.setText("Wysłano! Zamykanie... " + seconds);
                    seconds--;
                    if(seconds < 0){
                        Window window = responseLabel.getScene().getWindow();
                        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
                    }
                }
            }));
            timeline.setCycleCount(4);
            timeline.play();

        }
    }
}
