# Chess Application v2.0
Re-write of my chess-application to separate out handling game flow and state from controller to aide in stockfish integration and possible online play.

White pieces always starts first, click a square to select a piece then click the square you want to move it to.  Legal moves will have their squares highlighted in yellow.  If you choose the wrong piece by accident you can move it to a legal square then use the Undo button to choose a different piece or move to an illegal square and it will prompt you to choose a new piece right away.

If you want to see the previous work I made on this program starting from the very beginning the oldest repository can be found here: https://github.com/MichaelJOHare/chess-game.  
The repository containing the GUI implementation can be found here: https://github.com/MichaelJOHare/chess-application.

## Pictures

### Start of game
![Dialog-Pane](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/d30b01a8-3efb-4450-bd4f-d4f646f8189c)  ![Chess-Application](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/56395e1b-905c-45d7-abfe-78ea2cfbc0af)


### Mid-game
Flip the board if you want to play with black pieces.
![Chess-Application-Flip-Board](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/6cff627f-8e7e-422b-a777-b62f68d6c4f8)

Yellow highlighted squares are legal moves for the piece that is selected.
![Chess-Application-MidGame](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/dcb3816d-10e7-4597-9cc3-6f297f3d8f6a)

Or give the current board to Stockfish and ask it for the best move (Green highlighted squares)
![Chess-Application-Ask-Stockfish](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/ffc49857-7519-4b49-85d9-9ed0e672a904)

Working en passant and castling!
![Chess-Application-En-Passant](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/d5da1aa4-d082-4c59-8574-57128902d448)

### Checkmate!
![Chess-Application-Checkmate](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/0041845f-f9bb-46c8-9c8b-6f423edb2083)
