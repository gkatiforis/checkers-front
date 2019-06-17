package com.katiforis.checkers.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Board implements Serializable {

    private Cell[][] board;
    private static int BOARD_SIZE = 8;

    public Board(){
        board = new Cell[Board.BOARD_SIZE][Board.BOARD_SIZE];
    }

    public Cell getCell(int x, int y) {
        if((x<0 || x > 7) || (y<0 || y >7)){
            return null;
        }
        return this.board[x][y];
    }

    public List<Cell> getCellPieces(String givenColor)  {
        List<Cell> pieces = new ArrayList<>(12);

        for(int i=0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if(this.board[i][j] != null && this.board[i][j].getPlacedPiece() != null && this.board[i][j].getPlacedPiece().getColor().equals(givenColor)){
                    pieces.add(this.board[i][j]);
                }
            }
        }
        return pieces;
    }

    public List<Cell> movePiece(int fromX, int fromY, int toX, int toY) {
        Cell srcCell = this.getCell(fromX, fromY);
        Cell dstCell = this.getCell(toX, toY);
        List<Cell> changedCells = new ArrayList<>();
        if(srcCell.getPlacedPiece() == null){
            return Collections.emptyList();
        }
        if(dstCell.getPlacedPiece() != null){
            return Collections.emptyList();
        }

        if(isCaptureMove(srcCell, dstCell)){
            int capturedCellX = (fromX + toX)/ 2;
            int capturedCellY= (fromY + toY)/2;
            Cell cell = this.board[capturedCellX][capturedCellY];
            removePiece(cell);
            changedCells.add(cell);
        }
        srcCell.movePiece(dstCell);
        changedCells.add(srcCell);
        changedCells.add(dstCell);
        return changedCells;
    }

    public boolean isValidMove(Cell srcCell, Cell dstCell){
        List<Move> moves = possibleMoves(srcCell);
        for(Move move:moves){
            if(move.getTo().equals(dstCell)){
                return true;
            }
        }
        return false;
    }

    public List<Cell> movePiece(int[] src, int[] dst) {
        if(src.length != 2 || dst.length != 2){
           return Collections.emptyList();
        }
        return movePiece(src[0], src[1], dst[0], dst[1]);
    }

    public void removePiece(Cell cell){
        if(cell.getPlacedPiece().getColor().equals(Piece.LIGHT)){
            cell.placePiece(null);
        }
        else if(cell.getPlacedPiece().getColor().equals(Piece.DARK)){
            cell.placePiece(null);
        }
    }
    public List<Move> possibleMoves(Cell cell)  {
        return possibleMoves(Arrays.asList(cell));
    }

    public List<Move> possibleMoves(List<Cell> cells)  {
        List<Move> nextMoves = new ArrayList<>();
        List<Move> requiredNextMoves = new ArrayList<>();
        if(cells == null){
           return Collections.emptyList();
        }

        for (Cell givenCell : cells) {

        Piece givenPiece = givenCell.getPlacedPiece();

        if(givenPiece == null){
            Cell c  = getCell(givenCell.getX(), givenCell.getY());

            if(c != null){
                givenCell = c;
                givenPiece = c.getPlacedPiece();
            }
            if(givenPiece == null){
               continue;
            }
        }

        String playerColor = givenPiece.getColor();
        String opponentColor = Piece.getOpponentColor(playerColor);

        if(playerColor.equals(Piece.LIGHT)){
            int nextX = givenCell.getX()+1;
            if(nextX < 8){
                int nextY = givenCell.getY()+1;
                if(nextY < 8){
                    if(!this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                    }

                    else if(this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX + 1;
                        int yCoordAfterHoping = nextY + 1;
                        if(xCoordAfterHoping < 8 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                        }
                    }
                }
                nextY = givenCell.getY() -1;
                if(nextY >=0){
                    if(!this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                    }

                    else if(this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX + 1;
                        int yCoordAfterHoping = nextY - 1;
                        if(xCoordAfterHoping < 8 && yCoordAfterHoping >= 0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                        }
                    }
                }
            }
            if(givenPiece.isKing()){
                nextX = givenCell.getX() -1;
                if(nextX >=0){
                    int nextY = givenCell.getY()+1;
                    if(nextY < 8 && !this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                    }

                    else if(nextY < 8 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX - 1;
                        int yCoordAfterHoping = nextY + 1;
                        if(xCoordAfterHoping >=0 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                        }
                    }
                    nextY = givenCell.getY() -1;
                    if(nextY >=0 && !this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                    }

                    else if(nextY >=0 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX - 1;
                        int yCoordAfterHoping = nextY - 1;
                        if(xCoordAfterHoping >=0 && yCoordAfterHoping >= 0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                        }
                    }
                }
            }
        }
        else if(givenPiece.getColor().equals(Piece.DARK)){
            int nextX = givenCell.getX()-1;
            if(nextX >= 0){
                int nextY = givenCell.getY()+1;
                if(nextY < 8 && !this.board[nextX][nextY].containsPiece()){
                    nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                }

                else if(nextY < 8 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                    int xCoordAfterHoping = nextX -1;
                    int yCoordAfterHoping = nextY +1;
                    if(xCoordAfterHoping >=0 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                        requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                    }
                }
                nextY = givenCell.getY()-1;
                if(nextY >=0 && !this.board[nextX][nextY].containsPiece()){
                    nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                }
                else if(nextY >=0 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                    int xCoordAfterHoping = nextX -1;
                    int yCoordAfterHoping = nextY - 1;
                    if(xCoordAfterHoping >=0 && yCoordAfterHoping >=0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                        requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                    }
                }
            }
            if(givenPiece.isKing()){
                nextX = givenCell.getX()+1;
                if(nextX < 8){
                    int nextY = givenCell.getY()+1;
                    if(nextY < 8 && !this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                    }
                    else if(nextY < 8 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX + 1;
                        int yCoordAfterHoping = nextY +  1;
                        if(xCoordAfterHoping < 8 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                        }
                    }
                    nextY = givenCell.getY() -1;
                    if(nextY >=0 && !this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                    }
                    else if(nextY >=0 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX + 1;
                        int yCoordAfterHoping = nextY -  1;
                        if(xCoordAfterHoping < 8 && yCoordAfterHoping >= 0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                        }
                    }
                }
            }
        }
        }
        if(!requiredNextMoves.isEmpty()){
            return requiredNextMoves;
        }else{
            return nextMoves;
        }
    }

    public List<Move> getCaptureMoves(Cell givenCell) {
        if(givenCell == null){
           return Collections.emptyList();
        }
        List<Move> possibleMovesOfCell = possibleMoves(Arrays.asList(givenCell));
        List<Move> capturingMoves = new ArrayList<>();
        for(Move move: possibleMovesOfCell){
            if(isCaptureMove(move.getFrom(), move.getTo())){
                capturingMoves.add(move);
            }
        }
        return capturingMoves;
    }

    public boolean isCaptureMove(Cell srcCell, Cell dstCell) {
        if (srcCell == null || dstCell == null) {
           return false;
        }
        if (srcCell.getPlacedPiece() == null) {
            srcCell = getCell(srcCell.getX(), srcCell.getY());
        }
        return (Math.abs(srcCell.getX() - dstCell.getX()) == 2) && (Math.abs(srcCell.getY() - dstCell.getY()) == 2);
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public boolean hasMoves(String color){
        List<Cell> pieces = getCellPieces(color);
        if(!pieces.isEmpty()){
            if(!possibleMoves(pieces).isEmpty()){
                return true;
            }
        }
        return false;
    }
}
