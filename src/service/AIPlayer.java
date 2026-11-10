package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Board;

public class AIPlayer {

    private final WinChecker winChecker;
    private final Random random;

    public AIPlayer() {
        winChecker = new WinChecker();
        random = new Random();
    }

    public int chooseMediumColumn(Board board, int aiPlayer, int humanPlayer) {

        // 1. Look for a winning move for the AI.
        for (int column = 0; column < Board.COLS; column++) {

            Board simulation = copyBoard(board);

            if (!simulation.dropToken(column, aiPlayer)) {
                continue;
            }

            if (winChecker.hasWon(simulation, aiPlayer)) {
                return column;
            }
        }

        // 2. Block an immediate winning move by the human player.
        for (int column = 0; column < Board.COLS; column++) {

            Board simulation = copyBoard(board);

            if (!simulation.dropToken(column, humanPlayer)) {
                continue;
            }

            if (winChecker.hasWon(simulation, humanPlayer)) {
                return column;
            }
        }

        // 3. Avoid moves that allow the human player to win immediately.
        List<Integer> safeColumns = new ArrayList<>();

        for (int column = 0; column < Board.COLS; column++) {

            Board simulation = copyBoard(board);

            // Check whether the column is available.
            if (!simulation.dropToken(column, aiPlayer)) {
                continue;
            }

            boolean humanCanWin = false;

            // Simulate the human player's possible response.
            for (int humanColumn = 0; humanColumn < Board.COLS; humanColumn++) {

                Board humanSimulation = copyBoard(simulation);

                if (!humanSimulation.dropToken(humanColumn, humanPlayer)) {
                    continue;
                }

                if (winChecker.hasWon(humanSimulation, humanPlayer)) {
                    humanCanWin = true;
                    break;
                }
            }

            if (!humanCanWin) {
                safeColumns.add(column);
            }
        }

        // 4. Choose from the safe columns, if possible.
        if (!safeColumns.isEmpty()) {

            List<Integer> preferredColumns = new ArrayList<>();

            for (int column : safeColumns) {

                // The center column is preferred,
                // but the other columns remain possible.
                if (random.nextInt(100) < getColumnPreference(column)) {
                    preferredColumns.add(column);
                }
            }

            if (!preferredColumns.isEmpty()) {
                return preferredColumns.get(
                        random.nextInt(preferredColumns.size())
                );
            }

            return safeColumns.get(
                    random.nextInt(safeColumns.size())
            );
        }

        // 5. If all moves are dangerous, choose an available column.
        List<Integer> availableColumns = new ArrayList<>();

        for (int column = 0; column < Board.COLS; column++) {
            if (board.getCell(0, column) == 0) {
                availableColumns.add(column);
            }
        }

        if (availableColumns.isEmpty()) {
            return -1;
        }

        return availableColumns.get(random.nextInt(availableColumns.size()));
    }

    private Board copyBoard(Board original) {

        Board copy = new Board();

        // Copy the tokens in each column,
        // from the bottom to the top.
        for (int column = 0; column < Board.COLS; column++) {

            for (int row = Board.ROWS - 1; row >= 0; row--) {

                int player = original.getCell(row, column);

                if (player == 1 || player == 2) {
                    copy.dropToken(column, player);
                }
            }
        }

        return copy;
    }

    private int getColumnPreference(int column) {

        switch (column) {
            case 3:
                return 90; // Center column: strongly preferred.
            case 2:
            case 4:
                return 70; // Columns close to the center.
            case 1:
            case 5:
                return 50; // Intermediate columns.
            default:
                return 30; // Outer columns.
        }
    }

    public int chooseEasyColumn(Board board) {
        List<Integer> availableColumns = new ArrayList<>();

        for (int column = 0; column < Board.COLS; column++) {
            if (board.getCell(0, column) == 0) {
                availableColumns.add(column);
            }
        }

        if (availableColumns.isEmpty()) {
            return -1;
        }

        return availableColumns.get(
            random.nextInt(availableColumns.size())
        );
    }

