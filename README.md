# Chess Application v2.0
Re-write of my chess-application to separate out handling game flow and state from controller to aide in stockfish integration and possible online play.

White pieces always starts first, click a square to select a piece then click the square you want to move it to.  Legal moves will have their squares highlighted in yellow.  If you choose the wrong piece by accident you can move it to a legal square then use the Undo button to choose a different piece or move to an illegal square and it will prompt you to choose a new piece right away.

If you want to see the previous work I made on this program starting from the very beginning the oldest repository can be found here: https://github.com/MichaelJOHare/chess-game.  
The repository containing the GUI implementation can be found here: https://github.com/MichaelJOHare/chess-application.

## Pictures

### Start of game
![Dialog-Pane](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/d30b01a8-3efb-4450-bd4f-d4f646f8189c) ![start-game](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/15d5e103-a7f7-4a90-9339-16cbd33a80f1)




### Mid-game
Flip the board if you want to play with black pieces.
![flip-board](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/870f4231-0937-4bef-bc71-dd2454e4ee7c)


Green dots are are legal moves for the piece that is selected (knight on c3 in this example) while green corners indicate legal captures.
![knight-moves](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/83e2bd49-3d14-444d-92f7-e0f25d4e0e63)



Or give the current board to Stockfish and ask it for the best move (Purple highlighted squares)
![ask-stockfish](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/7009e936-f261-4050-a894-3572939a5284)


# Working en passant, castling, and pawn promotion!
En Passant
![en-passant](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/8b4dfe79-d833-4fd9-a59d-5ee008a11195)
Castling
![castling](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/656b75e3-d678-4c62-bf74-8eb390be9a80)
Before pawn promotion
![before-promote](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/19deaf55-8bde-406d-aa17-847d3a799e8f)
Choosing promotion piece
![during-promote](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/dcb84f6a-6d4e-4f4f-94b2-aab1589c2316)
After promotion
![after-promote](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/995c410a-1438-4b19-a388-a30931b2c8be)



### Checkmate and Stalemate!
![checkmate](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/f11abac9-d849-418d-b6c2-dc9e616f2301)
![stalemate](https://github.com/MichaelJOHare/chess-application2.0/assets/46801493/3cdef4ae-191a-4c30-8913-aa4a19c01e77)


