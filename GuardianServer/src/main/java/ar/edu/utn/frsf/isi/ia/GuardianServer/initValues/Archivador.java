package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.Guardian;

public class Archivador {

	private static String PATH_IN;
	private static String PATH_OUT;

	{
		try{
			PATH_IN = new URI(Guardian.class.getResource("/db/init.pl").toString()).getPath();
			PATH_OUT = new URI(Guardian.class.getResource("/db").toString()).getPath() + "/customInit.pl";
		} catch(URISyntaxException e){
			e.printStackTrace();
		}
	}

	public List<String> leerArchivo() {
		List<String> archivo = new ArrayList<>();
		File file = new File(PATH_IN);

		try(FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr);){
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
		File file = new File(PATH_OUT);
		if(file.exists()){
			file.delete();
		}
		try{
			file.createNewFile();
		} catch(IOException e){
			e.printStackTrace();
		}
		try(FileWriter fichero = new FileWriter(file); PrintWriter pw = new PrintWriter(fichero);){
			archivo.forEach(linea -> pw.println(linea));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
