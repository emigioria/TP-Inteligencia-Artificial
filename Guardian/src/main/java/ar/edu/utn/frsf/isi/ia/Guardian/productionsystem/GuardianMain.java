/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import frsf.cidisi.faia.simulator.ProductionSystemBasedAgentSimulator;

public class GuardianMain {

	public static void main(String[] args) throws IOException {
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
				"\nChe eameo pará un toque." +
				"\n¡Ayuda! ¡Socorro!" +
				"\nCallate, dame todo o te corto." +
				"\nBueno pero no me hagas nada!" +
				"\nAhí viene la policia. Chau gato!" +
				"\nMe robaron, ayuda" +
				"\nEstas arrestado ladrón!");

		File archivoSalida = new File("SalidaSimulacion.txt");
		if(archivoSalida.exists()){
			archivoSalida.delete();
		}
		archivoSalida.createNewFile();
		System.setOut(new PrintStream(new FileOutputStream(archivoSalida)));

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
