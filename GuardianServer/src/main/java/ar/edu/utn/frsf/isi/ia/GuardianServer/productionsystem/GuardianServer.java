package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.EstadoGuardian;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.Guardian;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.Archivador;

public class GuardianServer extends Guardian {

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
		super.mostrarAccion(accion); //TODO enviar al movil un string
	}

}
