package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;

public class GuardianPerception extends Perception {

	private String percepcion;

	public GuardianPerception() {

	}

	public GuardianPerception(Agent agent, Environment environment) {
		super(agent, environment);
	}

	/**
	 * This method is used to setup the perception.
	 */
	@Override
	public void initPerception(Agent agentIn, Environment environmentIn) {

	}

	@Override
	public String toString() {
		return percepcion;
	}

	public void setPercepcion(String percepcion) {
		this.percepcion = percepcion;
	}

	public String getPercepcion() {
		return percepcion;
	}
}
