package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

/** Turns a ChessBoard into colored text for the terminal. */
public class BoardPrinter {

    public static String draw(ChessBoard board, ChessGame.TeamColor perspective) {
        boolean whiteView = (perspective == ChessGame.TeamColor.WHITE);
        StringBuilder out = new StringBuilder();

        out.append(columnLabels(whiteView));
        for (int i = 0; i < 8; i++) {
            int row = whiteView ? 8 - i : 1 + i;
            out.append(" ").append(row).append(" ");
            for (int j = 0; j < 8; j++) {
                int col = whiteView ? 1 + j : 8 - j;
                out.append(squareColor(row, col));
                out.append(pieceText(board.getPiece(new ChessPosition(row, col))));
            }
            out.append(RESET_BG_COLOR).append(RESET_TEXT_COLOR);
            out.append(" ").append(row).append("\n");
        }
        out.append(columnLabels(whiteView));
        return out.toString();
    }

    private static String columnLabels(boolean whiteView) {
        String letters = whiteView ? "abcdefgh" : "hgfedcba";
        StringBuilder labels = new StringBuilder("   ");
        for (char letter : letters.toCharArray()) {
            labels.append(" ").append(letter).append(" ");
        }
        return labels.append("\n").toString();
    }

    private static String squareColor(int row, int col) {
        boolean dark = (row + col) % 2 == 0;
        return dark ? SET_BG_COLOR_BLACK : SET_BG_COLOR_LIGHT_GREY;
    }

    private static String pieceText(ChessPiece piece) {
        if (piece == null) {
            return "   ";
        }
        String textColor = (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE;
        String letter = switch (piece.getPieceType()) {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
        return textColor + SET_TEXT_BOLD + " " + letter + " " + RESET_TEXT_BOLD_FAINT;
    }
}