package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece starting = board.getPiece(startPosition);
        if (starting == null){
            return null;
        }
        Collection<ChessMove> moves = starting.pieceMoves(board, startPosition);
        ArrayList<ChessMove> legalMoves = new ArrayList<>();
        for (ChessMove move:moves){
            ChessBoard copy = new ChessBoard(board);
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();
            copy.addPiece(end, board.getPiece(start));
            copy.addPiece(start, null);
            if (isInCheckOnBoard(starting.getTeamColor(),copy) == false){
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPiece piece = board.getPiece(start);
        if (piece == null){
            throw new InvalidMoveException("No piece at starting position");
        }
        if (piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException("Not your turn!");
        }
        if (!validMoves(start).contains(move)){
            throw new InvalidMoveException("Illegal move");
        }
        ChessPosition end = move.getEndPosition();
        if (move.getPromotionPiece() != null) {
            board.addPiece(end, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        } else {
            board.addPiece(end, piece);
        }
        board.addPiece(start, null);
        if (teamTurn == TeamColor.BLACK){
            teamTurn = TeamColor.WHITE;
        } else {
            teamTurn = TeamColor.BLACK;
        }
    }
    private boolean hasAnyValidMoves (TeamColor teamColor){
        for (int i=1; i<=8; i++){
            for (int j=1; j<=8; j++){
                ChessPosition square = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(square);
                if(piece != null && piece.getTeamColor() == teamColor && !validMoves(square).isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckOnBoard(teamColor, board);
    }

    private ChessPosition findKing(TeamColor teamColor, ChessBoard board) {
        for (int i=1; i<=8; i++){
            for (int j=1; j<=8; j++){
                ChessPosition square = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(square);
                if(piece != null && ChessPiece.PieceType.KING == piece.getPieceType() && piece.getTeamColor() == teamColor){
                    return square;
                }
            }
        }
        return null;
    }
    private boolean isInCheckOnBoard(TeamColor teamColor, ChessBoard board){
        ChessPosition kingPosition = findKing(teamColor,board);
        for (int i=1; i<=8; i++){
            for (int j=1; j<=8; j++){
                ChessPosition square = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(square);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = piece.pieceMoves(board, square);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
