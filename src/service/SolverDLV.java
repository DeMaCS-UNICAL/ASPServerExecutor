package service;

import java.io.File;
import java.util.ArrayList;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;
import model.MyCallback;
import model.Option;
import model.Result;
import resources.Config;

public class SolverDLV {
	private String program;
	private DLVDesktopService dlvService;
	private Result result;
	private Handler handler;
	private InputProgram input;
	private Config config;

	public SolverDLV(String program) {
		this.result = new Result();
		this.config = new Config();
		this.dlvService = new DLVDesktopService(getPath());
		this.handler = new DesktopHandler(dlvService);
		this.input = new InputProgram();
		this.program = program;
		input.setSeparator(" ");
		input.addProgram(program);
		handler.addProgram(input);
	}

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
		result.setModel(output.getOutput());
		return result.toJson();

	}

	public void solveAsync(ArrayList<Option> options,MyCallback callback) {
		OptionDescriptor opDescriptor = new OptionDescriptor();
		opDescriptor.setSeparator(" ");
		for (int i = 0; i < options.size(); i++) {
			Option optiontmp = options.get(i);
			opDescriptor.addOption(optiontmp.getToASP());
			handler.addOption(opDescriptor);
		}
		opDescriptor.addOption(" ");
		handler.startAsync(callback);
	}


	public String getPath() {
		String OS = System.getProperty("os.name").toLowerCase();
		StringBuffer path = new StringBuffer();
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
				path.append("lib/dlv.x86-64-linux-elf-static.bin");

			} else {
				path.append("lib/dlv.i386-apple-darwin.bin");

			}
		}
		return path.toString();

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

}
