package ar.edu.utn.frsf.isi.ia.Guardian.datos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Sinonimos {

	private String urlBase = "http://thesaurus.altervista.org/thesaurus/v1?key=P9VVIjkF5XeTFRVRTMcl&language=es_ES&output=json&word=";

	public ArrayList<String> sinonimosDe(String palabra) {
		try{
			return procesarRespuesta(getHTML(urlBase + palabra));
		} catch(Exception e){
			return new ArrayList<>();
		}
	}

	private ArrayList<String> procesarRespuesta(String json) {
		StringBuilder sinonimos = new StringBuilder();

		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(json).getAsJsonObject();
		o.get("response").getAsJsonArray().forEach(
				je -> sinonimos.append(je.getAsJsonObject().get("list").getAsJsonObject().get("synonyms") + "|"));

		ArrayList<String> retorno = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(sinonimos.toString(), "|");
		while(st.hasMoreTokens()){
			retorno.add(st.nextToken());
		}

		return retorno;
	}

	public static String getHTML(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while((line = rd.readLine()) != null){
			result.append(line);
		}
		rd.close();
		return result.toString();
	}
}
