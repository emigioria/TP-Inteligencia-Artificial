package frsf.cidisi.exercise.patrullero.search;

import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import frsf.cidisi.faia.exceptions.PrologConnectorException;
import frsf.cidisi.faia.simulator.SearchBasedAgentSimulator;

public class PatrulleroMain {

	public static void main(String[] args) throws PrologConnectorException {
		//TODO crear mapa y posicion agente e incidente
		Mapa mapaAmbiente = null;
		Interseccion posicionAgenteAmbiente = null;

		Mapa mapaPatrullero = null;
		Interseccion posicionAgentePatrullero = null;
		Interseccion posicionIncidente = null;

		Patrullero agent = new Patrullero(mapaPatrullero, posicionAgentePatrullero, posicionIncidente);

		AmbienteCiudad environment = new AmbienteCiudad(mapaAmbiente, posicionAgenteAmbiente);

		SearchBasedAgentSimulator simulator =
				new SearchBasedAgentSimulator(environment, agent);

		simulator.start();
	}

}
