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
