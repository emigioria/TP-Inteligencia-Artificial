package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import frsf.cidisi.faia.simulator.ProductionSystemBasedAgentSimulator;

public class GuardianMain {

	public static void main(String[] args) {
		AmbienteCiudad ambiente = new AmbienteCiudad();
		Guardian agente;
		try{
			agente = new Guardian();
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
		GuardianMain main = new GuardianMain(ambiente, agente);

		ambiente.getEnvironmentState().setFrasesDichas("Voy caminando por la calle, mientras pienso en voz alta..." +
				"\n¿Eameo, tenés hora?" +
				"\nEh, no." +
				"\nChe ameo pará un toque." +
				"\n¡Ayuda!" +
				"\nCallate y dame el celu o te corto.");

		main.start();
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
