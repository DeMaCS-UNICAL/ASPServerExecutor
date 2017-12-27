package service;

import java.io.File;
import java.util.ArrayList;

import it.unical.mat.embasp.base.Callback;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;
import model.Option;
import model.Result;
import resources.Config;

abstract class Solver {
	protected ArrayList<String> programs;
	protected Result result;
	protected Handler handler;
	protected InputProgram input;
	protected Config config;
	DLVDesktopService dlvService;

	
	public Solver() {
		config = new Config();
	}
	/**
	 * Restiuice il path assoluto del solver dalla classe di configurazione e
	 * sceglie il solver adeguato al proprio sistema oprativo
	 * 
	 * @return String Concatena il path assoluto e il tipo di solver in base al
	 *         sistema operativo ed eventualmente anche all'architettura
	 */
	public StringBuffer setPath(String solver, String OS) {
		StringBuffer path = new StringBuffer();
		
		if (OS.indexOf("nux") >= 0){
			path.append(config.getProperty("absolute_path_timeout"));
			path.append(File.separator);
			path.append("./timeout ");
			path.append("-t ");
			path.append(config.getProperty("timeMax"));
			path.append(" -m ");
			path.append(config.getProperty("memMax"));
			path.append(" ");
		}
		path.append(config.getProperty("absolute_path"));
		path.append(File.separator);
		path.append(solver);
		path.append(File.separator);
		
		return path;
	}
	
	public abstract String getPath(String solver);
	
	/**
	 * Aggiunge le opzioni al programma ed esegue il solver in maniera sincrona
	 * 
	 * @param options
	 *            Riceve le opzioni da settare
	 * @return string Restuisce in formato json, gli Answer Set nell'oggetto
	 *         Result
	 */
	public String solveSync(ArrayList<Option> options, String solverOption) {
		OptionDescriptor opDescriptor = new OptionDescriptor();
		opDescriptor.setSeparator(" ");
		for (int i = 0; i < options.size(); i++) {
			Option optiontmp = options.get(i);
			if (!optiontmp.getName().equals("")) {
				switch (solverOption) {
				case "dlv":
					opDescriptor.addOption(optiontmp.getToASP());
					break;
				case "clingo":
					opDescriptor.addOption(optiontmp.getToClingo());
					break;
				default:
					break;
				}
				handler.addOption(opDescriptor);
			}

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
	public void solveAsync(ArrayList<Option> options, Callback call, String solverOption) {
		OptionDescriptor opDescriptor = new OptionDescriptor();
		opDescriptor.setSeparator(" ");
		for (int i = 0; i < options.size(); i++) {
			Option optiontmp = options.get(i);
			if (!optiontmp.getName().equals("")){
				opDescriptor.addOption(solverOption);
				handler.addOption(opDescriptor);
			}
		}
		System.out.println(opDescriptor.getOptions()); // debug string
		opDescriptor.addOption(" ");
		handler.startAsync(call);
	}
	
	/**
	 * Controlla che il formato delle opzioni sia corretto, utilizzando la regex
	 * per il valore e verificando se il nome delle opzioni esiste tra le quelle
	 * del Solver
	 * 
	 * @param options
	 *            Riceve le opzioni da controllare
	 * @return boolean Se le opzioni risultano corrette ritorna true altrimenti
	 *         false
	 */
	public abstract boolean checkOptions(ArrayList<Option> options);
	
	public void addPrograms(ArrayList<String> programs,InputProgram input){
		for (String p : programs) {
			input.addProgram(p);
		}
	}
	
	public ArrayList<String> getPrograms() {
		return programs;
	}
	
	public void setPrograms(ArrayList<String> programs) {
		this.programs = programs;
	}
	
	public Result getResult() {
		return result;
	}
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	public InputProgram getInput() {
		return input;
	}
	
	public void setInput(InputProgram input) {
		this.input = input;
	}
	
	public Config getConfig() {
		return config;
	}
	
	public void setConfig(Config config) {
		this.config = config;
	}
}
