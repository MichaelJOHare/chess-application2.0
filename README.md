# Chess Application v2.0
Re-write of my chess-application to separate out handling game flow and state from controller to aide in stockfish integration and possible online play.

White pieces always starts first, click a square to select a piece then click the square you want to move it to.  Legal moves will have their squares highlighted in yellow.  If you choose the wrong piece by accident you can move it to a legal square then use the Undo button to choose a different piece or move to an illegal square and it will prompt you to choose a new piece right away.

If you want to see the previous work I made on this program starting from the very beginning the oldest repository can be found here: https://github.com/MichaelJOHare/chess-game.  
The repository containing the GUI implementation can be found here: https://github.com/MichaelJOHare/chess-application.

## Pictures

### Start of game
![Dialog-Pane](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/d30b01a8-3efb-4450-bd4f-d4f646f8189c) <img width="1200" alt="start-game" src="https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/6aabd0f4-b6ba-461c-9e1a-3aefc82a4ab4">



### Mid-game
Flip the board if you want to play with black pieces.
<img width="1200" alt="flip-board" src="https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/890a349f-42d2-41d9-93e9-f5e199299fc4">

Green dots are are legal moves for the piece that is selected (knight on c3 in this example) while green corners indicate legal captures.
<img width="1200" alt="knight-moves" src="https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/23890313-ad18-4d36-b825-aef78f45d9dd">


Or give the current board to Stockfish and ask it for the best move (Green highlighted squares)
<img width="1200" alt="ask-stockfish" src="https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/29d1164b-0260-44ec-b200-fbc513c04c23">


Working en passant and castling!

<img width="1200" alt="en-passant" src="https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/8d492b4c-5a26-4093-87c3-940e12672eb6"> 
<img width="1200" alt="castling" src="https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/4859fbb5-5390-4656-836e-bb26d462ecfe">



### Checkmate and Stalemate!
<img width="1200" alt="checkmate" src="https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/96fff70f-001c-4a92-9c90-d2847d0174d5"> 
<img width="1200" alt="stalemate" src="https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/54188b42-b4b4-4ccd-83af-ed0d11ab4b78">


