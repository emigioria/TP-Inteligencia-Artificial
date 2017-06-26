package ar.edu.utn.frsf.isi.ia.Guardian.gui.controladores;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.AmbienteCiudad;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.Guardian;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.GuardianMain;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorPatrullero;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class VentanaDeComandosController extends ControladorPatrullero {

	public static final String URL_VISTA = "vistas/ventanaDeComandos.fxml";

	@FXML
	private TextArea taEntrada;

	@FXML
	private void tomarEntrada() {
		AmbienteCiudad ambiente = new AmbienteCiudad();
		Guardian agente;
		try{
			agente = new Guardian();
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}
		GuardianMain main = new GuardianMain(ambiente, agente);

		ambiente.getEnvironmentState().setFrasesDichas(taEntrada.getText());

		main.start();
	}

	@Override
	protected void inicializar() {

	}

}
