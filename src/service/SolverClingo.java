package service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.clingo.desktop.ClingoDesktopService;
import model.Option;
import model.Result;
import resources.Config;

/**
 * Racchiude nelle funzioni i metodi del framework EmbASP per generare Answer
 * Set usuffruendo del solver Clingo
 * 
 *
 */
public class SolverClingo extends Solver {
	private ClingoDesktopService clingoService;

	/**
	 * inizializza le varie classi necessarie al funzionamento di EmbASP
	 * 
	 * @param program
	 *            riceve e inserisce il programma per essere seguito
	 */
	public SolverClingo(ArrayList<String> programs) {
		result = new Result();
		config = new Config();
		clingoService = new ClingoDesktopService(getPath("clingo"));
		handler = new DesktopHandler(clingoService);
		input = new InputProgram();
		this.programs = programs;
		input.setSeparator(" ");
		addPrograms(programs, input);
		handler.addProgram(input);
	}

	@Override
	public boolean checkOptions(ArrayList<Option> options) {
		Pattern regex = Pattern.compile(REGEX_VALIDATION);
		Matcher matcher;
		for (Option option : options) {
			if (!option.getName().equals("free choice") && !option.getName().equals("")) {
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
		StringBuffer path = setPath(solver,OS);
		
		if (OS.indexOf("win") >= 0) {
			String arch = System.getProperty("os.arch");
			if (arch.equals("x86_64")) {
				path.append("clingo64.exe");
			} else {
				path.append("clingo32.exe");
			}

		} else if (OS.indexOf("mac") >= 0) {
			path.append("clingo_macos");

		} else if (OS.indexOf("nux") >= 0) {
			path.append("clingo_linux");
		}
		
		System.out.println(path.toString() + " full path"); // debug string
		return path.toString();
	}

	public ClingoDesktopService getClingoService() {
		return clingoService;
	}

	public void setClingoService(ClingoDesktopService clingoService) {
		this.clingoService = clingoService;
	}

}
