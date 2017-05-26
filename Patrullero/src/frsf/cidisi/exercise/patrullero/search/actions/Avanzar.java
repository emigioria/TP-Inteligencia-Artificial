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

		// PreConditions: que la calle apuntada no esté cerrada y que se esté apuntando a una salida
		if(getCostAvanzar(s) > 0 && orientacionAgente.hasNext()){
			// PostConditions: moverse a la siguiente esquina y apuntar a la primera calle saliente de la misma
			Arista aristaElegida = orientacionAgente.next();
			estadoPatrullero.setUltimaCalleRecorrida(aristaElegida);
			estadoPatrullero.setPosicion(aristaElegida.getDestino());
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

		Iterator<Arista> orientacionAgenteAmbiente = estadoAmbiente.getOrientacionAgente();
		Iterator<Arista> orientacionAgentePatrullero = estadoPatrullero.getOrientacion();

		// PreConditions: que se esté apuntando a una salida
		if(orientacionAgenteAmbiente.hasNext()){
			// Update the agent state
			// PostConditions: moverse a la siguiente esquina y apuntar a la primera calle saliente de la misma
			Arista aristaElegidaPatrullero = orientacionAgentePatrullero.next();
			estadoPatrullero.setUltimaCalleRecorrida(aristaElegidaPatrullero);
			estadoPatrullero.setPosicion(aristaElegidaPatrullero.getDestino());
			estadoPatrullero.initOrientacion();

			// Update the real world
			// PostConditions: actualizar el mundo real moviendo el agente a la siguiente esquina y apuntando a la primera calle saliente de la misma
			Arista aristaElegidaAmbiente = orientacionAgenteAmbiente.next();
			estadoAmbiente.setUltimaCalleRecorridaAgente(aristaElegidaAmbiente);
			estadoAmbiente.setPosicionAgente(aristaElegidaAmbiente.getDestino());
			estadoAmbiente.initOrientacion();

			//Actualizo la hora y si chocó con un obstaculo total
			Integer tiempo = getCost(estadoAmbiente);
			Boolean corteTotalEncontrado = (tiempo < 0);
			tiempo = Math.abs(tiempo);
			estadoAmbiente.addHora(tiempo.longValue());
			if(corteTotalEncontrado){
				estadoAmbiente.setAgenteEnCorteTotal(true);
			}
			return estadoAmbiente;
		}

		return null;
	}

	//Costo antes de ejecutar la acción
	private Integer getCostAvanzar(SearchBasedAgentState sbs) {
		EstadoPatrullero estadoPatrullero = ((EstadoPatrullero) sbs);
		Interseccion posicion = estadoPatrullero.getPosicion();
		Arista salida = posicion.getSalientes().get(estadoPatrullero.getOrientacion().nextIndex());
		return getCosto(salida.getDestino(), salida);
	}

	private Integer getCosto(Interseccion interseccion, Arista arista) {
		Integer pesoArista = arista.getPeso();
		for(Obstaculo obs: arista.getObstaculos()){
			pesoArista = obs.getPeso(pesoArista);
		}
		Integer pesoInterseccion = interseccion.getPeso();
		for(Obstaculo obs: arista.getDestino().getObstaculos()){
			pesoInterseccion = obs.getPeso(pesoInterseccion);
		}
		return ((pesoArista < 0 || pesoInterseccion < 0) ? (-1) : (1)) * (Math.abs(pesoArista) + Math.abs(pesoInterseccion));
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

	//Costo después de ejecutar la acción
	private Integer getCost(EnvironmentState sbs) {
		EstadoAmbiente estadoAmbiente = ((EstadoAmbiente) sbs);
		Interseccion posicion = estadoAmbiente.getPosicionAgente();
		Arista entrada = estadoAmbiente.getUltimaCalleRecorridaAgente();
		return getCosto(posicion, entrada, estadoAmbiente.getHora());
	}

	private Integer getCosto(Interseccion interseccion, Arista arista, Long hora) {
		Integer pesoArista = arista.getPeso();
		for(Obstaculo obs: arista.getObstaculos()){
			if(obs.getTiempoInicio() <= hora && obs.getTiempoFin() > hora){
				pesoArista = obs.getPeso(pesoArista);
			}
		}
		Integer pesoInterseccion = interseccion.getPeso();
		for(Obstaculo obs: arista.getDestino().getObstaculos()){
			if(obs.getTiempoInicio() <= hora && obs.getTiempoFin() > hora){
				pesoInterseccion = obs.getPeso(pesoInterseccion);
			}
		}
		return ((pesoArista < 0 || pesoInterseccion < 0) ? (-1) : (1)) * (Math.abs(pesoArista) + Math.abs(pesoInterseccion));
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