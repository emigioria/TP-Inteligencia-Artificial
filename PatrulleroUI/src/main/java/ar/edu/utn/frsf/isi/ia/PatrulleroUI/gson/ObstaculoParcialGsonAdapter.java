/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Lugar;
import frsf.cidisi.exercise.patrullero.search.modelo.NombreObstaculo;
import frsf.cidisi.exercise.patrullero.search.modelo.ObstaculoParcial;
import frsf.cidisi.exercise.patrullero.search.modelo.Visibilidad;

public class ObstaculoParcialGsonAdapter implements JsonSerializer<ObstaculoParcial>, JsonDeserializer<ObstaculoParcial> {

	@Override
	public JsonElement serialize(ObstaculoParcial obstaculoParcial, Type tipo, JsonSerializationContext contexto) {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("id", obstaculoParcial.getId());
		jsonObject.addProperty("lugarID", obstaculoParcial.getLugar().getId());
		jsonObject.addProperty("TipoLugar", obstaculoParcial.getLugar().getClass().getCanonicalName());
		jsonObject.addProperty("nombre", obstaculoParcial.getNombre().name());
		jsonObject.addProperty("visibilidad", obstaculoParcial.getVisibilidad().name());
		jsonObject.addProperty("tiempoInicio", obstaculoParcial.getTiempoInicio());
		jsonObject.addProperty("tiempoFin", obstaculoParcial.getTiempoFin());
		jsonObject.addProperty("retardoMultiplicativo", obstaculoParcial.getRetardoMultiplicativo());

		return jsonObject;
	}

	@Override
	public ObstaculoParcial deserialize(JsonElement obstaculoParcialJSON, Type tipo, JsonDeserializationContext contexto) throws JsonParseException {
		JsonObject jsonObject = obstaculoParcialJSON.getAsJsonObject();

		Long id = jsonObject.get("id").getAsLong();
		NombreObstaculo nombre = NombreObstaculo.valueOf(jsonObject.get("nombre").getAsString());
		Visibilidad visibilidad = Visibilidad.valueOf(jsonObject.get("visibilidad").getAsString());
		Integer tiempoInicio = jsonObject.get("tiempoInicio").getAsInt();
		Integer tiempoFin = jsonObject.get("tiempoFin").getAsInt();
		Integer retardoMultiplicativo = jsonObject.get("retardoMultiplicativo").getAsInt();
		Long lugarId = jsonObject.get("lugarID").getAsLong();

		Lugar lugar;
		String className = jsonObject.get("TipoLugar").getAsString();
		try{
			Class<?> clz = Class.forName(className);
			if(clz.equals(Interseccion.class)){
				lugar = new Interseccion(lugarId, null, null, null);
			}
			else if(clz.equals(Arista.class)){
				lugar = new Arista(lugarId, null, null, null, null);
			}
			else{
				throw new NoClassDefFoundError();
			}
		} catch(Exception e){
			throw new JsonParseException(e);
		}

		return new ObstaculoParcial(id, nombre, tiempoInicio, tiempoFin, visibilidad, lugar, retardoMultiplicativo);
	}

}
