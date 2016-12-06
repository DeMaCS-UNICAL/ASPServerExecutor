package model;

import java.util.ArrayList;

/**
 * Memoriazza e gestisce le opzioni in input
 *
 */
public class Option {
	private String name;
	private ArrayList<String> value;

	public Option() {
		this.name = "";
		this.value = new ArrayList<>();
	}

	/**
	 * Costruisce le opzioni separando gli eventuali valori tra " , ", in quanto
	 * il metodo addOption non permette lo spazio tra l'aggiunta di un opzione e
	 * l'altra
	 * 
	 * @return string
	 */
	public String getToASP() {
		StringBuffer s = new StringBuffer();
		if (!name.equals("free choice")) {
			s.append(name);
			for (int i = 0; i < value.size(); i++) {
				if (i != 0) {
					s.append(",");
				}
				s.append(value.get(i));

			}
		} else {
			for (int i = 0; i < value.size() - 1; i++) {
				s.append(value.get(i));
				s.append(" ");
			}
			s.append(value.get(value.size() - 1));
		}

		return s.toString();
	}

	public String getToClingo() {
		StringBuffer s = new StringBuffer();
		if (!name.equals("")) {
			for (int i = 0; i < value.size() - 1; i++) {
				s.append(value.get(i));
				s.append(" ");
			}
			s.append(value.get(value.size() - 1));
		} else {
			s.append(name);
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
