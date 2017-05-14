package frsf.cidisi.exercise.patrullero.search.actions;

import java.util.Iterator;

import frsf.cidisi.exercise.patrullero.search.EstadoAmbiente;
import frsf.cidisi.exercise.patrullero.search.EstadoPatrullero;
import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

public class Avanzar extends SearchAction {

	/**
	 * This method updates a tree node state when the search process is running.
	 * It does not updates the real world state.
	 */
	@Override
	public SearchBasedAgentState execute(SearchBasedAgentState s) {
		EstadoPatrullero estadoPatrullero = (EstadoPatrullero) s;
		Iterator<Arista> orientacionAgente = estadoPatrullero.getOrientacion();

		//TODO falta analizar los obstáculos
		// PreConditions: que se esté apuntando a una calle
		if(orientacionAgente.hasNext()){
			// PostConditions: moverse a la siguiente esquina y apuntar a la primera calle saliente
			estadoPatrullero.setPosicion(orientacionAgente.next().getDestino());
			estadoPatrullero.initOrientacion();
			return estadoPatrullero;
		}
		return null;
	}

	/**
	 * This method updates the agent state and the real world state.
	 */
	@Override
	public EnvironmentState execute(AgentState ast, EnvironmentState est) {
		EstadoAmbiente estadoAmbiente = (EstadoAmbiente) est;
		EstadoPatrullero estadoPatrullero = ((EstadoPatrullero) ast);

		//TODO faltan los obstáculos (ver invisibles)
		// PreConditions: que se pudo avanzar
		// Update the agent state
		if(this.execute(estadoPatrullero) != null){ //TODO esta línea cambia con obstaculos
			//TODO recordar setear AgenteEnCorteTotal si el agente eligió pasar por una arista con obstaculo total o se movió a una intersección con uno
			// PostConditions: actualizar el mundo real moviendo el agente a la siguiente esquina y apuntando a la primera calle saliente
			// Update the real world
			estadoAmbiente.setPosicionAgente(estadoPatrullero.getPosicion());
			estadoAmbiente.initOrientacion();
			estadoAmbiente.addHora(getCost(estadoPatrullero).longValue());
			return estadoAmbiente;
		}

		return null;
	}

	private Long getCostAvanzar(SearchBasedAgentState sbs) {
		EstadoPatrullero estadoPatrullero = ((EstadoPatrullero) sbs);
		Interseccion posicion = estadoPatrullero.getPosicion();
		Arista salida = posicion.getSalientes().get(estadoPatrullero.getOrientacion().nextIndex());
		return getCosto(salida.getDestino(), salida);
	}

	private Long getCosto(Interseccion interseccion, Arista arista) {
		Long pesoArista = arista.getPeso();
		for(Obstaculo obs: arista.getObstaculos()){
			pesoArista = obs.getPeso(pesoArista);
		}
		Long pesoInterseccion = interseccion.getPeso();
		for(Obstaculo obs: arista.getDestino().getObstaculos()){
			pesoInterseccion = obs.getPeso(pesoInterseccion);
		}
		return (pesoArista < 0 || pesoInterseccion < 0) ? (-1) : (1) * (Math.abs(pesoArista) + Math.abs(pesoInterseccion));
	}

	/**
	 * This method returns the action cost.
	 */
	//Costo después de ejecutar la acción
	@Override
	public Double getCost(SearchBasedAgentState sbs) {
		EstadoPatrullero estadoPatrullero = ((EstadoPatrullero) sbs);
		Interseccion posicion = estadoPatrullero.getPosicion();
		Arista entrada = estadoPatrullero.getUltimaCalleRecorrida();
		return getCosto(posicion, entrada).doubleValue();
	}

	/**
	 * This method is not important for a search based agent, but is essensial
	 * when creating a calculus based one.
	 */
	@Override
	public String toString() {
		return "Avanzar";
	}
}