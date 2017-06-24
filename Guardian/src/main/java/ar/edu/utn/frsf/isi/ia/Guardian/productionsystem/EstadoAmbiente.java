package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import frsf.cidisi.faia.state.EnvironmentState;

/**
 * This class represents the real world state.
 */
public class EstadoAmbiente extends EnvironmentState {

	public EstadoAmbiente() {
		super();
		this.initState();
	}

	/**
	 * This method is used to setup the initial real world.
	 */
	@Override
	public void initState() {

	}

	@Override
	public String toString() {
		return "Estado ambiente.";
	}

	// The following methods are agent-specific:
}
