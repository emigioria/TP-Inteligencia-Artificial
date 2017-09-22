package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.Guardian;

public class GuardianServer extends Guardian {

	public GuardianServer() throws Exception {
		super();
	}

	@Override
	protected void mostrarAccion(String accion) {
		super.mostrarAccion(accion); //TODO enviar al movil un string
	}

}
