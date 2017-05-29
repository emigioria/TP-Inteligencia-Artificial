package frsf.cidisi.exercise.patrullero.search;

import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.environment.Environment;

public class AmbienteCiudad extends Environment {

	public AmbienteCiudad(Mapa mapa, Interseccion posicionAgente) {
		// Create the environment state
		this.environmentState = new EstadoAmbiente(mapa, posicionAgente);
	}

	@Override
	public EstadoAmbiente getEnvironmentState() {
		return (EstadoAmbiente) super.getEnvironmentState();
	}

	/**
	 * This method is called by the simulator. Given the Agent, it creates
	 * a new perception reading, for example, the agent position.
	 *
	 * @param agent
	 * @return A perception that will be given to the agent by the simulator.
	 */
	@Override
	public PatrulleroPerception getPercept() {
		// Create a new perception to return
		PatrulleroPerception perception = new PatrulleroPerception();
		perception.getobstaculos_detectables().addAll(this.getEnvironmentState().getObstaculosVisiblesAgente());

		// Return the perception
		return perception;
	}

	@Override
	public String toString() {
		return environmentState.toString();
	}

	@Override
	public boolean agentFailed(Action actionReturned) {
		return this.getEnvironmentState().estaAgenteEnCorteTotal();
	}

	@Override
	public void close() {
		super.close();
		System.out.println("Tiempo total: " + getEnvironmentState().getHora());
	}
	// The following methods are agent-specific:

}
