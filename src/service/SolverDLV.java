package service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;
import model.Option;
import model.Result;

public class SolverDLV {
	private String program;
	private DLVDesktopService dlvService;
	private Result result;
	private Handler handler;
	private InputProgram input;

	public SolverDLV(String program) {
		this.result = new Result();
		this.dlvService = checkOS();
		this.handler = new DesktopHandler(dlvService);
		this.input = new InputProgram();
		this.program = program;
		input.setSeparator(" ");
		input.addProgram(program);
		handler.addProgram(input);
	}

	public String solve() {
		handler.addProgram(input);
		Output output = handler.startSync();
		result.setModel(output.getOutput());
		return result.toJson();

	}

	public String solveWithOption(ArrayList<Option> options) {

		for (int i = 0; i < options.size(); i++) {
			Option optiontmp = options.get(i);
			OptionDescriptor opDescriptor = null;
			if (!optiontmp.getName().equals("option")) {
				if (optiontmp.getValue().size() == 1 && !optiontmp.getValue().get(0).equals("")) {
					opDescriptor = new OptionDescriptor("-" + optiontmp.getName() + "=");
					opDescriptor.setSeparator("");
					opDescriptor.addOption(optiontmp.getValue().get(0) + " ");
					handler.addOption(opDescriptor);

				} else if (optiontmp.getValue().size() > 1) {
					opDescriptor = new OptionDescriptor("-" + optiontmp.getName() + "=");
					opDescriptor.setSeparator(",");
					for (int k = 0; k < optiontmp.getValue().size() - 1; k++) {
						opDescriptor.addOption(optiontmp.getValue().get(k));
					}
					opDescriptor.addOption(optiontmp.getValue().get(optiontmp.getValue().size() - 1) + " ");
					handler.addOption(opDescriptor);

				} else {
					opDescriptor = new OptionDescriptor("-" + optiontmp.getName() + " ");
					opDescriptor.setSeparator("");
					handler.addOption(opDescriptor);

				}

			}
		}

		Output output = handler.startSync();
		result.setModel(output.getOutput());
		return result.toJson();

	}

	public DLVDesktopService checkOS() {
		String OS = System.getProperty("os.name").toLowerCase();
		Path path = null;

		if (OS.indexOf("win") >= 0) {
			path = Paths.get("lib/dlv.mingw.exe");
			return new DLVDesktopService(path.toAbsolutePath().toString());
		} else if (OS.indexOf("mac") >= 0) {
			path = Paths.get("lib/dlv.i386-apple-darwin.bin");
			return new DLVDesktopService(path.toAbsolutePath().toString());
		} else if (OS.indexOf("nux") >= 0) {
			String arch = System.getProperty("os.arch");
			if (arch.equals("x86_64")) {
				path = Paths.get("lib/dlv.x86-64-linux-elf-static.bin");
				return new DLVDesktopService(path.toAbsolutePath().toString());
			} else {
				path = Paths.get("lib/dlv.i386-apple-darwin.bin");
				return new DLVDesktopService(path.toAbsolutePath().toString());
			}
		}
		return null;

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

}
