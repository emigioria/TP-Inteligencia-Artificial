package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes;

import java.io.File;
import java.util.ArrayList;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.MapaGUI;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ManejadorArchivos {

	private Stage stage;

	public ManejadorArchivos(Stage stage) {
		this.stage = stage;
	}

	private File solicitarArchivoCarga() {
		FileChooser archivoSeleccionado = getSeleccionadorJSON();

		File retorno = archivoSeleccionado.showOpenDialog(stage);
		if(retorno != null){
			retorno = new File(retorno.toString());
		}
		return retorno;
	}

	private File solicitarArchivoGuardado() {
		FileChooser archivoSeleccionado = getSeleccionadorJSON();

		File retorno = archivoSeleccionado.showSaveDialog(stage);
		if(retorno != null){
			retorno = new File(retorno.toString());
		}
		return retorno;
	}

	private FileChooser getSeleccionadorJSON() {
		String tipo = "(*.json)";

		ArrayList<String> tiposFiltro = new ArrayList<>();
		tiposFiltro.add("*.json");

		ExtensionFilter filtro = new ExtensionFilter("Archivo JSON " + tipo, tiposFiltro);

		FileChooser archivoSeleccionado = new FileChooser();
		archivoSeleccionado.getExtensionFilters().add(filtro);

		return archivoSeleccionado;
	}

	public MapaGUI cargarMapa() {
		// TODO Auto-generated method stub
		return null;
	}

	public void guardarMapa(MapaGUI mapa) {
		// TODO Auto-generated method stub

	}
}
