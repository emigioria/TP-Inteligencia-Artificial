package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import frsf.cidisi.faia.environment.Environment;

public class AmbienteCiudad extends Environment {

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
	 * @return A perception that will be given to the agent by the simulator. null if there's nothing to percieve.
	 */
	@Override
	public GuardianPerception getPercept() {
		// Create a new perception to return
		GuardianPerception perception = new GuardianPerception();
		String frase = this.getEnvironmentState().getNextFrase();
		if(frase != null){
			perception.setPercepcion(frase);
			return perception;
		}
		else{
			return null;
		}
	}

	@Override
	public String toString() {
		return environmentState.toString();
	}

	// The following methods are agent-specific:

}
