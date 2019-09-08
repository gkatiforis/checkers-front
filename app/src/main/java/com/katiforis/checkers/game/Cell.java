package com.katiforis.checkers.game;

import java.io.Serializable;

public class Cell implements Serializable {
	private int x, y;
	private Piece placedPiece;

	public Cell(int x, int y){
		if((x<0 || x>7) || (y<0 || y>7)){
			return;
		}
		this.x = x;
		this.y = y;
		this.placedPiece = null;
	}

	public Piece getPlacedPiece(){
		return this.placedPiece;
	}

	public void setPlacedPiece(Piece placedPiece) {
		this.placedPiece = placedPiece;
	}

	public int[] getCoords(){
		int[] coords = {x, y};
		return coords;
	}

	public void placePiece(Piece givenPiece){
		this.placedPiece = givenPiece;
		if(givenPiece != null){
			givenPiece.setCell(this);
			if(this.x == 0 && givenPiece.getColor().equals(Piece.DARK)){
				this.placedPiece.makeKing();
			}
			else if(this.x == 7 && givenPiece.getColor().equals(Piece.LIGHT)){
				this.placedPiece.makeKing();
			}
		}
	}

	public boolean containsPiece(){
		return (this.placedPiece != null);
	}

	public void movePiece(Cell anotherCell) {
		if(anotherCell == null){
			return;
		}
		//TODO: enable play piece sound
		//GameActivity.INSTANCE.getAudioPlayer().playPiece();
		anotherCell.placePiece(this.placedPiece);
		this.placedPiece.setCell(anotherCell);
		this.placedPiece = null;
	}

	public String toString(){
		String str = "";
		str += "Cell Loc: ("+ this.x + ", " + this.y + ") \t Placed piece: ";
		if(this.placedPiece == null){
			str += "nothing\n";
		}
		else{
			str += this.placedPiece.getColor() + "  isKing: " + placedPiece.isKing()+ "\n";
		}
		return str;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cell cell = (Cell) o;
		return x == cell.x &&
				y == cell.y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}
}




