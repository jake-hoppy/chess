package chess;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }
    private Collection<ChessMove> slidingMoves(ChessBoard board, ChessPosition myPosition, int[][] directions){
        List<ChessMove> moves = new ArrayList<>();
        for (int i=0; i<directions.length; i++){

            int rowChange = directions[i][0];
            int colChange = directions[i][1];

            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            while(true){
                row = row + rowChange;
                col = col + colChange;

                if (row < 1 || row > 8 || col < 1 || col > 8) {
                    break;
                }
                ChessPosition spot = new ChessPosition(row,col);
                ChessPiece pieceThere = board.getPiece(spot);

                if (pieceThere == null){
                    moves.add(new ChessMove(myPosition,spot,null));
                } else if(pieceThere.getTeamColor() == pieceColor){
                    break;
                } else {
                    moves.add(new ChessMove(myPosition,spot,null));
                    break;
                }
            }
        }
        return moves;
    }
    private Collection<ChessMove> singleStepMoves (ChessBoard board, ChessPosition myPosition, int[][] spots){
        List<ChessMove> moves = new ArrayList<>();
        for (int i=0; i<spots.length; i++){
            int rowChange = spots[i][0];
            int colChange = spots[i][1];

            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            row = row + rowChange;
            col = col + colChange;

            if (row < 1 || row > 8 || col < 1 || col > 8) {
                continue;
            }
            ChessPosition spot = new ChessPosition(row,col);
            ChessPiece pieceThere = board.getPiece(spot);

            if (pieceThere == null){
                moves.add(new ChessMove(myPosition, spot, null));
            } else if ((pieceThere.getTeamColor() == pieceColor)){
                continue;
            } else {
                moves.add(new ChessMove(myPosition, spot, null));
                continue;
            }
        }
        return moves;
    }
    private void addPawnMove(List<ChessMove> moves, ChessPosition from, ChessPosition to, int promotionRow) {
        if (to.getRow() == promotionRow) {
            moves.add(new ChessMove(from, to, PieceType.QUEEN));
            moves.add(new ChessMove(from, to, PieceType.ROOK));
            moves.add(new ChessMove(from, to, PieceType.BISHOP));
            moves.add(new ChessMove(from, to, PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(from, to, null));
        }
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();
        int direction;
        int startRow;
        int promotionRow;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            direction = 1;
            startRow = 2;
            promotionRow = 8;
        } else {
            direction = -1;
            startRow = 7;
            promotionRow = 1;
        }

        int col = myPosition.getColumn();
        int row = myPosition.getRow() + direction;

        if (row >= 1 && row <= 8) {
            ChessPosition ahead = new ChessPosition(row, col);

            if (board.getPiece(ahead) == null) {
                addPawnMove(moves, myPosition, ahead, promotionRow);

                if (myPosition.getRow() == startRow) {
                    ChessPosition twoAhead = new ChessPosition(row + direction, col);
                    if (board.getPiece(twoAhead) == null) {
                        moves.add(new ChessMove(myPosition, twoAhead, null));
                    }
                }
            }

            for (int newCol : new int[]{col - 1, col + 1}) {
                if (newCol < 1 || newCol > 8) continue;
                ChessPosition target = new ChessPosition(row, newCol);
                ChessPiece occupant = board.getPiece(target);
                if (occupant != null && occupant.getTeamColor() != pieceColor) {
                    addPawnMove(moves, myPosition, target, promotionRow);
                }
            }
        }
        return moves;
    }
    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();
        // BISHOP
        if (type == PieceType.BISHOP){
            return slidingMoves(board, myPosition, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}});
        }
        if (type == PieceType.ROOK){
            return slidingMoves(board, myPosition, new int[][]{{1,0},{-1,0},{0,1},{0,-1}});
        }
        if (type == PieceType.QUEEN){
            return slidingMoves(board, myPosition, new int[][]{{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}});
        }
        if (type == PieceType.KING){
            return singleStepMoves(board, myPosition, new int[][]{{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}});
        }
        if (type == PieceType.KNIGHT){
            return singleStepMoves(board, myPosition, new int[][]{{2,1},{1,2},{-2,1},{-1,2},{-1,-2},{-2,-1},{2,-1},{1,-2}});
        }
        if (type == PieceType.PAWN){
            return pawnMoves(board, myPosition);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
