package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;
import model.Option;
import model.Result;

/**
 * Racchiude nelle funzioni i metodi del framework EmbASP per generare Answer
 * Set usuffruendo del solver DLV
 * 
 *
 */
public class SolverDLV extends Solver {
	private DLVDesktopService dlvService;
	private Set<String> optionsDLV;

	/**
	 * inizializza le varie classi necessarie al funzionamento di EmbASP
	 * 
	 * @param program
	 *            riceve e inserisce il programma per essere seguito
	 */
	public SolverDLV(ArrayList<String> programs) {
		this.optionsDLV = new HashSet<String>();
		this.programs = programs;
		addOptions();
		this.dlvService = new DLVDesktopService(getPath("dlv"));
		handler = new DesktopHandler(dlvService);
		result = new Result();
		input = new InputProgram();
		input.setSeparator(" ");
		addPrograms(programs, input);
		handler.addProgram(input);
	}

	/**
	 * Aggiunge le opzioni di DLV, utilizzati successivamente per il controllo
	 */
	private void addOptions() {
		optionsDLV.add("-silent");
		optionsDLV.add("-filter=");
		optionsDLV.add("-nofacts");
		optionsDLV.add("-FC");
	}

	@Override
	public boolean checkOptions(ArrayList<Option> options) {
		Pattern regex = Pattern.compile(REGEX_VALIDATION);
		Matcher matcher;

		for (Option option : options) {
			if (!optionsDLV.contains(option.getName()) && !option.getName().equals("free choice") && !option.getName().equals("")) {
				return false;
			}
			for (String value : option.getValue()) {
				matcher = regex.matcher(value);
				if (!matcher.matches()) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public String getPath(String solver) {
		String OS = System.getProperty("os.name").toLowerCase();
		StringBuffer path = setPath(solver, OS);
	
		if (OS.indexOf("win") >= 0) {
			path.append("dlv.mingw.exe");

		} else if (OS.indexOf("mac") >= 0) {
			path.append("dlv.i386-apple-darwin.bin");

		} else if (OS.indexOf("nux") >= 0) {
			String arch = System.getProperty("os.arch");
			if (arch.equals("x86_64")) {
				path.append("dlv.x86-64-linux-elf-static.bin");

			} else {
				path.append("dlv.i386-linux-elf-static.bin");

			}
		}
		System.out.println(path.toString() + " full path"); // debug string
		
		return path.toString();
	}
	
	public DLVDesktopService getDlvService() {
		return dlvService;
	}

	public void setDlvService(DLVDesktopService dlvService) {
		this.dlvService = dlvService;
	}

	public Set<String> getOptionsDLV() {
		return optionsDLV;
	}

	public void setOptionsDLV(Set<String> optionDLV) {
		this.optionsDLV = optionDLV;
	}
}