    public int chooseHardColumn(Board board, int aiPlayer, int humanPlayer) {

        int bestColumn = -1;
        int bestScore = Integer.MIN_VALUE;

        // Start by examining the center columns.
        int[] preferredOrder = {3, 2, 4, 1, 5, 0, 6};

        for (int column : preferredOrder) {

            if (board.getCell(0, column) != 0) {
                continue;
            }

            Board simulatedBoard = copyBoard(board);

            if (!simulatedBoard.dropToken(column, aiPlayer)) {
                continue;
            }

            int score = minimax(
                    simulatedBoard,
                    5,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    false,
                    aiPlayer,
                    humanPlayer
            );

            if (score > bestScore) {
                bestScore = score;
                bestColumn = column;
            }
        }

        return bestColumn;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean maximizingPlayer, int aiPlayer, int humanPlayer) {

        if (winChecker.hasWon(board, aiPlayer)) {
            return 100000 + depth;
        }

        if (winChecker.hasWon(board, humanPlayer)) {
            return -100000 - depth;
        }

        if (depth == 0 || board.isFull()) {
            return evaluateBoard(board, aiPlayer, humanPlayer);
        }

        int[] preferredOrder = {3, 2, 4, 1, 5, 0, 6};

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;

            for (int column : preferredOrder) {
                Board simulation = copyBoard(board);

                if (!simulation.dropToken(column, aiPlayer)) {
                    continue;
                }

                int evaluation = minimax(
                        simulation,
                        depth - 1,
                        alpha,
                        beta,
                        false,
                        aiPlayer,
                        humanPlayer);

                maxEval = Math.max(maxEval, evaluation);
                alpha = Math.max(alpha, evaluation);

                if (beta <= alpha) {
                    break;
                }
            }

            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;

            for (int column : preferredOrder) {
                Board simulation = copyBoard(board);

                if (!simulation.dropToken(column, humanPlayer)) {
                    continue;
                }

                int evaluation = minimax(
                        simulation,
                        depth - 1,
                        alpha,
                        beta,
                        true,
                        aiPlayer,
                        humanPlayer);

                minEval = Math.min(minEval, evaluation);
                beta = Math.min(beta, evaluation);

                if (beta <= alpha) {
                    break;
                }
            }

            return minEval;
        }
    }

    private int evaluateBoard(Board board, int aiPlayer, int humanPlayer) {

        int score = 0;

        // The center of the board offers more opportunities.
        int centerColumn = Board.COLS / 2;

        for (int row = 0; row < Board.ROWS; row++) {
            if (board.getCell(row, centerColumn) == aiPlayer) {
                score += 6;
            } else if (board.getCell(row, centerColumn) == humanPlayer) {
                score -= 6;
            }
        }

        // Evaluate all possible four-cell windows.
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {

                // Horizontal
                if (col + 3 < Board.COLS) {
                    score += evaluateWindow(
                            board, row, col, 0, 1,
                            aiPlayer, humanPlayer);
                }

                // Vertical
                if (row + 3 < Board.ROWS) {
                    score += evaluateWindow(
                            board, row, col, 1, 0,
                            aiPlayer, humanPlayer);
                }

                // Descending diagonal
                if (row + 3 < Board.ROWS
                        && col + 3 < Board.COLS) {
                    score += evaluateWindow(
                            board, row, col, 1, 1,
                            aiPlayer, humanPlayer);
                }

                // Ascending diagonal
                if (row - 3 >= 0 && col + 3 < Board.COLS) {
                    score += evaluateWindow(
                            board, row, col, -1, 1,
                            aiPlayer, humanPlayer);
                }
            }
        }

        return score;
    }

    private int evaluateWindow(
            Board board,
            int row,
            int col,
            int rowStep,
            int colStep,
            int aiPlayer,
            int humanPlayer) {

        int aiCount = 0;
        int humanCount = 0;
        int emptyCount = 0;

        // Examine the four cells in the window.
        for (int i = 0; i < 4; i++) {
            int cell = board.getCell(
                row + i * rowStep,
                col + i * colStep
            );

            if (cell == aiPlayer) {
                aiCount++;
            } else if (cell == humanPlayer) {
                humanCount++;
            } else if (cell == 0) {
                emptyCount++;
            }
        }

        // Both players have tokens in this window.
        if (aiCount > 0 && humanCount > 0) {
            return 0;
        }

        // Evaluate the AI's opportunities.
        if (aiCount == 3 && emptyCount == 1) {
            return 100;
        }

        if (aiCount == 2 && emptyCount == 2) {
            return 10;
        }

        // Evaluate the human player's threats.
        if (humanCount == 3 && emptyCount == 1) {
            return -120;
        }

        if (humanCount == 2 && emptyCount == 2) {
            return -12;
        }

        return 0;
    }
}
