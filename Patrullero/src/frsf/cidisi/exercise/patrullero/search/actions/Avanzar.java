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

	/**
	 * This method returns the action cost.
	 */
	@Override
	public Double getCost(SearchBasedAgentState sbs) {
		EstadoPatrullero estadoPatrullero = ((EstadoPatrullero) sbs);
		Interseccion posicion = estadoPatrullero.getPosicion();
		Arista salida = posicion.getSalientes().get(estadoPatrullero.getOrientacion().nextIndex());
		Long pesoArista = salida.getPeso();
		for(Obstaculo obs: salida.getObstaculos()){
			pesoArista = obs.getPeso(pesoArista);
		}
		Long pesoInterseccion = salida.getDestino().getPeso();
		for(Obstaculo obs: salida.getDestino().getObstaculos()){
			pesoInterseccion = obs.getPeso(pesoInterseccion);
		}
		return (double) (pesoArista + pesoInterseccion);
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