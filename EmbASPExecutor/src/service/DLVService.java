package service;

import java.util.ArrayList;

import com.google.gson.Gson;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.specializations.dlv.DLVFilterOption;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;
import model.Option;
import model.Result;

public class DLVService {
	private String program;
	private DLVDesktopService dlvService;
	private Gson gson;
	private Result result;

	public DLVService(String program) {
		this.program = program;
		this.setResult(new Result());
		this.dlvService = new DLVDesktopService("WebContent/WEB-INF/lib/dlv.i386-apple-darwin.bin");
		this.gson = new Gson();
	}

	public String solve() {
		InputProgram input = new InputProgram();
		input.setSeparator(" ");
		input.addProgram(program);
		ArrayList<InputProgram> inputProgram = new ArrayList<InputProgram>();
		ArrayList<OptionDescriptor> option = new ArrayList<OptionDescriptor>();
		inputProgram.add(input);
		Output o = dlvService.startSync(inputProgram, option);
		result.setModal(o.getOutput());
		return gson.toJson(result);

	}

	public String solveWithOption(ArrayList<Option> options) {
		InputProgram input = new InputProgram();
		input.setSeparator("");
		input.addProgram(program);
		ArrayList<InputProgram> inputProgram = new ArrayList<InputProgram>();
		inputProgram.add(input);
		ArrayList<OptionDescriptor> optiondInput = new ArrayList<OptionDescriptor>();
		if (options.size() == 1) {
			Option optiontmp = options.get(0);
			if (optiontmp.getName().equals("filter") && optiontmp.getValue().size() == 1) {
				DLVFilterOption filter = new DLVFilterOption(optiontmp.getValue().get(0) + " ");
				filter.setSeparator("");
				optiondInput.add(filter);
				System.out.println(filter.getOptions());
				Output o = dlvService.startSync(inputProgram, optiondInput);
				result.setModal(o.getOutput());
				return gson.toJson(result);
			} else if (optiontmp.getName().equals("filter") && optiontmp.getValue().size() > 1) {

				DLVFilterOption filter = new DLVFilterOption(optiontmp.getValue().get(0));
				filter.setSeparator(",");
				for (int i = 1; i < optiontmp.getValue().size() - 1; i++) {
					filter.addOption(optiontmp.getValue().get(i));
				}
				filter.addOption(optiontmp.getValue().get(optiontmp.getValue().size() - 1) + " ");
				optiondInput.add(filter);
				System.out.println(filter.getOptions());
				Output o = dlvService.startSync(inputProgram, optiondInput);
				result.setModal(o.getOutput());
				return gson.toJson(result);

			} else if (!optiontmp.getName().equals("filter") && optiontmp.getValue().size() == 1) {
				OptionDescriptor od = null;
				if (!optiontmp.getValue().get(0).equals("")) {
					od = new OptionDescriptor("-" + optiontmp.getName() + "=" + optiontmp.getValue() + " ");
				} else {
					od = new OptionDescriptor("-" + optiontmp.getName()+" ");

				}
				od.setSeparator("");
				optiondInput.add(od);
				System.out.println(od.getOptions());
				Output o = dlvService.startSync(inputProgram, optiondInput);
				result.setModal(o.getOutput());
				return gson.toJson(result);

			} else if ((!optiontmp.getName().equals("filter") && optiontmp.getValue().size() > 1)) {				
		
				 OptionDescriptor od = new OptionDescriptor("-" + optiontmp.getName() + "=" + optiontmp.getValue().get(0));
					od.setSeparator(",");
						
				for (int i = 1; i < optiontmp.getValue().size() - 1; i++) {
					od.addOption(optiontmp.getValue().get(i));
				}
				od.addOption(optiontmp.getValue().get(optiontmp.getValue().size() - 1) + " ");
				optiondInput.add(od);
				System.out.println(od.getOptions());
				Output o = dlvService.startSync(inputProgram, optiondInput);
				result.setModal(o.getOutput());
				return gson.toJson(result);
			}
		} else {
			for (int i = 0; i < options.size(); i++) {
				Option oplist = options.get(i);
				DLVFilterOption filter = null;
				if (oplist.getName().equals("filter") && oplist.getValue().size() == 1) {
					filter = new DLVFilterOption(oplist.getValue().get(0) + " ");
					filter.setSeparator("");
					System.out.println(filter.getOptions());
					optiondInput.add(filter);

				} else if (oplist.getName().equals("filter") && oplist.getValue().size() > 1) {

					filter = new DLVFilterOption(oplist.getValue().get(0));
					filter.setSeparator(",");
					for (int k = 1; k < oplist.getValue().size() - 1; k++) {
						filter.addOption(oplist.getValue().get(k));
					}

					filter.addOption(oplist.getValue().get(oplist.getValue().size() - 1) + " ");
					System.out.println(filter.getOptions());
					optiondInput.add(filter);

				} else if (!oplist.getName().equals("filter") && oplist.getValue().size() == 1) {
					OptionDescriptor od = null;
					if (!oplist.getValue().get(0).equals("")) {
						od = new OptionDescriptor("-" + oplist.getName() + "=" + oplist.getValue().get(0) + " ");
					} else {
						od = new OptionDescriptor("-" + oplist.getName()+" ");

					}
					od.setSeparator(" ");
					System.out.println(od.getOptions());
					optiondInput.add(od);

				} else if (!oplist.getName().equals("filter") && oplist.getValue().size() > 1) {
					OptionDescriptor od = new OptionDescriptor("-" + oplist.getName() + "=" + oplist.getValue().get(0));
					od.setSeparator(",");
					for (int k = 1; k < oplist.getValue().size() - 1; k++) {
						od.addOption("-" + oplist.getName() + "=" + oplist.getValue().get(k));
					}
					od.addOption(oplist.getValue().get(oplist.getValue().size() - 1) + " ");
					System.out.println(od.getOptions());
					optiondInput.add(od);

				}

			}

			Output o = dlvService.startSync(inputProgram, optiondInput);
			result.setModal(o.getOutput());
			return gson.toJson(result);

		}
		return null;
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

	public static void main(String[] args) {
		DLVService d = new DLVService("prova(1). text(X):-prova(X). executor(X):-prova(X).");
		String prova = null;
		Option o = new Option();
		Option o1= new Option();
		ArrayList<String> value1 = new ArrayList<>();
		o1.setName("filter");
		value1.add("text");
		o1.setValue(value1);
		ArrayList<String> value = new ArrayList<>();
		ArrayList<Option> op = new ArrayList<>();
		value.add("");
		o.setName("nofacts");
		o.setValue(value);
		op.add(o);
		op.add(o1);
		prova = d.solveWithOption(op);
		System.out.println(prova);
	}

}
