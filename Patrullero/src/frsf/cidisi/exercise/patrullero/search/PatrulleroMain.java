package frsf.cidisi.exercise.patrullero.search;

import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.Calle;
import frsf.cidisi.exercise.patrullero.search.modelo.EstrategiasDeBusqueda;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import frsf.cidisi.exercise.patrullero.search.modelo.TipoIncidente;
import frsf.cidisi.faia.exceptions.PrologConnectorException;
import frsf.cidisi.faia.simulator.SearchBasedAgentSimulator;

public class PatrulleroMain {

	public static void main(String[] args) throws PrologConnectorException {
		//Crear mapa y posicion agente e incidente
		//Mapa ambiente
		Mapa mapaAmbiente = new Mapa();
		Interseccion i1 = new Interseccion(1L, 1, 1.0, 0.0);
		Interseccion i2 = new Interseccion(2L, 1, 1.0, 1.0);
		Interseccion i3 = new Interseccion(3L, 1, 0.0, 1.0);
		Interseccion i4 = new Interseccion(4L, 1, 0.0, 0.0);
		mapaAmbiente.getEsquinas().add(i1);
		mapaAmbiente.getEsquinas().add(i2);
		mapaAmbiente.getEsquinas().add(i3);
		mapaAmbiente.getEsquinas().add(i4);
		Calle c1 = new Calle(1L, "Moreno");
		Calle c2 = new Calle(2L, "Belgrano");
		Calle c3 = new Calle(3L, "San Martin");
		Calle c4 = new Calle(4L, "Sarmiento");
		mapaAmbiente.getCalles().add(c1);
		mapaAmbiente.getCalles().add(c2);
		mapaAmbiente.getCalles().add(c3);
		mapaAmbiente.getCalles().add(c4);
		try{
			new Arista(1L, 10, i1, i2, c1);
			new Arista(2L, 10, i2, i3, c2);
			new Arista(3L, 10, i3, i4, c3);
			new Arista(4L, 10, i4, i1, c4);
		} catch(Exception e){
		}

		Interseccion posicionAgenteAmbiente = i1;

		//Mapa patrullero
		Mapa mapaPatrullero = new Mapa();
		i1 = new Interseccion(1L, 1, 1.0, 0.0);
		i2 = new Interseccion(2L, 1, 1.0, 1.0);
		i3 = new Interseccion(3L, 1, 0.0, 1.0);
		i4 = new Interseccion(4L, 1, 0.0, 0.0);
		mapaPatrullero.getEsquinas().add(i1);
		mapaPatrullero.getEsquinas().add(i2);
		mapaPatrullero.getEsquinas().add(i3);
		mapaPatrullero.getEsquinas().add(i4);
		c1 = new Calle(1L, "Moreno");
		c2 = new Calle(2L, "Belgrano");
		c3 = new Calle(3L, "San Martin");
		c4 = new Calle(4L, "Sarmiento");
		mapaPatrullero.getCalles().add(c1);
		mapaPatrullero.getCalles().add(c2);
		mapaPatrullero.getCalles().add(c3);
		mapaPatrullero.getCalles().add(c4);
		try{
			new Arista(1L, 10, i1, i2, c1);
			new Arista(2L, 10, i2, i3, c2);
			new Arista(3L, 10, i3, i4, c3);
			new Arista(4L, 10, i4, i1, c4);
		} catch(Exception e){
		}

		Interseccion posicionAgentePatrullero = i1;
		Interseccion posicionIncidente = i4;

		mapaAmbiente.getEsquinas().stream().forEach(e -> e.getSalientes().sort((x, y) -> x.getCalle().getNombre().compareTo(y.getCalle().getNombre())));
		mapaPatrullero.getEsquinas().stream().forEach(e -> e.getSalientes().sort((x, y) -> x.getCalle().getNombre().compareTo(y.getCalle().getNombre())));

		Patrullero agent = new Patrullero(mapaPatrullero, posicionAgentePatrullero, posicionIncidente, TipoIncidente.ALARMA_DE_CASA_DE_FAMILIA);
		agent.setEstrategia(EstrategiasDeBusqueda.COSTO_UNIFORME);
		AmbienteCiudad environment = new AmbienteCiudad(mapaAmbiente, posicionAgenteAmbiente);
		new PatrulleroMain(environment, agent).start();
	}

	private SearchBasedAgentSimulator simulator;

	public PatrulleroMain(AmbienteCiudad ambienteCiudad, Patrullero patrullero) {
		//Simular
		simulator = new SearchBasedAgentSimulator(ambienteCiudad, patrullero);
	}

	public void start() {
		simulator.start();
	}
}
