package service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;
import model.Option;
import model.Result;
import resources.Config;

public class SolverDLV2 extends Solver {
	
	private DLV2DesktopService dlv2Service;
	
	/**
	 * inizializza le varie classi necessarie al funzionamento di EmbASP
	 * 
	 * @param program
	 *            riceve e inserisce il programma per essere seguito
	 */
	public SolverDLV2(ArrayList<String> programs) {
		this.dlv2Service = new DLV2DesktopService(getPath("dlv2"));
		handler = new DesktopHandler(dlv2Service);
		result = new Result();
		config = new Config();
		this.programs = programs;
		input = new InputProgram();
		input.setSeparator(" ");
		addPrograms(programs, input);
		handler.addProgram(input);
	}

	@Override
	public boolean checkOptions(ArrayList<Option> options) {
		Pattern regex = Pattern.compile("[A-Za-z0-9=_-]*");
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
	
		path.append("dlv2");
		
		System.out.println(path.toString() + " full path"); // debug string
		return path.toString();
	}
	
	public DLV2DesktopService getDlv2Service() {
		return dlv2Service;
	}

	public void setDlv2Service(DLV2DesktopService dlv2Service) {
		this.dlv2Service = dlv2Service;
	}

}
