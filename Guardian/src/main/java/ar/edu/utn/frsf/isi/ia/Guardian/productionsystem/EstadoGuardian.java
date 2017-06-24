package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.PrologConnector;
import frsf.cidisi.faia.solver.productionsystem.WorkingMemory;

/**
 * Represent the internal state of the Agent.
 */
public class EstadoGuardian implements WorkingMemory {

	public PrologConnector plc;

	public EstadoGuardian() {

		this.initState();
	}

	/**
	 * This method is optional, and sets the initial state of the agent.
	 */
	public void initState() {

	}

	/**
	 * This method is used to update the Agent State when a Perception is
	 * received by the Simulator.
	 */
	@Override
	public void updateState(Perception p) {

	}

	// The following methods are agent-specific:
	public PrologConnector getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

}
