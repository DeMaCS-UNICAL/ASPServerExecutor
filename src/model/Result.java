package model;

import com.google.gson.Gson;

public class Result {
	private String model;
	private Gson gson;
	
	public Result() {
		this.model="";
		this.gson= new Gson();
	}
	
	public String toJson()
	{
		return gson.toJson(this);
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

}
