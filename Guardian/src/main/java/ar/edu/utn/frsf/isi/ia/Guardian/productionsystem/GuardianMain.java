package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import frsf.cidisi.faia.simulator.ProductionSystemBasedAgentSimulator;

public class GuardianMain {

	public static void main(String[] args) {
		//TODO hacer
	}

	private ProductionSystemBasedAgentSimulator simulator;

	public GuardianMain(AmbienteCiudad ambienteCiudad, Guardian guardian) {
		//Simular
		simulator = new ProductionSystemBasedAgentSimulator(ambienteCiudad, guardian);
	}

	public void start() {
		simulator.start();
	}
}
