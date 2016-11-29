package service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import it.unical.mat.embasp.asp.AnswerSets;
import it.unical.mat.embasp.base.Callback;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;
import model.Option;
import model.Result;
import resources.Config;

/**
 * Racchiude nelle funzioni i metodi del framework EmbASP per generare Answer
 * Set usuffruendo del solver DLV
 * 
 *
 */
public class SolverDLV {
	private String program;
	private DLVDesktopService dlvService;
	private Result result;
	private Handler handler;
	private InputProgram input;
	private Config config;
	private Set<String> optionsDLV;

	/**
	 * inizializza le varie classi necessarie al funzionamento di EmbASP
	 * 
	 * @param program
	 *            riceve e inserisce il programma per essere seguito
	 */
	public SolverDLV(String program) {
		this.result = new Result();
		this.config = new Config();
		this.optionsDLV = new HashSet<String>();
		addOptions();
		this.dlvService = new DLVDesktopService(getPath());
		this.handler = new DesktopHandler(dlvService);
		this.input = new InputProgram();
		this.program = program;
		input.setSeparator(" ");
		input.addProgram(program);
		handler.addProgram(input);
	}

	/**
	 * Aggiunge le opzioni al programma ed esegue il solver in maniera sincrona
	 * 
	 * @param options
	 *            Riceve le opzioni da settare
	 * @return string Restuisce in formato json, gli Answer Set nell'oggetto
	 *         Result
	 */
	public String solveSync(ArrayList<Option> options) {
		OptionDescriptor opDescriptor = new OptionDescriptor();
		opDescriptor.setSeparator(" ");
		for (int i = 0; i < options.size(); i++) {
			Option optiontmp = options.get(i);
			opDescriptor.addOption(optiontmp.getToASP());
			handler.addOption(opDescriptor);

		}
		opDescriptor.addOption(" ");
		Output output = handler.startSync();
		AnswerSets answerSets = (AnswerSets) output;
		if (answerSets.getAnswersets().isEmpty()) {
			result.setError("Sorry, there aren't answer sets for this program");
		} else {
			result.setModel(answerSets.getAnswerSetsString());

		}
		return result.toJson();

	}

	/**
	 * Aggiunge le opzioni al programma e esegue il solver in maniera asincrona
	 * 
	 * @param options
	 *            Riceve le opzioni da settare
	 * @param call
	 *            Classe necessaria per eseguire il metodo in maniera asincrona
	 */
	public void solveAsync(ArrayList<Option> options, Callback call) {
		OptionDescriptor opDescriptor = new OptionDescriptor();
		opDescriptor.setSeparator(" ");
		for (int i = 0; i < options.size(); i++) {
			Option optiontmp = options.get(i);
			opDescriptor.addOption(optiontmp.getToASP());
			handler.addOption(opDescriptor);
		}
		opDescriptor.addOption(" ");
		System.out.println(opDescriptor.getOptions());
		handler.startAsync(call);
	}

	/**
	 * Restiuice il path assoluto del solver dalla classe di configurazione e
	 * sceglie il solver adeguato al proprio sistema oprativo
	 * 
	 * @return String Concatena il path assoluto e il tipo di solver in base al
	 *         sistema operativo ed eventualmente anche all'architettura
	 */
	public String getPath() {
		String OS = System.getProperty("os.name").toLowerCase();
		StringBuffer path = new StringBuffer();
		
		if (OS.indexOf("nux") >= 0){
			path.append(config.getAbsolutePathTimeout());
			path.append(File.separator);
			path.append("./timeout ");
			path.append("-t ");
			path.append(config.getTimeMax());
			path.append(" -m ");
			path.append(config.getMemMax());
			path.append(" ");
		}
		path.append(config.getAbsolutePath());
		path.append(File.separator);
		path.append("dlv");
		path.append(File.separator);

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

	/**
	 * Aggiunge le opzioni di DLV, utilizzati successivamente per il controllo
	 */
	public void addOptions() {
		optionsDLV.add("-silent");
		optionsDLV.add("-filter=");
		optionsDLV.add("-nofacts");
	}

	/**
	 * Controlla che il formato delle opzioni sia corretto, utilizzando la regex
	 * per il valore e verificando se il nome delle opzioni esiste tra le quelle
	 * di DLV
	 * 
	 * @param options
	 *            Riceve le opzioni da controllare
	 * @return boolean Se le opzioni risultano corrette ritorna true altrimenti
	 *         false
	 */
	public boolean checkOptionsDLV(ArrayList<Option> options) {
		Pattern regex = Pattern.compile("[A-Za-z0-9=_-]*");
		Matcher matcher;

		for (Option option : options) {
			if (!optionsDLV.contains(option.getName()) && !option.getName().equals("option")) {
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

	public InputProgram getInput() {
		return input;
	}

	public void setInput(InputProgram input) {
		this.input = input;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public DLVDesktopService getDlvService() {
		return dlvService;
	}

	public void setDlvService(DLVDesktopService dlvService) {
		this.dlvService = dlvService;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Set<String> getOptionsDLV() {
		return optionsDLV;
	}

	public void setOptionsDLV(Set<String> optionDLV) {
		this.optionsDLV = optionDLV;
	}

}
