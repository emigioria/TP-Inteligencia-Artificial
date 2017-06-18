package frsf.cidisi.faia.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import frsf.cidisi.faia.solver.search.NTree;

public class TreeMLWriter {

	private static int fileIdx = 0;
	private static final String searchTreesDir = "TreeML/";

	public static void printFile(NTree node) {

		File f = new File(searchTreesDir);
		if(!f.exists()){
			f.mkdir();
		}

		try(FileOutputStream os = new FileOutputStream(new File(searchTreesDir + fileIdx
				+ ".xml"))){

			PrintWriter out = new PrintWriter(os);
			XMLWriter xml = new XMLWriter(out);

			xml.startTree(new Integer(fileIdx).toString());
			fileIdx = fileIdx + 1;

			//			NTree node = (NTree) graph.clone();

			while(node.getParent() != null){
				node = node.getParent();
			}

			List<NTree> nodos = node.getSonsTotal();
			nodos.add(node);

			nodos.forEach(nodo -> escribirNodo(nodo, xml));
			nodos.forEach(nodo -> escribirEnlaces(nodo, xml));

			xml.end();
			xml.finish();
			out.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	private static void escribirNodo(NTree tree, XMLWriter xml) {
		String tag = Constante.NODE.toString();
		ArrayList<String> names = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();

		names.add(Constante.ID.toString());
		names.add(Constante.ACTION.toString());
		names.add(Constante.COST.toString());
		names.add(Constante.AGENT_STATE.toString());

		Integer i = new Integer(tree.getExecutionOrder());
		values.add(i.toString());
		if(tree.getAction() == null){
			values.add("null");
		}
		else{
			values.add(tree.getAction().toString());
		}

		values.add(new Double(tree.getCost()).toString());
		values.add(tree.getAgentState().toString());

		xml.tag(tag, names, values, 4);

	}

	private static void escribirEnlaces(NTree tree, XMLWriter xml) {
		String tag = Constante.EDGE.toString();
		ArrayList<String> names = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();

		//tree = tree.clone();
		Vector<NTree> ts = tree.getSons();

		ts.forEach(son -> {
			names.add(Constante.SOURCE.toString());
			names.add(Constante.TARGET.toString());

			values.add(new Integer(tree.getExecutionOrder()).toString());
			values.add(new Integer(son.getExecutionOrder()).toString());

			xml.tag(tag, names, values, names.size());
			names.clear();
			values.clear();
		});
	}
}
