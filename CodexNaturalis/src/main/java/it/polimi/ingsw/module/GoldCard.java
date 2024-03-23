package it.polimi.ingsw.module;

import java.util.Map;

public class GoldCard extends ResourceCard {

	private int n_reqElement;

	private Elements secondElement;

	private TypeBonus type;

	public void Gold_Card(int n, Elements s, TypeBonus type, Elements cElement) {

	}

	public void Gold_Card(int n, Elements s, TypeBonus type) {

	}

	public boolean checkRequirements(Map input) {
		return false;
	}

}
