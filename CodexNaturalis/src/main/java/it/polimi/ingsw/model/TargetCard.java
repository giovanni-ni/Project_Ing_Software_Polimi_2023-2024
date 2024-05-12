package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class TargetCard implements Serializable {

	private int idCard;

	private int basePoint;

	private boolean ifCommon;

	public TargetCard() {

	}

	public TargetCard(int idCard, int basePoint, boolean ifCommon) {
		this.idCard = idCard;
		this.basePoint = basePoint;
		this.ifCommon = ifCommon;
	}

	public int getbasePoint() {
		return this.basePoint;
	}

	public void setbasePoint(int p) {
		this.basePoint = p;
	}

	public int getIdCard() {
		return idCard;
	}
	public void setIdCard(int idCard) {
		this.idCard = idCard;
	}

	public boolean isIfCommon() {
		return ifCommon;
	}

	public void setIfCommon(boolean ifCommon) {
		this.ifCommon = ifCommon;
	}

	/*public int checkTarget(Board input) {
		/***********maybe controller part****************/
		/*int n = 0;

		switch(this.idCard) {
			case 1:
				n = input.getCounterOfElements().get(Elements.INK) / 2;
				break;

			case 2:
				n = input.getCounterOfElements().get(Elements.FEATHER) / 2;
				break;

			case 3:
				n = input.getCounterOfElements().get(Elements.PARCHMENT) / 2;
				break;

			case 4:
				n = minNumElement(input.getCounterOfElements().get(Elements.PARCHMENT),input.getCounterOfElements().get(Elements.INK),input.getCounterOfElements().get(Elements.FEATHER));
				break;

			case 5:
				ArrayList<Integer> copy = input.getExists();
				for(int i = 1; i <= 10; i++) {
					if(copy.contains(i)) {
						Coordinate xy = input.getCoordinate(i);
						int x = xy.getX();
						int y = xy.getY();

						if(input.isCardCoordinate(x+1, y-1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x+1, y-1);
							int next = c1.getCode();
							if(c1.getKingdom() == Elements.MUSHROOMS) {
								if(input.isCardCoordinate(x+2, y-2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 2, y - 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.MUSHROOMS) {
										n++;
										copy.remove(i);
										copy.remove(next);
										copy.remove(next_next);
									}
								}
							}
						}
					}
				}

				for(int j = 41; j <= 50; j++) {
					if (copy.contains(j)) {
						Coordinate xy = input.getCoordinate(j);
						int x = xy.getX();
						int y = xy.getY();

						if (input.isCardCoordinate(x + 1, y - 1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x + 1, y - 1);
							int next = c1.getCode();
							if (c1.getKingdom() == Elements.MUSHROOMS) {
								if (input.isCardCoordinate(x + 2, y - 2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 2, y - 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.MUSHROOMS) {
										n++;
										copy.remove(j);
										copy.remove(next);
										copy.remove(next_next);
									}
								}
							}
						}
					}
				}
				break;

			case 6:
				ArrayList<Integer> copy6 = input.getExists();
				for(int i = 31; i <= 40; i++) {
					if(copy6.contains(i)) {
						Coordinate xy = input.getCoordinate(i);
						int x = xy.getX();
						int y = xy.getY();

						if(input.isCardCoordinate(x+1, y+1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x+1, y+1);
							int next = c1.getCode();
							if(c1.getKingdom() == Elements.INSECT) {
								if(input.isCardCoordinate(x+2, y+2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 2, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.INSECT) {
										n++;
										copy6.remove(i);
										copy6.remove(next);
										copy6.remove(next_next);
									}
								}
							}
						}
					}
				}

				for(int j = 71; j <= 80; j++) {
					if (copy6.contains(j)) {
						Coordinate xy = input.getCoordinate(j);
						int x = xy.getX();
						int y = xy.getY();

						if (input.isCardCoordinate(x + 1, y + 1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x + 1, y + 1);
							int next = c1.getCode();
							if (c1.getKingdom() == Elements.INSECT) {
								if (input.isCardCoordinate(x + 2, y + 2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 2, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.INSECT) {
										n++;
										copy6.remove(j);
										copy6.remove(next);
										copy6.remove(next_next);
									}
								}
							}
						}
					}
				}

				break;


			case 7:
				ArrayList<Integer> copy7 = input.getExists();
				for(int i = 21; i <= 30; i++) {
					if(copy7.contains(i)) {
						Coordinate xy = input.getCoordinate(i);
						int x = xy.getX();
						int y = xy.getY();

						if(input.isCardCoordinate(x+1, y-1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x+1, y-1);
							int next = c1.getCode();
							if(c1.getKingdom() == Elements.ANIMALS) {
								if(input.isCardCoordinate(x+2, y-2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 2, y - 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.ANIMALS) {
										n++;
										copy7.remove(i);
										copy7.remove(next);
										copy7.remove(next_next);
									}
								}
							}
						}
					}
				}

				for(int j = 61; j <= 70; j++) {
					if (copy7.contains(j)) {
						Coordinate xy = input.getCoordinate(j);
						int x = xy.getX();
						int y = xy.getY();

						if (input.isCardCoordinate(x + 1, y - 1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x + 1, y - 1);
							int next = c1.getCode();
							if (c1.getKingdom() == Elements.ANIMALS) {
								if (input.isCardCoordinate(x + 2, y - 2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 2, y - 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.ANIMALS) {
										n++;
										copy7.remove(j);
										copy7.remove(next);
										copy7.remove(next_next);
									}
								}
							}
						}
					}
				}
				break;

			case 8:
				ArrayList<Integer> copy8 = input.getExists();
				for(int i = 11; i <= 20; i++) {
					if(copy8.contains(i)) {
						Coordinate xy = input.getCoordinate(i);
						int x = xy.getX();
						int y = xy.getY();

						if(input.isCardCoordinate(x+1, y+1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x+1, y+1);
							int next = c1.getCode();
							if(c1.getKingdom() == Elements.VEGETAL) {
								if(input.isCardCoordinate(x+2, y+2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 2, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.VEGETAL) {
										n++;
										copy8.remove(i);
										copy8.remove(next);
										copy8.remove(next_next);
									}
								}
							}
						}
					}
				}

				for(int j = 51; j <= 60; j++) {
					if (copy8.contains(j)) {
						Coordinate xy = input.getCoordinate(j);
						int x = xy.getX();
						int y = xy.getY();

						if (input.isCardCoordinate(x + 1, y + 1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x + 1, y + 1);
							int next = c1.getCode();
							if (c1.getKingdom() == Elements.VEGETAL) {
								if (input.isCardCoordinate(x + 2, y + 2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 2, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.VEGETAL) {
										n++;
										copy8.remove(j);
										copy8.remove(next);
										copy8.remove(next_next);
									}
								}
							}
						}
					}
				}

				break;

			case 9:
				n = input.getCounterOfElements().get(Elements.MUSHROOMS) / 3;
				break;

			case 10:
				n = input.getCounterOfElements().get(Elements.INSECT) / 3;
				break;

			case 11:
				n = input.getCounterOfElements().get(Elements.ANIMALS) / 3;
				break;

			case 12:
				n = input.getCounterOfElements().get(Elements.VEGETAL) / 3;
				break;

			case 13:
				ArrayList<Integer> copy13 = input.getExists();
				for(int i = 1; i <= 10; i++) {
					if(copy13.contains(i)) {
						Coordinate xy = input.getCoordinate(i);
						int x = xy.getX();
						int y = xy.getY();

						if(input.isCardCoordinate(x, y+1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x, y+1);
							int next = c1.getCode();
							if(c1.getKingdom() == Elements.MUSHROOMS) {
								if(input.isCardCoordinate(x+1, y+2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 1, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.VEGETAL) {
										n++;
										copy13.remove(i);
										copy13.remove(next);
										copy13.remove(next_next);
									}
								}
							}
						}
					}
				}

				for(int j = 41; j <= 50; j++) {
					if (copy13.contains(j)) {
						Coordinate xy = input.getCoordinate(j);
						int x = xy.getX();
						int y = xy.getY();

						if (input.isCardCoordinate(x, y + 1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x, y + 1);
							int next = c1.getCode();
							if (c1.getKingdom() == Elements.MUSHROOMS) {
								if (input.isCardCoordinate(x + 1, y + 2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 1, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.INSECT) {
										n++;
										copy13.remove(j);
										copy13.remove(next);
										copy13.remove(next_next);
									}
								}
							}
						}
					}
				}

				break;

			case 14:
				ArrayList<Integer> copy14 = input.getExists();
				for(int i = 21; i <= 30; i++) {
					if(copy14.contains(i)) {
						Coordinate xy = input.getCoordinate(i);
						int x = xy.getX();
						int y = xy.getY();

						if(input.isCardCoordinate(x+1, y+1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x+1, y+1);
							int next = c1.getCode();
							if(c1.getKingdom() == Elements.INSECT) {
								if(input.isCardCoordinate(x+1, y+2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 1, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.INSECT) {
										n++;
										copy14.remove(i);
										copy14.remove(next);
										copy14.remove(next_next);
									}
								}
							}
						}
					}
				}

				for(int j = 61; j <= 70; j++) {
					if (copy14.contains(j)) {
						Coordinate xy = input.getCoordinate(j);
						int x = xy.getX();
						int y = xy.getY();
						if (input.isCardCoordinate(x + 1, y + 1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x + 1, y + 1);
							int next = c1.getCode();
							if (c1.getKingdom() == Elements.INSECT) {
								if (input.isCardCoordinate(x + 1, y + 2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x + 1, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.INSECT) {
										n++;
										copy14.remove(j);
										copy14.remove(next);
										copy14.remove(next_next);
									}
								}
							}
						}
					}
				}

				break;

			case 15:
				ArrayList<Integer> copy15 = input.getExists();
				for(int i = 1; i <= 10; i++) {
					if(copy15.contains(i)) {
						Coordinate xy = input.getCoordinate(i);
						int x = xy.getX();
						int y = xy.getY();

						if(input.isCardCoordinate(x-1, y+1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x-1, y+1);
							int next = c1.getCode();
							if(c1.getKingdom() == Elements.ANIMALS) {
								if(input.isCardCoordinate(x-1, y+2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x - 1, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.ANIMALS) {
										n++;
										copy15.remove(i);
										copy15.remove(next);
										copy15.remove(next_next);
									}
								}
							}
						}
					}
				}

				for(int j = 41; j <= 50; j++) {
					if (copy15.contains(j)) {
						Coordinate xy = input.getCoordinate(j);
						int x = xy.getX();
						int y = xy.getY();

						if (input.isCardCoordinate(x - 1, y + 1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x - 1, y + 1);
							int next = c1.getCode();
							if (c1.getKingdom() == Elements.ANIMALS) {
								if (input.isCardCoordinate(x - 1, y + 2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x - 1, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.ANIMALS) {
										n++;
										copy15.remove(j);
										copy15.remove(next);
										copy15.remove(next_next);
									}
								}
							}
						}
					}
				}

				break;

			case 16:
				ArrayList<Integer> copy16 = input.getExists();
				for(int i = 11; i <= 20; i++) {
					if(copy16.contains(i)) {
						Coordinate xy = input.getCoordinate(i);
						int x = xy.getX();
						int y = xy.getY();

						if(input.isCardCoordinate(x, y+1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x, y+1);
							int next = c1.getCode();
							if(c1.getKingdom() == Elements.VEGETAL) {
								if(input.isCardCoordinate(x-1, y+2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x - 1, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.INSECT) {
										n++;
										copy16.remove(i);
										copy16.remove(next);
										copy16.remove(next_next);
									}
								}
							}
						}
					}
				}

				for(int j = 51; j <= 60; j++) {
					if (copy16.contains(j)) {
						Coordinate xy = input.getCoordinate(j);
						int x = xy.getX();
						int y = xy.getY();

						if (input.isCardCoordinate(x, y + 1)) {
							ResourceCard c1 = (ResourceCard) input.getCardInBoard(x, y + 1);
							int next = c1.getCode();
							if (c1.getKingdom() == Elements.VEGETAL) {
								if (input.isCardCoordinate(x-1, y + 2)) {
									ResourceCard c2 = (ResourceCard) input.getCardInBoard(x - 1, y + 2);
									int next_next = c2.getCode();
									if (c2.getKingdom() == Elements.INSECT) {
										n++;
										copy16.remove(j);
										copy16.remove(next);
										copy16.remove(next_next);
									}
								}
							}
						}
					}
				}

				break;

		}

		return 0;
	}*/

/*	private int minNumElement(int n1, int n2, int n3){
		int min;
		min = n1;
		if(min >n2) {
			min = n2;
		}

		if(min > n3) {
			min = n3;
		}

		return min;
	}*/

	public abstract int checkGoal(Board board);

	public abstract int countPoint(Board board);
}
