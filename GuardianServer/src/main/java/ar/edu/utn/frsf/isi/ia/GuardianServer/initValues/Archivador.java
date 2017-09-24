package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Archivador {

	private String PATH_IN = "src/main/resources/init.pl";
	private String PATH_OUT = "src/main/resources/customInit.pl";

	public List<String> leerArchivo() {
		List<String> archivo = new ArrayList<String>();
		File file = new File(PATH_IN);

		try(FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr);) {
			String linea;
			archivo.clear();
			while((linea = br.readLine()) != null) {
				archivo.add(linea);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return archivo;
	}

	public void escribirArchivo(List<String> archivo) {
		File file = new File(PATH_OUT);
		if(file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
		try(FileWriter fichero = new FileWriter(file); PrintWriter pw = new PrintWriter(fichero);) {
			archivo.forEach(linea -> pw.println(linea));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
