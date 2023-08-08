# Chess Application v2.0
Re-write of my chess-application to separate out handling game flow and state from controller to aide in stockfish integration and possible online play.

White pieces always starts first, click a square to select a piece then click the square you want to move it to.  Legal moves will have their squares highlighted in yellow.  If you choose the wrong piece by accident you can move it to a legal square then use the Undo button to choose a different piece or move to an illegal square and it will prompt you to choose a new piece right away.

If you want to see the previous work I made on this program starting from the very beginning the oldest repository can be found here: https://github.com/MichaelJOHare/chess-game.  
The repository containing the GUI implementation can be found here: https://github.com/MichaelJOHare/chess-application.

## Pictures

### Start of game
![Chess-Application](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/cebf9fdd-c9e3-4c76-8290-6e14e731ab9d)

### Mid-game 
Yellow highlighted squares are legal moves for the piece that is selected.
![Chess-Application-MidGame](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/1bf8ef78-7dac-4eb6-8aa9-5ed279d5585c)

Or give the current board to Stockfish and ask it for the best move (Green highlighted squares)
![Chess-Application-Ask-Stockfish](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/4d024a7c-8b65-406e-b0f1-69884245f744)

### Checkmate!
![Chess-Application-Checkmate](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/754f8247-c578-425f-8064-c34f5fc689ba)
