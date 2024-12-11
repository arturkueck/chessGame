# chessGame

## Task 3
Encountered Bugs: 
Not all chess rules are implemented. No Bugs with possible configurations. Sometime laggy runetime due to resizing of the frame.

You can start the frontend/main.java without runparametes to get a plain chessboard with no configurationpossibility and a notation of the moves you are doing.

you can give up to 3 parameters for the main-method:
"fieldnames"
"colorpicker"
"rotator"

fieldnames:
You can check a checkbox for showing and higind the names of the fields

colorpicker:
You get two colorpickers for the by default white and brown squares. Now you can choose your own fieldbackground. 
Advise 1: Dont take a black background as you wount be able to see the black pieces anymore. The fieldnames are always visible.
Advise 2: Dont make the by default white squares darken then the by default black squares.

rotator:
You can check a checkbox to rotate the black pieces around. Can be nice if you want to play with a friend on e.g. a tablet laying on a table.

## Task 4
features fieldnames, colorpicker and rotator are now selectable via the config of the feature-model-ide

Encountered Bugs: 
Promotion had few bugs, concidering the registration of the piece inside the board class and the registration of the position inside the piece so that the piece can determine its reachable fields. 
Capturing a pawn EnPassat was working not properly at first. The piece was not being captures despite the enemy pawn performing the EnPassat movement.

features:

notation:
the moves are being notated next to the board.

enpassat:
a pawn can take a pawn that performed a double move next to it.

pawnpromotion:
a pawn can transform into another piece excl. king and pawn once reaching the last column.

