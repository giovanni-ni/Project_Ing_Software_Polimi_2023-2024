package it.polimi.ingsw.model;

public class InitialCard extends Card {
	private Elements[] centralElements;

	public InitialCard(){
		super();
	}

	public InitialCard(int code, boolean isFront) {
		super(code, isFront);
	}

	public InitialCard(int code, boolean isFront, Elements[] elem) {
		super(code, isFront);
		for(int i = 0; i < elem.length; i++) {
			centralElements[i] = elem[i];
		}

	}

	public Elements[] getCentralElements() {
		return centralElements;
	}

	public void setCentralElement(Elements[] elements) {
		for(int i = 0; i < elements.length; i++) {
			centralElements[i] = elements[i];
		}
	}


	public Elements getCentralElement(int index) {
		return centralElements[index];
	}

	public void setCentralElement(int index, Elements elem) {
		centralElements[index] = elem;
	}



}
