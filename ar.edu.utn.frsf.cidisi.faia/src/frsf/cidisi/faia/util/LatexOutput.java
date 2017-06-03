/*
 * Copyright 2007-2009 Georgina Stegmayer, Milagros Gutiérrez, Jorge Roa,
 * Luis Ignacio Larrateguy y Milton Pividori.
 *
 * LatexOutput program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LatexOutput program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LatexOutput program. If not, see <http://www.gnu.org/licenses/>.
 */
package frsf.cidisi.faia.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import frsf.cidisi.faia.exceptions.LatexOutputException;
import frsf.cidisi.faia.simulator.events.EventHandler;
import frsf.cidisi.faia.solver.search.NTree;

public enum LatexOutput implements EventHandler {
	INSTANCE {
		@Override
		public void runEventHandler(Object[] params) {
			new Thread(() -> {
				try{
					semaphore.acquire();

					LatexOutput.compileLatexFiles(true);

					semaphore.release();
				} catch(InterruptedException | LatexOutputException e){
					e.printStackTrace();
				}
			}).start();
		}
	};

	private static final String lineSeparator = System.getProperty("line.separator");
	private static final String pdflatexResourceDir = "/pdflatex/";
	private static final String pdflatexDir = "pdflatex/";
	private static String[] archivosLatex = new String[] { "a0poster.cls", "a0size.sty", "nodo.sty", "qtree.sty" };
	private static int fileIdx = 0;
	private static Semaphore semaphore = new Semaphore(1);

	private static void copyFiles(String faiaPdflatex) throws IOException, URISyntaxException {
		for(String archivo: archivosLatex){
			FileOperations.CopyFile(
					new URI(LatexOutput.class.getResource(faiaPdflatex + archivo).toString()).getPath(),
					pdflatexDir + archivo);
		}
	}

	private static void compileLatexFiles(boolean removeTexFiles) throws LatexOutputException {
		// Copio los archivos necesarios para poder compilar con pdflatex
		try{
			LatexOutput.copyFiles(LatexOutput.pdflatexResourceDir);
		} catch(IOException | URISyntaxException e){
			throw new LatexOutputException("LaTeX files not found: " + e.getMessage());
		}

		// Creo el objeto que representa la carpeta pdfLatex
		File carpetaPdflatex = new File(pdflatexDir);

		// Elimino los archivos PDF que haya en el directorio pdfLatex
		Arrays.stream(carpetaPdflatex.listFiles(new PdfFilesFilter())).forEach(archivoPdf -> archivoPdf.delete());

		Process p;
		String[] comando;

		System.out.println("Compiling Latex files...");

		// Por cada archivo .tex compilo a PDF
		for(File archivoTex: carpetaPdflatex.listFiles(new TexFilter())){

			System.out.print("  " + archivoTex.getName());
			comando = new String[] { "pdflatex", "-quiet", "-halt-on-error", archivoTex.getName() };

			try{
				// Ejecuto el comando para la compilación
				p = Runtime.getRuntime().exec(comando, null, carpetaPdflatex);

				// Espero que termine de compilar y muestro el estado de la ejecución (si
				// fue exitoso o no)
				p.waitFor();
				if(p.exitValue() == 0){
					System.out.print(" -> Ok");
				}
				else{
					System.out.println(" -> There was an error. This is standard output of the 'pdflatex' command:");
					System.out.println();
					BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

					String s = null;
					while((s = stdError.readLine()) != null){
						System.out.println("\t" + s);
					}

					System.out.println();

					throw new LatexOutputException("'pdflatex' execution failed.");
				}
			} catch(IOException e){
				throw new LatexOutputException("LaTeX/MiKTeX is not installed: " + e.getMessage());
			} catch(InterruptedException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println();
		}

		System.out.println();
		System.out.println("Latex compilation finished.");

		// Ahora elimino los archivos temporales que creó pdflatex
		Arrays.stream(carpetaPdflatex.listFiles(new TempFilesFilter())).forEach(archivoTemporal -> archivoTemporal.delete());

		// Si se ha indicado borrar tmabién los archivos tex, los borro
		Arrays.stream(carpetaPdflatex.listFiles(new TexFilter())).forEach(archivoTex -> archivoTex.delete());

		System.out.println("Temp files deleted.");
	}

	public static void printFile(NTree tree) {
		try{
			semaphore.acquire();
		} catch(InterruptedException e1){
			return;
		}

		printFile(tree, "ESTRATEGIA NO SETEADA");

		semaphore.release();
	}

	public static void printFile(NTree tree, String strategyName) {
		try{
			semaphore.acquire();
		} catch(InterruptedException e1){
			return;
		}

		StringBuffer str = new StringBuffer();

		// Clase del documento y opciones generales
		str.append("\\documentclass[a0,landscale]{a0poster}" + lineSeparator);

		// Paquetes utilizados
		str.append("\\usepackage{mathptmx}" + lineSeparator);
		str.append("\\usepackage{qtree}" + lineSeparator);
		str.append("\\usepackage{nodo}" + lineSeparator);
		str.append("\\usepackage[spanish]{babel}" + lineSeparator);
		str.append("\\usepackage[utf8]{inputenc}" + lineSeparator);

		str.append("\\title{Árbol de ejecución - Ejecución Nro: " + fileIdx + " - Estrategia: " +
				strategyName + "}" + lineSeparator);
		str.append("\\author{}" + lineSeparator);
		str.append("\\begin{document}" + lineSeparator);
		str.append("\\maketitle" + lineSeparator);

		//StringBuffer sf = new StringBuffer();
		int cuentaArboles = 0;
		//int nivelesProcesados = 0;

		Vector<NTree> nodosAProcesar = new Vector<>();
		nodosAProcesar.add(tree);
		nodosAProcesar.addAll(tree.getSonsTotal());

		for(NTree nodo: nodosAProcesar){
			if(!nodo.getSons().isEmpty()){
				if(cuentaArboles == 0){
					str.append("\\begin{figure}[!h]" + lineSeparator);
				}
				str.append("\\Tree " + nodo.toQtree() + lineSeparator);
				//str.append(tree.getSonsTotal().elementAt(i).toQtree() + "\n");
				cuentaArboles++;

				if(cuentaArboles == 4){
					cuentaArboles = 0;
					str.append("\\end{figure}" + lineSeparator);
				}
			}
		}

		if(cuentaArboles > 0){
			str.append("\\end{figure}" + lineSeparator);
		}
		str.append(lineSeparator);
		//Busqueda.logLatex.debug(sf.toString());
		str.append("\\end{document}" + lineSeparator);

		// Ahora creo el archivo

		// Si la carpeta que necesito no existe, la creo.
		File f = new File(pdflatexDir);
		if(!f.exists()){
			f.mkdir();
		}

		try(Writer out = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(pdflatexDir + fileIdx + ".tex"),
						"UTF-8"))){
			out.write(str.toString());
			fileIdx++;
		} catch(Exception e){
			e.printStackTrace();
		}

		semaphore.release();
	}

}
