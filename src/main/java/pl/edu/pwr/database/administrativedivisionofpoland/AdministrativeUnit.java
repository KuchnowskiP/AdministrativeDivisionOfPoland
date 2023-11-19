package pl.edu.pwr.database.administrativedivisionofpoland;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AdministrativeUnit {
    public SimpleStringProperty name;
    public SimpleIntegerProperty population;

    public AdministrativeUnit(String name, int population) {
        this.name = new SimpleStringProperty(name);
        this.population = new SimpleIntegerProperty(population);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getPopulation() {
        return population.get();
    }

    public SimpleIntegerProperty populationProperty() {
        return population;
    }

    public void setPopulation(int population) {
        this.population.set(population);
    }
}
