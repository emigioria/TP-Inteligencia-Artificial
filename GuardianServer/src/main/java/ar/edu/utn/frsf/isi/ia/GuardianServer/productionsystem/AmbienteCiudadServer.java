/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.AmbienteCiudad;
import frsf.cidisi.faia.state.EnvironmentState;

@Service
public class AmbienteCiudadServer extends AmbienteCiudad {

	@Autowired
	public AmbienteCiudadServer(EnvironmentState environmentState) {
		// Create the environment state
		this.environmentState = environmentState;
	}

}
