package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.InterseccionGUI;
import javafx.scene.control.ListCell;

public class InterseccionGUIListCell extends ListCell<InterseccionGUI> {
	@Override
	protected void updateItem(InterseccionGUI item, boolean empty) {
		super.updateItem(item, empty);
		if(item != null){
			setText(item.toString());
		}
	}
}
