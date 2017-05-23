/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.Calle;

public class CalleGsonAdapter implements JsonSerializer<Calle>, JsonDeserializer<Calle> {

	@Override
	public JsonElement serialize(Calle calle, Type tipo, JsonSerializationContext contexto) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", calle.getId());
		jsonObject.addProperty("nombre", calle.getNombre());

		JsonArray jsonTramosArray = new JsonArray();
		calle.getTramos().forEach(a -> jsonTramosArray.add(contexto.serialize(a)));
		jsonObject.add("tramos", jsonTramosArray);

		return jsonObject;
	}

	@Override
	public Calle deserialize(JsonElement calleJSON, Type tipo, JsonDeserializationContext contexto) throws JsonParseException {
		JsonObject jsonObject = calleJSON.getAsJsonObject();

		Long id = jsonObject.get("id").getAsLong();
		String nombre = jsonObject.get("nombre").getAsString();

		JsonArray jsonTramosArray = jsonObject.get("tramos").getAsJsonArray();
		ArrayList<Arista> tramos = new ArrayList<>();
		jsonTramosArray.forEach(jsonArista -> tramos.add(contexto.deserialize(jsonArista.getAsJsonObject(), Arista.class)));

		Calle calle = new Calle(id, nombre);
		calle.setTramos(tramos);
		tramos.stream().forEach(a -> a.setCalle(calle));
		return calle;
	}

}
