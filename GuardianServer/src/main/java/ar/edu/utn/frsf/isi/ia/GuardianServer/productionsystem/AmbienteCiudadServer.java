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
