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


How did you localize the places where the features had to be inserted?
First I coded the code in java. Secondly I looked for the functions relating to the feature beaing executed in the frontend and clicked through all nested functions, checking whather or not they are existing only for this feature. If you I added the Preprocessor #ifdef annotation.

What is the maximum number of annotation for a feature?
1

Did you find any complicated presence conditions?
As the promotion was part of the backend disabling it was messing with the pawn promotion. But i fixed any bugs.

## Task 5
new features:

check:
Only moves that end the check are allowed. Rollback method to go back to the function, ask for another move if move was illegal.

checkmate:
Check if there is a check given and there are no moves that end the check. Afterwards there are no more moves allowed. The board "freezes".

features i used as plugins:
6 chess-pieces can be selected and deselected from the arraylist in the Board-Constructor in the Board Class.

What errors did you find during testing?
There were no errors. It looks like there was robust programming for the board.

How time-consuming is it to create plugins?
Due to reusability of code from the Board-Constructor not very time-consuming.

How often did you have to adapt the framework or the interfaces afterwards?
I did a basic frameword with only one creat-method which gives not much variability so I did not have to do a lot of adaptation. It would have been more complex to let the user decide which piece exactly he/ she wants to create rather than always creating all pieces of that kind.

How many interfaces are there in your implementation? 
One abstract interface in the piece-class and the implementation in each piece --> 6.

What adjustments might be necessary when adding further plugins?
By addind the feature that the user can decide where to create a piece he could build own positions by adjusting the frameword. Now a user can only edit the start-position.

