/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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
				"\nCallate y dame el celu o te corto." +
				"\nDar plata!");

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
