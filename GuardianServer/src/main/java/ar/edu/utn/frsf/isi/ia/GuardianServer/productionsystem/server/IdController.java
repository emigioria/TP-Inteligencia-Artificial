package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem.server;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.Archivador;

@RestController
public class IdController {

	private File proximoId;
	private Archivador archivador = new Archivador();

	@GetMapping("/id")
	public String getId() throws IOException {
		return getIdAndIncrement().toString();
	}

	private String getIdAndIncrement() throws IOException {
		String id = archivador.leerArchivo(proximoId).get(0);
		String nextId = (new Long(id) + 1) + "";
		archivador.escribirAArchivo(Collections.singletonList(nextId), proximoId);
		return id;
	}

	public IdController() throws IOException {
		proximoId = new File("proximoId");
		if(!proximoId.exists()){
			proximoId.createNewFile();
			archivador.escribirAArchivo(Collections.singletonList((1) + ""), proximoId);
		}
	}
}
