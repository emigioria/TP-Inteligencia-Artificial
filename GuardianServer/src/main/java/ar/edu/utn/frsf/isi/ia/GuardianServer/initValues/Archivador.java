package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.utils.Charsets;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.Guardian;

public class Archivador {

	public static String INIT_PL;
	public static String CUSTOM_PL;

	{
		try{
			INIT_PL = new URI(Guardian.class.getResource("/db/init.pl").toString()).getPath();
			CUSTOM_PL = new URI(Guardian.class.getResource("/db/customInit.pl").toString()).getPath();
		} catch(URISyntaxException e){
			e.printStackTrace();
		}
	}

	public List<String> leerArchivo() {
		List<String> archivo = new ArrayList<>();
		File file = new File(INIT_PL);

		try(Reader reader = new InputStreamReader(new FileInputStream(file), Charsets.UTF_8);
				BufferedReader br = new BufferedReader(reader)){
			String linea;
			archivo.clear();
			while((linea = br.readLine()) != null){
				archivo.add(linea);
			}
		} catch(Exception e){
			e.printStackTrace();
		}

		return archivo;
	}

	public void escribirArchivo(List<String> archivo) {
		File file = new File(CUSTOM_PL);
		if(file.exists()){
			file.delete();
		}
		try{
			file.createNewFile();
		} catch(IOException e){
			e.printStackTrace();
		}
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
				PrintWriter pw = new PrintWriter(writer)){
			archivo.forEach(linea -> pw.println(linea));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
