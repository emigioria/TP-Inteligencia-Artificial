package ar.edu.utn.frsf.isi.ia.GuardianServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.AmbienteCiudad;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.Guardian;
import ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem.GuardianServer;
import frsf.cidisi.faia.simulator.ProductionSystemBasedAgentSimulator;

@SpringBootApplication
@RestController
public class GuardianServerController {

	@PostMapping("/palabras")
	public String palabras(List<String> posiblesFrasesEscuchadas) {
		return "Hola";
	}

	@RequestMapping("/hello")
	public String greeting() {
		return "Hola";
	}

	public GuardianServerController() throws IOException {
		AmbienteCiudad ambienteCiudad = new AmbienteCiudad();
		Guardian agenteGuardian;
		try{
			agenteGuardian = new GuardianServer();
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
		ProductionSystemBasedAgentSimulator simulator = new ProductionSystemBasedAgentSimulator(ambienteCiudad, agenteGuardian);

		ambienteCiudad.getEnvironmentState().setFrasesDichas("Voy caminando por la calle, mientras pienso en voz alta..." +
				"\n¿Eameo, tenés hora?" +
				"\nEh, no." +
				"\nChe eameo pará un toque." +
				"\n¡Ayuda! ¡Socorro!" +
				"\nCallate, dame todo o te corto." +
				"\nBueno pero no me hagas nada!" +
				"\nAhí viene la policia. Chau gato!" +
				"\nMe robaron, ayuda" +
				"\nEstas arrestado ladrón!");

		File archivoSalida = new File("SalidaSimulacion.txt");
		if(archivoSalida.exists()){
			archivoSalida.delete();
		}
		archivoSalida.createNewFile();
		System.setOut(new PrintStream(new FileOutputStream(archivoSalida)));

		simulator.start();
	}
}
