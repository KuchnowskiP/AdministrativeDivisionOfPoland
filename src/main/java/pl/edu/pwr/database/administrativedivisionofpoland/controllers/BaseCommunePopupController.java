package pl.edu.pwr.database.administrativedivisionofpoland.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IResultFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Gettable;

import java.util.Objects;

public abstract class BaseCommunePopupController extends BaseUnitPopupController {
    @FXML
    protected ChoiceBox countyChoiceBox;
    @FXML
    protected ChoiceBox voivodeshipChoiceBox;
    @FXML
    protected ChoiceBox communeTypeChoiceBox;
    PageResult<VoivodeshipDto> requestVoivodeships;
    PageResult<CountyDto> requestCounties;
    VoivodeshipDto selectedVoivodeship = new VoivodeshipDto(-1, "", "", "");
    CountyDto selectedCounty = new CountyDto(-1, -1, "", "", false, "", "");

    Gettable<CountyDto> countyGetter;

    public BaseCommunePopupController(IResultFetcher resultFetcher, IDataSender requestSender) {
        super(resultFetcher, requestSender);
    }

    protected void initializeVoivodeshipChoiceBoxListener() {
        voivodeshipChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    System.out.println(newValue);
                    if (!Objects.equals(String.valueOf(newValue), "-")) {
                        countyChoiceBox.getItems().clear();
                        countyChoiceBox.setDisable(false);
                        countyChoiceBox.getItems().add("-");
                        countyChoiceBox.setValue("-");
                        selectedVoivodeship = requestVoivodeships.getItems().stream()
                                .filter(voivodeshipDto -> newValue.equals(voivodeshipDto.getName())).findAny().get();
                        try {
                            requestCounties = countyGetter.get(selectedVoivodeship.getId(), 1, Integer.MAX_VALUE);
                            for (int i = 0; i < requestCounties.getItems().size(); i++) {
                                countyChoiceBox.getItems().add(requestCounties.getItems().get(i).getName());
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        selectedVoivodeship.setId(-1);
                        countyChoiceBox.setValue("-");
                        countyChoiceBox.setDisable(true);
                    }
                }
            }
        });
    }

    protected void initializeCountyChoiceBoxListener() {
        countyChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    if (!Objects.equals(String.valueOf(newValue), "-")) {
                        selectedCounty = requestCounties.getItems().stream()
                                .filter(voivodeshipDto -> newValue.equals(voivodeshipDto.getName())).findAny().get();
                    }

                    if (Objects.equals(String.valueOf(newValue), "-")) {
                        selectedCounty.setId(-1);
                    }
                }
            }
        });
    }

    protected void setChoiceBoxes() throws Exception {
        requestVoivodeships = resultFetcher.getVoivodeships(1, Integer.MAX_VALUE);
        voivodeshipChoiceBox.getItems().add("-");
        voivodeshipChoiceBox.setValue("-");
        for (int i = 0; i < requestVoivodeships.items.size(); i++) {
            voivodeshipChoiceBox.getItems().add(requestVoivodeships.getItems().get(i).getName());
        }
        communeTypeChoiceBox.getItems().addAll("gmina miejska", "gmina wiejska", "gmina miejsko-wiejska");
    }
}
