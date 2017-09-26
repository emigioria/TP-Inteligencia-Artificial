/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.EstadoGuardian;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.Guardian;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.Archivador;

@Controller
public class GuardianServer extends Guardian {

	@Autowired
	private SimpMessagingTemplate simpTemplate;

	public void enviarAccion(String message) {
		simpTemplate.convertAndSend("/topic/accion", message);
	}

	public GuardianServer() throws Exception {
		super();
	}

	@Override
	protected void initAgentState() throws Exception {
		EstadoGuardian agState = new EstadoGuardian(Archivador.CUSTOM_PL);
		this.setAgentState(agState);
	}

	@Override
	protected void mostrarAccion(String accion) {
		super.mostrarAccion(accion);
		this.enviarAccion(accion);
	}

}
