package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import frsf.cidisi.faia.environment.Environment;

public class AmbienteCiudad extends Environment {

	private String frasesDichas;
	
	public AmbienteCiudad() {
		// Create the environment state
		this.environmentState = new EstadoAmbiente();
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
	public GuardianPerception getPercept() {
		// Create a new perception to return
		GuardianPerception perception = new GuardianPerception();
		
		// Return the perception
		return perception;
	}
	
	public void setFrasesDichas(String frases){
		frasesDichas = frases;
	}

	@Override
	public String toString() {
		return environmentState.toString();
	}

	// The following methods are agent-specific:

}
