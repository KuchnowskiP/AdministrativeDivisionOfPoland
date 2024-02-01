package pl.edu.pwr.database.administrativedivisionofpoland.Utils;

import javafx.scene.control.ChoiceBox;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.VoivodeshipDataService;

import java.io.IOException;

public class Utils {
    static VoivodeshipDataService voivodeshipDataService = new VoivodeshipDataService();
    public static PageResult<VoivodeshipDto> getVoivodeshipResult(ChoiceBox voivodeshipChoiceBox) throws IOException, InterruptedException {
        PageResult<VoivodeshipDto> requestVoivodeships = voivodeshipDataService.get(null,1, Integer.MAX_VALUE);
        voivodeshipChoiceBox.getItems().add("-");
        voivodeshipChoiceBox.setValue("-");
        for(int i = 0; i < requestVoivodeships.items.size(); i++){
            voivodeshipChoiceBox.getItems().add(requestVoivodeships.getItems().get(i).getName());
        }
        return requestVoivodeships;
    }
}
