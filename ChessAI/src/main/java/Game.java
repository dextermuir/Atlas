import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {
  Logger logger = Logger.getLogger(Main.class.getName());
  private int turnCount;
  private ChessBoard chessBoard;
  private NeuralNetworkManager networkManager;
  public Game(NeuralNetworkManager mainNeuralNetworkManager)
  {
    turnCount = 0;
    chessBoard = new ChessBoard();
    networkManager = mainNeuralNetworkManager;
  }

  /**
   * To initialise and contain the game loop
   */
  public void play()
  {
    logger.log(Level.INFO, "game started");
    boolean gameOver = false;
    while(!gameOver)
    {
      //calculate a new output array -> bUP short for board update parameters
      int[] bUPs = networkManager.calculateNewOutputArray(chessBoard);
      if(chessBoard.isMoveLegal(bUPs[0], bUPs[1], bUPs[2], bUPs[3], bUPs[4]))
      {
        //legal move found -> update board + add position and move to memory -> check game over
        chessBoard.updateBoard(bUPs[0], bUPs[1], bUPs[2], bUPs[3], bUPs[4]);
        networkManager.addInputAndOutputArraysToMemory();

        //check for game overs
        if(chessBoard.thereIsInsufficientMaterial() || networkManager.drawByThreepeatRepetition())
        {
          System.out.println("draw by repetition or insufficient material");
          gameOver = true;
          break;
        }
        if(chessBoard.oppositeColourKingIsInStalemateOrCheckmate())
        {
          gameOver = true;
          chessBoard.flipBoard();
          if(chessBoard.checkCheck(chessBoard.getBoard()))
          {
            System.out.println("checkmate");
            if(turnCount % 2 == 0)
            {
              chessBoard.printBoard();
              for(int i = 0; i < networkManager.getOutputArrayMemory().length; i++)
              {
                if(i % 2 == 0)
                {
                  networkManager.editWeightsAndBiases(true, networkManager.getOutputArrayMemory()[i], networkManager.getSelectedIndexOfEachOutputArrayMemory()[i], networkManager.getInputArrayMemory()[i]);
                }
                else
                {
                  networkManager.editWeightsAndBiases(false, networkManager.getOutputArrayMemory()[i], networkManager.getSelectedIndexOfEachOutputArrayMemory()[i], networkManager.getInputArrayMemory()[i]);
                }
              }
            }
            else
            {
              chessBoard.flipBoard();
              chessBoard.printBoard();
              for(int i = 0; i < networkManager.getOutputArrayMemory().length; i++)
              {
                if(i % 2 == 0)
                {
                  networkManager.editWeightsAndBiases(false, networkManager.getOutputArrayMemory()[i], networkManager.getSelectedIndexOfEachOutputArrayMemory()[i], networkManager.getInputArrayMemory()[i]);
                }
                else
                {
                  networkManager.editWeightsAndBiases(true, networkManager.getOutputArrayMemory()[i], networkManager.getSelectedIndexOfEachOutputArrayMemory()[i], networkManager.getInputArrayMemory()[i]);
                }
              }
            }
          }
          else
          {
            System.out.println("stalemate");
          }
          chessBoard.flipBoard();
          break;
        }
        if(turnCount % 2 == 0)
        {
          System.out.println("white has moved");
          chessBoard.printBoard();
          chessBoard.flipBoard();
        }
        else
        {
          System.out.println("black has moved");
          chessBoard.flipBoard();
          chessBoard.printBoard();
        }
        turnCount++;
      }
      else //illegal move attempted -> loop and deprio attempted move
      {
        networkManager.editWeightsAndBiases(false, networkManager.getCurrentOutputArray(), networkManager.getCurrentSelectedOutputArrayIndex(), networkManager.getCurrentInputArray());
      }
    }
    System.out.println("number of moves played = " + turnCount);
    logger.log(Level.INFO, "game over");
  }

  /**
   * to test en passant functionality
   */
  public void enPassantTest() {
    if (chessBoard.isMoveLegal(1, 4, 3, 4, -1)) {
      chessBoard.updateBoard(1, 4, 3, 4, -1);
    }
    chessBoard.printBoard();
    chessBoard.flipBoard();

    if (chessBoard.isMoveLegal(3, 3, 2, 4, -1)) {
      chessBoard.updateBoard(3, 3, 2, 4, -1);
    }
    chessBoard.flipBoard();
    chessBoard.printBoard();

    if (chessBoard.isMoveLegal(0, 1, 2, 2, -1)) {
      chessBoard.updateBoard(0, 1, 2, 2, -1);
    }
    chessBoard.printBoard();
    chessBoard.flipBoard();
  }

  /**
   * to test functionality of the flipBoard method
   */
  public void flipBoardTest() {
    chessBoard.setupTestBoard1();
    chessBoard.printBoard();
    chessBoard.flipBoard();
    chessBoard.printBoard();
    chessBoard.flipBoard();
    chessBoard.printBoard();
  }

  /**
   * to test parts of the neural network manager that will be called from game
   */
  public void neuralNetworkWeightsAndBiasesEditingTest()
  {
    networkManager.calculateNewOutputArray(chessBoard); //bUP short for board update parameters
    networkManager.editWeightsAndBiases(false, networkManager.getCurrentOutputArray(), networkManager.getCurrentSelectedOutputArrayIndex(), networkManager.getCurrentInputArray());
    networkManager.calculateNewOutputArray(chessBoard); //bUP short for board update parameters
  }
}