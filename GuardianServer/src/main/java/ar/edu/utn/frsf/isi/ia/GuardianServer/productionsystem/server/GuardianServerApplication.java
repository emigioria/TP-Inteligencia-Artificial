/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class GuardianServerApplication implements SendMessageListener {

	@Autowired
	private SimpMessagingTemplate simpTemplate;
	private Map<String, GuardianExecutionInstance> guardianes = new HashMap<>();

	@MessageMapping("/fraseEscuchada")
	public void receiveGreeting(Mensaje message) throws Exception {
		String id = message.getId();
		if(!guardianes.containsKey(id)){
			guardianes.put(id, new GuardianExecutionInstance(id, this));
		}
		guardianes.get(id).escuchar(message.getMensaje());
	}

	@Override
	public void enviarMensaje(Mensaje mensaje) {
		simpTemplate.convertAndSend("/topic/accion-" + mensaje.getId(), mensaje.getMensaje());
	}

}
