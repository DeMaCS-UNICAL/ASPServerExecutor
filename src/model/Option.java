package model;

import java.util.ArrayList;

public class Option {
	private String name;
	private ArrayList<String> value;

	public Option() {
		this.name = "";
		this.value = new ArrayList<>();
	}

	public String getToASP() {
		StringBuffer s = new StringBuffer();
		s.append(name);
		for (int i = 0; i < value.size(); i++) {
			if (i != 0) {
				s.append(",");
			}
			s.append(value.get(i));

		}

		return s.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getValue() {
		return value;
	}

	public void setValue(ArrayList<String> value) {
		this.value = value;
	}

}
