package frsf.cidisi.exercise.patrullero.search;

import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import frsf.cidisi.faia.exceptions.PrologConnectorException;
import frsf.cidisi.faia.simulator.SearchBasedAgentSimulator;

public class PatrulleroMain {

	public static void main(String[] args) throws PrologConnectorException {
		//TODO crear mapa y posicion agente e incidente
		Mapa mapa = null;
		Mapa mapaAgenteSinObstaculos = null;
		Interseccion posicionAgente = null;
		Interseccion posicionIncidente = null;

		Patrullero agent = new Patrullero(mapaAgenteSinObstaculos, posicionAgente, posicionIncidente);

		AmbienteCiudad environment = new AmbienteCiudad(mapa, posicionAgente);

		SearchBasedAgentSimulator simulator =
				new SearchBasedAgentSimulator(environment, agent);

		simulator.start();
	}

}
