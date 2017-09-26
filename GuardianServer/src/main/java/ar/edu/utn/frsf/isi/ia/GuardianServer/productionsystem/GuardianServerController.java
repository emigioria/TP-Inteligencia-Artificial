/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.AmbienteCiudad;
import frsf.cidisi.faia.simulator.ProductionSystemBasedAgentSimulator;

@SpringBootApplication
public class GuardianServerController {

	@Autowired
	public GuardianServerController(EstadoAmbienteServer estadoAmbienteServer, GuardianServer agenteGuardian) throws IOException {
		new Thread(() -> {
			AmbienteCiudad ambienteCiudad = new AmbienteCiudad(estadoAmbienteServer);
			ProductionSystemBasedAgentSimulator simulator = new ProductionSystemBasedAgentSimulator(ambienteCiudad, agenteGuardian);

			try{
				File archivoSalida = new File("SalidaSimulacion.txt");
				if(archivoSalida.exists()){
					archivoSalida.delete();
				}
				archivoSalida.createNewFile();
				System.setOut(new PrintStream(new FileOutputStream(archivoSalida)));
			} catch(Exception e){
				e.printStackTrace();
				System.out.println("No se guardará la ejecución");
			}

			simulator.start();
		}).start();
	}
}
