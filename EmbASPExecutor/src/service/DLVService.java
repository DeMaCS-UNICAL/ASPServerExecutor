package service;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
		this.program=program;
		this.setResult(new Result());
		this.dlvService=new DLVDesktopService("WebContent/WEB-INF/lib/dlv.i386-apple-darwin.bin");
		this.gson = new Gson();
	}
	
	public String solve() {
		InputProgram input = new InputProgram();
		input.setSeparator("");
		input.addProgram(program);
		ArrayList<InputProgram> inputProgram= new ArrayList<InputProgram>();
		ArrayList<OptionDescriptor> option= new ArrayList<OptionDescriptor>();
		inputProgram.add(input);
		Output o= dlvService.startSync(inputProgram, option);
		result.setModal(o.getOutput());
		return gson.toJson(result);
	
		
	}
	public String solveWithOption(ArrayList<Option> options) {
		InputProgram input = new InputProgram();
		input.setSeparator("");
		input.addProgram(program);
		ArrayList<InputProgram> inputProgram= new ArrayList<InputProgram>();
		ArrayList<OptionDescriptor> optiondInput= new ArrayList<OptionDescriptor>();
		if (options.size() == 1)
		{
			Option optiontmp= options.get(0);
			if (optiontmp.getName().equals("filter") && optiontmp.getValue().size()==1)
			{
				DLVFilterOption filter = new DLVFilterOption(optiontmp.getValue().get(0)+" ");
				filter.setSeparator("");
				optiondInput.add(filter);
				Output o= dlvService.startSync(inputProgram, optiondInput);
				result.setModal(o.getOutput());
				return gson.toJson(result);
			}else if (optiontmp.getName().equals("filter") && optiontmp.getValue().size()>1){

				DLVFilterOption filter = new DLVFilterOption(optiontmp.getValue().get(0));
				filter.setSeparator(",");
				for (int i = 1; i < optiontmp.getValue().size()-1; i++) {
					filter.addOption(optiontmp.getValue().get(i));
				}
				filter.addOption(optiontmp.getValue().get(optiontmp.getValue().size()-1)+" ");
				optiondInput.add(filter);
				Output o= dlvService.startSync(inputProgram, optiondInput);
				result.setModal(o.getOutput());
				return gson.toJson(result);
				
			}else if (!optiontmp.getName().equals("filter") && optiontmp.getValue().size()==1){
				OptionDescriptor od = new OptionDescriptor("-"+optiontmp.getName()+"="+optiontmp.getValue()+" ");
				od.setSeparator("");
				optiondInput.add(od);
				Output o= dlvService.startSync(inputProgram, optiondInput);
				result.setModal(o.getOutput());
				return gson.toJson(result);
				
			}else if (!optiontmp.getName().equals("filter") && optiontmp.getValue().size()>1){
				OptionDescriptor od = new OptionDescriptor("-"+optiontmp.getName()+"="+optiontmp.getValue().get(0));
				od.setSeparator(",");
				for (int i = 1; i < optiontmp.getValue().size()-1; i++) {
					od.addOption(optiontmp.getValue().get(0));
				}
				od.addOption(optiontmp.getValue().get(optiontmp.getValue().size()-1)+" ");
				optiondInput.add(od);
				Output o= dlvService.startSync(inputProgram, optiondInput);
				result.setModal(o.getOutput());
				return gson.toJson(result);
			}
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
		DLVService d = new DLVService("prova(1).");
		String prova = null;
		prova=d.solve();
		System.out.println(prova);
	}
	

	

}
