package ar.edu.utn.frsf.isi.ia.GuardianServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.AmbienteCiudad;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.Guardian;
import frsf.cidisi.faia.simulator.ProductionSystemBasedAgentSimulator;

@SpringBootApplication
@Controller
public class GuardianServerApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(GuardianServerApplication.class, args);
	}

	public GuardianServerApplication() throws IOException {
		AmbienteCiudad ambienteCiudad = new AmbienteCiudad();
		Guardian agenteGuardian;
		try{
			agenteGuardian = new Guardian() {
				@Override
				protected void initAgentState() throws Exception {
					super.initAgentState(); //TODO cambiar por archivo generado
				}
			};
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

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public void greeting(List<String> posiblesFrasesEscuchadas) throws Exception {

	}
}
