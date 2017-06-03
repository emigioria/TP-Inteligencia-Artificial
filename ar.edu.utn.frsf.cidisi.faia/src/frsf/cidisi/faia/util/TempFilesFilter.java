package frsf.cidisi.faia.util;

import java.io.File;
import java.io.FilenameFilter;

public class TempFilesFilter implements FilenameFilter {

	@Override
	public boolean accept(File arg0, String arg1) {
		return (arg1.toLowerCase().endsWith(".aux") || arg1.toLowerCase().endsWith(".log"));
	}
}
