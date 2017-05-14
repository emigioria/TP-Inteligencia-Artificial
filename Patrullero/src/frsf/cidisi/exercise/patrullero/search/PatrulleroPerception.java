package frsf.cidisi.exercise.patrullero.search;

import java.util.HashSet;
import java.util.Set;

import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;

public class PatrulleroPerception extends Perception {

	private Set<Obstaculo> obstaculos_detectables = new HashSet<>();

	public PatrulleroPerception() {

	}

	public PatrulleroPerception(Agent agent, Environment environment) {
		super(agent, environment);
	}

	/**
	 * This method is used to setup the perception.
	 */
	@Override
	public void initPerception(Agent agentIn, Environment environmentIn) {
		//		Patrullero agent = (Patrullero) agentIn;
		//		AmbienteCiudad environment = (AmbienteCiudad) environmentIn;
		//		EstadoAmbiente environmentState = environment.getEnvironmentState();
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		for(Obstaculo obs: obstaculos_detectables){
			str.append("Nombre: ").append(obs.getNombre()).append(". Lugar: ").append(obs.getLugar()).append(".\n");
		}
		return str.toString();
	}

	// The following methods are agent-specific:
	public Set<Obstaculo> getobstaculos_detectables() {
		return obstaculos_detectables;
	}

	public void setobstaculos_detectables(Set<Obstaculo> arg) {
		this.obstaculos_detectables = arg;
	}

}
