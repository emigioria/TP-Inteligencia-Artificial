/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.EstadoAmbiente;

/**
 * This class represents the real world state.
 */
@Controller
public class EstadoAmbienteServer extends EstadoAmbiente {

	private BlockingQueue<String> frasesEscuchadas = new LinkedBlockingQueue<>();

	@MessageMapping("/fraseEscuchada")
	public void receiveGreeting(String message) throws Exception {
		frasesEscuchadas.put(message);
	}

	public EstadoAmbienteServer() {
		super();
		this.initState();
	}

	@Override
	public String getNextFrase() {
		try{
			return frasesEscuchadas.take();
		} catch(InterruptedException e){
			return null;
		}
	}

	/**
	 * This method is used to setup the initial real world.
	 */
	@Override
	public void initState() {

	}

	@Override
	public void setFrasesDichas(String frases) {
		throw new RuntimeException();
	}

	@Override
	public String toString() {
		return "Estado ambiente.";
	}

	// The following methods are agent-specific:
}
