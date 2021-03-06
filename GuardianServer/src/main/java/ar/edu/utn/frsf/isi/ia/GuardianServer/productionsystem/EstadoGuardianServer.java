/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.EstadoGuardian;
import frsf.cidisi.faia.exceptions.PrologConnectorException;

public class EstadoGuardianServer extends EstadoGuardian {

	public EstadoGuardianServer(String agentId) throws PrologConnectorException {
		super(new MultiUserPrologConnector(agentId));
	}

	public EstadoGuardianServer(String agentId, String archivoPl) throws PrologConnectorException {
		super(new MultiUserPrologConnector(agentId, archivoPl));
	}

}
