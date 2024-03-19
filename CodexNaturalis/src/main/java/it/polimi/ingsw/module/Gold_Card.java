package it.polimi.ingsw.module;

public class Gold_Card extends Resource_Card {

	private int n_reqElement;

	private Elements secondElement;

	private Type_Bonus type;

	public void Gold_Card(int n, Elements s, Type_Bonus type, Elements cElement) {

	}

	public void Gold_Card(int n, Elements s, Type_Bonus type) {

	}

	public boolean checkRequirements(Map<?, ?> input) {
		return false;
	}

}
