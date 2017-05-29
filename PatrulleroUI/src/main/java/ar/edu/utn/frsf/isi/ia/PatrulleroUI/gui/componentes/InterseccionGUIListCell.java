/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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
