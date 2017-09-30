/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.AmbienteCiudad;
import ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem.EstadoAmbienteServer;
import ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem.GuardianServer;
import frsf.cidisi.faia.simulator.ProductionSystemBasedAgentSimulator;

@SpringBootApplication
@Controller
public class GuardianServerApplication {

	@Autowired
	private SimpMessagingTemplate simpTemplate;
	private LinkedBlockingDeque<String> frasesEscuchadas = new LinkedBlockingDeque<>();

	@MessageMapping("/fraseEscuchada")
	public void receiveGreeting(String message) throws Exception {
		frasesEscuchadas.put(message);
	}

	public void enviarMensaje(Long agentId, String mensaje) {
		simpTemplate.convertAndSend("/topic/accion", mensaje);
	}

	@Autowired
	public GuardianServerApplication() throws IOException {
		new Thread(() -> {
			GuardianServer agenteGuardian;
			try{
				Long agentId = Thread.currentThread().getId();
				agenteGuardian = new GuardianServer(agentId) {

					@Override
					public void enviarAccion(String message) {
						GuardianServerApplication.this.enviarMensaje(agentId, message);
					}
				};
			} catch(Exception e1){
				e1.printStackTrace();
				return;
			}

			EstadoAmbienteServer estadoAmbienteServer = new EstadoAmbienteServer(frasesEscuchadas);
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
