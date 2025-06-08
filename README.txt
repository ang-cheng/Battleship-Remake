=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays - implements the player's boards (where there is no ship, where
        there is a ship, whether a strike hit sea or a ship)

  2. Collections - implements a way to keep track of the information about
        ships that have or haven't been placed on the board yet. for example,
        helps keep track of the specific locations and lengths of ships that
        may be used to evaluate other things (whether a strike can be landed
        in a specific location, the win condition of the game, whether a
        particular ship has been sunk, etc). originally intended to implement
        Collections concept by using TreeSet<Point> to show where shots had
        been fired on the board by each player, but received feedback that
        this implementation could be done using a 2D array, so I incorporated
        this into the 2D arrays component instead.

  3. JUnit Testable Component - implemented a BattleShipTest file that I wrote
        test cases in based on the model of the game in the Battleship class

  4. File I/O - implemented a save and load game feature for the game

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

Battleship: model of the game
GameBoard: controls saving/loading of the game, instantiates the game
    model, handles mouseclick interaction from the players, controls
    button functions and game status updates
RunBattleship: sets up frame, widgets, panels for the GUI
Ship: a class containing information about the players' ships,
    specifically the length and location of the ships
ShipsPanel: displays the ship pieces during the ship placement stage
    that haven't yet been placed by the players. the images displayed
    are instances of ShipObject.
ShipObject: an abstract class that helps define what the ship piece
    images in ShipsPanel should look like and where they should be
TwoShip, ThreeShipOne, ThreeShipTwo, FourShip, FiveShip: instances of
    ShipObject that are the images of the ship pieces in ShipsPanel
Game: runs the game
BattleshipTest: runs tests on the Battleship game model


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

I struggled significantly with updating the ShipsPanel accordingly to the
state of the game. For example, oftentimes when I loaded a saved game file
the ShipsPanel was incorrectly displayed. I think part of the problem was
having the ShipsPanel be based off of the current state of the game, which
I kept forgetting to update to the ShipsPanel as well.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I think I separated functionality decently and private state was encapsulated,
but there were some methods and code bits that were repetitive. I think these
particular lines of code could have been simplified. For example, I had a few
methods that took in four integers, representing a start and end location of
a ship with respect to the game board. However, I think this could be
further simplified by just taking two Points and then breaking them down into
x and y components within the function itself.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

https://i.ebayimg.com/images/g/hNkAAOSwVvRgoEzL/s-l1200.jpg