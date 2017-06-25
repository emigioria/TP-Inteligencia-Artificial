package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import java.util.StringTokenizer;

import frsf.cidisi.faia.environment.Environment;

public class AmbienteCiudad extends Environment {

	private StringTokenizer frasesEscuchadasTokenizer;

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
		GuardianPerception perception = null;

		if(frasesEscuchadasTokenizer.hasMoreTokens()){
			perception = new GuardianPerception();
			perception.setPercepcion(frasesEscuchadasTokenizer.nextToken());
		}

		// Return the perception
		return perception;
	}

	public void setFrasesDichas(String frases) {
		frasesEscuchadasTokenizer = new StringTokenizer(frases, "\n\f\r.;¿?¡!");
	}

	@Override
	public String toString() {
		return environmentState.toString();
	}

	// The following methods are agent-specific:

}
