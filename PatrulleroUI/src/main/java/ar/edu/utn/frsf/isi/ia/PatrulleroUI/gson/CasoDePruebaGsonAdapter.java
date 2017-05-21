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

import frsf.cidisi.exercise.patrullero.search.modelo.CasoDePrueba;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;

public class CasoDePruebaGsonAdapter implements JsonSerializer<CasoDePrueba>, JsonDeserializer<CasoDePrueba> {

	@Override
	public JsonElement serialize(CasoDePrueba casoDePrueba, Type tipo, JsonSerializationContext contexto) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("posicionInicialPatrullero", casoDePrueba.getPosicionInicialPatrullero().getId());
		jsonObject.addProperty("posicionIncidente", casoDePrueba.getPosicionIncidente().getId());

		JsonArray jsonObstaculosArray = new JsonArray();
		casoDePrueba.getObstaculos().forEach(o -> jsonObstaculosArray.add(contexto.serialize(o, Obstaculo.class)));
		jsonObject.add("obstaculos", jsonObstaculosArray);

		return jsonObject;
	}

	@Override
	public CasoDePrueba deserialize(JsonElement casoDePruebaJSON, Type tipo, JsonDeserializationContext contexto) throws JsonParseException {
		JsonObject jsonObject = casoDePruebaJSON.getAsJsonObject();

		Long pipId = jsonObject.get("posicionInicialPatrullero").getAsLong();
		Interseccion posicionInicialPatrullero = new Interseccion(pipId, null, null, null);
		Long piId = jsonObject.get("posicionIncidente").getAsLong();
		Interseccion posicionIncidente = new Interseccion(piId, null, null, null);

		JsonArray jsonObstaculosArray = jsonObject.get("obstaculos").getAsJsonArray();
		ArrayList<Obstaculo> obstaculos = new ArrayList<>();
		jsonObstaculosArray.forEach(jsonObstaculo -> obstaculos.add(contexto.deserialize(jsonObstaculo.getAsJsonObject(), Obstaculo.class)));

		CasoDePrueba casoDePrueba = new CasoDePrueba();
		casoDePrueba.setPosicionInicialPatrullero(posicionInicialPatrullero);
		casoDePrueba.setPosicionIncidente(posicionIncidente);
		casoDePrueba.setObstaculos(obstaculos);
		return casoDePrueba;
	}

}
