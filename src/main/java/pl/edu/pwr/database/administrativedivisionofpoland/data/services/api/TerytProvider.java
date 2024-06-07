package pl.edu.pwr.database.administrativedivisionofpoland.data.services.api;

import java.io.IOException;

/**
 * Interface for providing new Teryt code.
 * Teryt is commonly used name for unique unit code assigned to every administrative unit in Poland
 * in National Official Register of Territorial Division of the Country (TERYT)
 * provided by Polish Central Statistical Office.
 *
 * @see <a href="https://eteryt.stat.gov.pl/eTeryt/english.aspx?contrast=default">Teryt</a>
 */
public interface TerytProvider {
    String getNewTeryt(String[] args) throws IOException, InterruptedException;
}
