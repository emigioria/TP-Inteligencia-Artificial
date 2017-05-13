package frsf.cidisi.exercise.patrullero.search.actions;

import java.util.ListIterator;

import frsf.cidisi.exercise.patrullero.search.EstadoAmbiente;
import frsf.cidisi.exercise.patrullero.search.EstadoPatrullero;
import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

public class CambiarOrientacion extends SearchAction {

	/**
	 * This method updates a tree node state when the search process is running.
	 * It does not updates the real world state.
	 */
	@Override
	public SearchBasedAgentState execute(SearchBasedAgentState s) {
		EstadoPatrullero estadoPatrullero = (EstadoPatrullero) s;
		ListIterator<Arista> orientacionAgente = estadoPatrullero.getOrientacion();

		// PreConditions: que haya otra calle para avanzar
		if(orientacionAgente.hasNext()){
			// PostConditions: moverse a la siguiente orientación
			orientacionAgente.next();
			if(orientacionAgente.hasNext()){
				//si esta existe ya estoy ahí, y fue exitoso
				return estadoPatrullero;
			}
			else{
				//si esta no existe volver atrás, y no fue exitoso
				orientacionAgente.previous();
			}
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

		// PreConditions: que se pudo cambiar orientacion
		// Update the agent state
		if(this.execute(estadoPatrullero) != null){
			// PostConditions: actualizar el mundo real con la nueva orientacion del agente
			// Update the real world
			estadoAmbiente.getOrientacionAgente().next();
			estadoAmbiente.addHora(getCost(estadoPatrullero));
			return estadoAmbiente;
		}

		return null;
	}

	/**
	 * This method returns the action cost.
	 */
	@Override
	public Double getCost(SearchBasedAgentState sbs) {
		//Es gratis
		return new Double(0);
	}

	/**
	 * This method is not important for a search based agent, but is essensial
	 * when creating a calculus based one.
	 */
	@Override
	public String toString() {
		return "CambiarOrientacion";
	}
}