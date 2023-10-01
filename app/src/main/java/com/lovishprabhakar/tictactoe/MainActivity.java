package com.lovishprabhakar.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.lovishprabhakar.tictactoe.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int PLAYER_X = 1;
    private static final int PLAYER_O = 2;

    private int currentPlayer = PLAYER_X;
    private int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6} // Diagonals
    };

    private boolean gameActive = true;

    private GridLayout gameGrid;
    private TextView playerTurnText;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameGrid = findViewById(R.id.gridLayout);
        playerTurnText = findViewById(R.id.playerTurnText);
        resetButton = findViewById(R.id.resetButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < gameGrid.getChildCount(); i++) {
            ImageView cell = (ImageView) gameGrid.getChildAt(i);
            cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCellClick((ImageView) view);
                }
            });
        }
    }

    private void onCellClick(ImageView cell) {
        int cellIndex = Integer.parseInt(cell.getTag().toString());
        if (gameState[cellIndex] == 0 && gameActive) {
            gameState[cellIndex] = currentPlayer;
            cell.setTranslationY(-2000f);

            if (currentPlayer == PLAYER_X) {
                cell.setImageResource(R.drawable.ximage);
                currentPlayer = PLAYER_O;
                playerTurnText.setText(getString(R.string.player_turn, "Player O"));
            } else {
                cell.setImageResource(R.drawable.oimage);
                currentPlayer = PLAYER_X;
                playerTurnText.setText(getString(R.string.player_turn, "Player X"));
            }

            cell.animate().translationYBy(2000f).setDuration(300);

            if (checkForWin()) {
                playerTurnText.setText(getString(R.string.player_wins, getCurrentPlayerName()));
                gameActive = false;
            } else if (isGameDraw()) {
                playerTurnText.setText(R.string.game_draw);
                gameActive = false;
            }
        }
    }

    private boolean checkForWin() {
        for (int[] winningPosition : winningPositions) {
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                gameState[winningPosition[0]] != 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isGameDraw() {
        for (int state : gameState) {
            if (state == 0) {
                return false;
            }
        }
        return true;
    }

    private void resetGame() {
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 0;
            ImageView cell = (ImageView) gameGrid.getChildAt(i);
            cell.setImageResource(0);
        }
        currentPlayer = PLAYER_X;
        gameActive = true;
        playerTurnText.setText(getString(R.string.player_turn, "Player X"));
    }

    private String getCurrentPlayerName() {
        return (currentPlayer == PLAYER_X) ? "Player X" : "Player O";
    }
}
