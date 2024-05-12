package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InitialCard extends Card implements Serializable {
	private ArrayList<Elements> centralElements;

	public Map<CornerPosition, Elements> getCornersFront() {
		return cornersFront;
	}

	public void setCornersFront(Map<CornerPosition, Elements> cornersFront) {
		this.cornersFront = cornersFront;
	}

	public Map<CornerPosition, Elements> getCornersBack() {
		return cornersBack;
	}

	public void setCornersBack(Map<CornerPosition, Elements> cornersBack) {
		this.cornersBack = cornersBack;
	}

	private Map<CornerPosition, Elements> cornersFront;
	private Map<CornerPosition,Elements> cornersBack;
	public InitialCard(){
		super();
	}

	public InitialCard(int code, boolean isFront) {
		super(code, isFront,null);
	}

	public InitialCard(int code, boolean isFront, Map<CornerPosition, Elements> corners, ArrayList<Elements> elem) {
		super(code, isFront,corners);
		this.centralElements=elem;

	}
	public InitialCard(int code, boolean isFront, Map<CornerPosition, Elements> corners, ArrayList<Elements> elem,Map<CornerPosition, Elements> cornersFront,Map<CornerPosition, Elements> cornersBack) {
		super(code, isFront,corners);
		this.centralElements=elem;
		this.cornersFront=cornersFront;
		this.cornersBack=cornersBack;

	}
	public void setSide(){
		if (super.getIsFront()){
			corners = cornersFront;

		}else{
			corners=cornersBack;
		}
	}


	public ArrayList<Elements> getCentralElements() {
		return centralElements;
	}

	public void setCentralElements(ArrayList<Elements> centralElements) {
		this.centralElements = centralElements;
	}





}
