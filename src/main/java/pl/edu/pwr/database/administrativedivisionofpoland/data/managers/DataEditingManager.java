package pl.edu.pwr.database.administrativedivisionofpoland.data.managers;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Editable;

public class DataEditingManager implements IDataEditingManager {
    private final Editable<VoivodeshipRequest> voivodeshipEditor;
    private final Editable<CountyRequest> countyEditor;
    private final Editable<CommuneRequest> communeEditor;

    public DataEditingManager(Editable<VoivodeshipRequest> voivodeshipEditor, Editable<CountyRequest> countyEditor,
                              Editable<CommuneRequest> communeEditor) {
        this.voivodeshipEditor = voivodeshipEditor;
        this.countyEditor = countyEditor;
        this.communeEditor = communeEditor;
    }

    @Override
    public boolean editVoivodeship(int unitId, VoivodeshipRequest voivodeshipRequest) throws Exception {
        return voivodeshipEditor.edit(unitId, voivodeshipRequest);
    }

    @Override
    public boolean editCounty(int unitId, CountyRequest countyRequest) throws Exception {
        return countyEditor.edit(unitId, countyRequest);
    }

    @Override
    public boolean editCommune(int unitId, CommuneRequest communeRequest) throws Exception {
        return communeEditor.edit(unitId, communeRequest);
    }
}
