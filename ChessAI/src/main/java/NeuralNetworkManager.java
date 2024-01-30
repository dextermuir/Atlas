import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NeuralNetworkManager {
  private Logger logger = Logger.getLogger(Main.class.getName());
  public static final int INPUT_ARRAY_SIZE = ChessBoard.NO_OF_SQUARES * Piece.NO_OF_UNIQUE_PIECES + ChessBoard.NO_OF_SQUARES_THE_CURRENT_EN_PASSANT_PIECE_CAN_BE_ON + ChessBoard.NO_OF_CASTLING_PRIVILEGES;
  public static final int LAYER_ARRAY_SIZE = 50;
  public static final int OUTPUT_ARRAY_SIZE = ChessBoard.NO_OF_SQUARES * ChessBoard.NO_OF_SQUARES + ChessBoard.NO_OF_SQUARES_TO_PROMOTE_FROM * Piece.NO_OF_VALID_WAYS_TO_PROMOTE;
  private FileManager fileManager;

  //The following are the input, result and intermediary calculation result holders (layers) of the machine
  private int[] inputArray = new int[INPUT_ARRAY_SIZE];
  private double[] layer1 = new double[LAYER_ARRAY_SIZE];
  private double[] layer2 = new double[LAYER_ARRAY_SIZE];
  private double[] outputArray = new double[OUTPUT_ARRAY_SIZE];
  private int currentSelectedOutputArrayIndex;

  //The following are the delta Costs from the corresponding layer in the network
  private double[] deltaCostByDeltaLayer1Activation = new double[LAYER_ARRAY_SIZE];
  private double[] deltaCostByDeltaLayer2Activation = new double[LAYER_ARRAY_SIZE];

  //The following have their values stored to a txt file and define the decisions made by the network
  private double[] biasVectorA = new double[LAYER_ARRAY_SIZE];
  private double[] biasVectorB = new double[LAYER_ARRAY_SIZE];
  private double[] biasVectorC = new double[OUTPUT_ARRAY_SIZE];
  private double[][] weightMatrixA = new double[LAYER_ARRAY_SIZE][INPUT_ARRAY_SIZE];
  private double[][] weightMatrixB = new double[LAYER_ARRAY_SIZE][LAYER_ARRAY_SIZE];
  private double[][] weightMatrixC = new double[OUTPUT_ARRAY_SIZE][LAYER_ARRAY_SIZE];

  //The following are used to determine how much the weights need to be changed
  private int[][] inputArrayMemory = new int[0][INPUT_ARRAY_SIZE]; //stores all the input arrays of completed moves
  private double[][] outputArrayMemory = new double[0][OUTPUT_ARRAY_SIZE]; //stores all the output arrays of completed moves - should be same length as input array memory
  private int[] selectedIndexOfEachOutputArrayMemory = new int[0]; //stores the indexes from each output array of completed moves - should be same length as previous 2

  /**
   * initialise each bias at 0
   * initialise each weight with the inverse sigmoid of a random number (redo on if random == 0)
   * inverse sigmoid -> f(x) = ln(x/(1-x))
   */
  public void generateNeuralNetwork()
  {
    double random;
    for(int i = 0; i < NeuralNetworkManager.LAYER_ARRAY_SIZE; i++)
    {
      biasVectorA[i] = 0.0;
    }
    for(int i = 0; i < NeuralNetworkManager.LAYER_ARRAY_SIZE; i++)
    {
      for(int j = 0; j < NeuralNetworkManager.INPUT_ARRAY_SIZE; j++)
      {
        random = Math.random();
        //prevent 0 being picked
        while(random == 0)
        {
          random = Math.random();
        }
        //inverse sigmoid so that (0,1) -> (-inf, +inf)
        random = Math.log(random/(1 - random));
        weightMatrixA[i][j] = random;
      }
    }
    for(int i = 0; i < NeuralNetworkManager.LAYER_ARRAY_SIZE; i++)
    {
      biasVectorB[i] = 0.0;
    }
    for(int i = 0; i < NeuralNetworkManager.LAYER_ARRAY_SIZE; i++)
    {
      for(int j = 0; j < NeuralNetworkManager.LAYER_ARRAY_SIZE; j++)
      {
        random = Math.random();
        //prevent 0 being picked
        while(random == 0)
        {
          random = Math.random();
        }
        //inverse sigmoid so that (0,1) -> (-inf, +inf)
        random = Math.log(random/(1 - random));
        weightMatrixB[i][j] = random;
      }
    }
    for(int i = 0; i < NeuralNetworkManager.OUTPUT_ARRAY_SIZE; i++)
    {
      biasVectorC[i] = 0.0;
    }
    for(int i = 0; i < NeuralNetworkManager.OUTPUT_ARRAY_SIZE; i++)
    {
      for(int j = 0; j < NeuralNetworkManager.LAYER_ARRAY_SIZE; j++)
      {
        random = Math.random();
        //prevent 0 being picked
        while(random == 0)
        {
          random = Math.random();
        }
        //inverse sigmoid so that (0,1) -> (-inf, +inf)
        random = Math.log(random/(1 - random));
        weightMatrixC[i][j] = random;
      }
    }
    logger.log(Level.INFO, "network generated");
  }

  /**
   * sets the matrices and biases to the values stored in the txt files
   */
  private void loadValuesFromFiles()
  {
    double[] weightValues = fileManager.readValues();
    for(int i = 0; i < LAYER_ARRAY_SIZE; i++)
    {
      biasVectorA[i] = weightValues[i];
    }
    for(int i = 0; i < LAYER_ARRAY_SIZE; i++)
    {
      biasVectorB[i] = weightValues[LAYER_ARRAY_SIZE + i];
    }
    for(int i = 0; i < OUTPUT_ARRAY_SIZE; i++)
    {
      biasVectorC[i] = weightValues[(LAYER_ARRAY_SIZE + LAYER_ARRAY_SIZE) + i];
    }
    for(int i = 0; i < LAYER_ARRAY_SIZE; i++)
    {
      for(int j = 0; j < INPUT_ARRAY_SIZE; j++)
      {
        weightMatrixA[i][j] = weightValues[(LAYER_ARRAY_SIZE + LAYER_ARRAY_SIZE + OUTPUT_ARRAY_SIZE) + (i * LAYER_ARRAY_SIZE) + j];
      }
    }
    for(int i = 0; i < LAYER_ARRAY_SIZE; i++)
    {
      for(int j = 0; j < LAYER_ARRAY_SIZE; j++)
      {
        weightMatrixB[i][j] = weightValues[(LAYER_ARRAY_SIZE + LAYER_ARRAY_SIZE + OUTPUT_ARRAY_SIZE + (INPUT_ARRAY_SIZE * LAYER_ARRAY_SIZE)) + (i * LAYER_ARRAY_SIZE) + j];
      }
    }
    for(int i = 0; i < OUTPUT_ARRAY_SIZE; i++)
    {
      for(int j = 0; j < LAYER_ARRAY_SIZE; j++)
      {
        weightMatrixC[i][j] = weightValues[(LAYER_ARRAY_SIZE + LAYER_ARRAY_SIZE + OUTPUT_ARRAY_SIZE + (INPUT_ARRAY_SIZE * LAYER_ARRAY_SIZE) + (LAYER_ARRAY_SIZE * LAYER_ARRAY_SIZE)) + (i * LAYER_ARRAY_SIZE) + j];
      }
    }
  }

  /**
   * decodes the current matrix of pieces in the board to an array of ints, storing the colour and type of piece on each square, the column of the piece that can be taken en passant if one exists and any castling privileges
   * @param chessBoard the board containing all the information of the current state of the game
   */
  private void setInputArray(ChessBoard chessBoard)
  {
    inputArray = new int[INPUT_ARRAY_SIZE];
    int inputArrayIndex;
    for(int i = 0; i < ChessBoard.NO_OF_ROWS; i++)
    {
      for(int j = 0; j < ChessBoard.NO_OF_COLUMNS; j++)
      {
        if(chessBoard.getBoard()[i][j] != null)
        {
          inputArrayIndex = chessBoard.getBoard()[i][j].getPieceType().ordinal()
          + Piece.NO_OF_PIECE_TYPES * chessBoard.getBoard()[i][j].getColour().ordinal()
          + Piece.NO_OF_UNIQUE_PIECES * j
          + Piece.NO_OF_UNIQUE_PIECES * ChessBoard.NO_OF_COLUMNS * i;
          inputArray[inputArrayIndex] = 1;
          if(chessBoard.getBoard()[i][j] == chessBoard.getPieceThatCanBeTakenEnPassant())
          {
            inputArray[ChessBoard.NO_OF_SQUARES * Piece.NO_OF_UNIQUE_PIECES + j] = 1;
          }
        }
      }
    }

    if(chessBoard.getBoard()[0][4] != null && !chessBoard.getBoard()[0][4].getPieceHasMoved() && chessBoard.getBoard()[0][7] != null && !chessBoard.getBoard()[0][7].getPieceHasMoved())
    {
      inputArray[ChessBoard.NO_OF_SQUARES * Piece.NO_OF_UNIQUE_PIECES + ChessBoard.NO_OF_SQUARES_THE_CURRENT_EN_PASSANT_PIECE_CAN_BE_ON] = 1;
    }
    if(chessBoard.getBoard()[0][4] != null && !chessBoard.getBoard()[0][4].getPieceHasMoved() && chessBoard.getBoard()[0][0] != null && !chessBoard.getBoard()[0][0].getPieceHasMoved())
    {
      inputArray[ChessBoard.NO_OF_SQUARES * Piece.NO_OF_UNIQUE_PIECES + ChessBoard.NO_OF_SQUARES_THE_CURRENT_EN_PASSANT_PIECE_CAN_BE_ON + 1] = 1;
    }
    if(chessBoard.getBoard()[7][4] != null && !chessBoard.getBoard()[7][4].getPieceHasMoved() &&chessBoard.getBoard()[7][7] != null && !chessBoard.getBoard()[7][7].getPieceHasMoved())
    {
      inputArray[ChessBoard.NO_OF_SQUARES * Piece.NO_OF_UNIQUE_PIECES + ChessBoard.NO_OF_SQUARES_THE_CURRENT_EN_PASSANT_PIECE_CAN_BE_ON + 2] = 1;
    }
    if(chessBoard.getBoard()[7][4] != null && !chessBoard.getBoard()[7][4].getPieceHasMoved() && chessBoard.getBoard()[7][0] != null && !chessBoard.getBoard()[7][0].getPieceHasMoved())
    {
      inputArray[ChessBoard.NO_OF_SQUARES * Piece.NO_OF_UNIQUE_PIECES + ChessBoard.NO_OF_SQUARES_THE_CURRENT_EN_PASSANT_PIECE_CAN_BE_ON + 3] = 1;
    }
  }

  public int[] getCurrentInputArray()
  {
    return inputArray;
  }

  public int[][] getInputArrayMemory()
  {
    return inputArrayMemory;
  }


  /**
   * sets the output array to the calculated result of consecutive matrix multiplications and bias adjustments
   * @returns pieceInitialRow, pieceInitialColumn, pieceTargetRow, pieceTargetRow, promotionIndex
   * @param chessBoard the current board being played
   * @param illegalMoveIndexes which indexes of output array are to be set to 0
   */
  public int[] calculateNewOutputArray(ChessBoard chessBoard)
  {
    setInputArray(chessBoard);
    double totalSumOfOutputArray = 0;
    for(int i = 0; i < LAYER_ARRAY_SIZE; i++)
    {
      double rowTotal = 0;
      for(int j = 0; j < INPUT_ARRAY_SIZE; j++)
      {
        rowTotal += (weightMatrixA[i][j] * inputArray[j]);
      }
      layer1[i] = rowTotal + biasVectorA[i];
    }
    for(int i = 0; i < LAYER_ARRAY_SIZE; i++)
    {
      double rowTotal = 0;
      for(int j = 0; j < LAYER_ARRAY_SIZE; j++)
      {
        rowTotal += (weightMatrixB[i][j] * layer1[j]);
      }
      layer2[i] = rowTotal + biasVectorB[i];
    }
    for(int i = 0; i < OUTPUT_ARRAY_SIZE; i++)
    {
      double rowTotal = 0;
      for(int j = 0; j < LAYER_ARRAY_SIZE; j++)
      {
        rowTotal += (weightMatrixC[i][j] * layer2[j]);
      }
      outputArray[i] = rowTotal + biasVectorC[i];
      outputArray[i] = 1/(1 + (Math.exp(-outputArray[i]))); //sigmoid
      totalSumOfOutputArray += outputArray[i];
    }

    //generate a weighted, random index of the output array
    double rand = new Random().nextDouble() * totalSumOfOutputArray;
    System.out.println("rand = " + rand + "\ntotal sum of the output array = " + totalSumOfOutputArray);
    double sumOfOutputArray = 0;
    int index = -1;
    for(int i = 0; i < OUTPUT_ARRAY_SIZE; i++)
    {
      sumOfOutputArray += outputArray[i];
      if(rand < sumOfOutputArray)
      {
        index = i;
        break;
      }
    }
    if(index == -1)
    {
      logger.log(Level.INFO, "error in finding output index");
    }

    //decode the output array index into a move
    int[] move = new int[5];
    if(index < ChessBoard.NO_OF_SQUARES * ChessBoard.NO_OF_SQUARES)
    {
      move[4] = -1;
      move[3] = index / (ChessBoard.NO_OF_SQUARES * ChessBoard.NO_OF_ROWS); //pieceTargetColumn will be between 0 and 7
      index -= move[3] * ChessBoard.NO_OF_SQUARES * ChessBoard.NO_OF_ROWS; //max index can be is ChessBoard.NO_OF_SQUARES * ChessBoard.NO_OF_ROWS - 1
      move[2] = index / ChessBoard.NO_OF_SQUARES; //pieceTargetRow will be between 0 and 7
      index -= move[2] * ChessBoard.NO_OF_SQUARES; //max index can be is ChessBoard.NO_OF_SQUARES - 1
      move[1] = index / ChessBoard.NO_OF_ROWS; //pieceInitialColumn will be between 0 and 7
      index -= move[1] * ChessBoard.NO_OF_ROWS; //max index can be is ChessBoard.NO_OF_ROWS - 1
      move[0] = index; //pieceInitialRow will be between 0 and 7
    }
    else
    {
      move[0] = 6;
      move[2] = 7;
      index -= ChessBoard.NO_OF_SQUARES * ChessBoard.NO_OF_SQUARES;
      move[1] = index / Piece.NO_OF_VALID_WAYS_TO_PROMOTE; //pieceInitialColumn will be between 0 and 7
      index -= move[1] * Piece.NO_OF_VALID_WAYS_TO_PROMOTE; //max index can be is Piece.NO_OF_VALID_WAYS_TO_PROMOTE - 1
      move[3] = move[1] - 1 + index / 4; //index / 4 is between 0 and 2, so move[3] is between -1 and +1 of pieceInitialColumn
      index -= (index / 4) * 4;
      move[4] = index; //promotion index is between 0 and 3
    }
    return move;
  }

  public double[] getCurrentOutputArray()
  {
    return outputArray;
  }

  public double[][] getOutputArrayMemory()
  {
    return outputArrayMemory;
  }

  public int getCurrentSelectedOutputArrayIndex()
  {
    return currentSelectedOutputArrayIndex;
  }

  public int[] getSelectedIndexOfEachOutputArrayMemory()
  {
    return selectedIndexOfEachOutputArrayMemory;
  }

  /**
   * flag1 is set to true when the loop moves to a new input array in memory, and is set to false if there is a discrepancy between the current input array and the one in memory that its being compared to
   * flag2 is initialised as false and set to true once an initial match has been found
   * @return when flag1 and flag2 are both true after looping over an input array from memory
   */
  public boolean drawByThreepeatRepetition()
  {
    boolean flag1, flag2;
    flag2 = false;
    for(int i = 0; i < inputArrayMemory.length - 1; i++)
    {
      if((inputArrayMemory.length - 1) % 2 == i % 2)
      {
        flag1 = true;
        for(int j = 0; j < inputArray.length; j++)
        {
          if(inputArray[j] != inputArrayMemory[i][j])
          {
            flag1 = false;
            break;
          }
        }
        if(flag1 && !flag2)
        {
          flag2 = true;
        }
        else if(flag1 && flag2)
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * this method controls the back propagation of the network its in charge of
   * - moves that lose reduce the prio on only the selected move
   * - moves that draw dont lose or gain prio on any move
   * - moves that win gain prio on the selected move and lose prio on all other moves
   *
   * This means all other winning and drawing moves are less likely to be chosen, meaning that if there are several
   * winning moves from a position they will all reach some probability = 1/n, n being the number of winning moves, and
   * any drawing moves will be treated as losses as they are constantly deprioed by the winning moves.
   * Whereas if there are only drawing moves, all the other losing moves will continued to be deprioed and leave the
   * drawing moves as the only remaining moves with non negligible probability weights.
   * If the player can only find losing moves, then they
   * should keep generating different moves between games deprioing the most recent moves, eventually meaning the player
   * has tried every move from the positions it is given. This also means the other player can find a winning move from
   * every position that the player can create from the moves it makes.
   *
   * Notes about using this method:
   * - Most moves should have negligible probability weights after an infinite amount of trials.
   * - It doesnt rely on the network deprioing moves to the point that they are ignored (which can cause overfitting),
   * they are instead randomnly selected using the outputs generated as probability weights (i.e. they only have weight
   * relative to other outputs)
   * - The initial move will say a lot about whether white wins, draws or loses (unlikely) i.e.if white wins, there will
   * likely be one or a few opening moves it plays every game. This will be similar with a draw but there will probably
   * be more moves it plays because it is likely easier to draw than win from more positions, but if it loses it will
   * play a different opening move most games.
   *
   * 1) it determines if the result was a win or a loss (draws can be, and are therefore, ignored)
   * 2) finds the output, d cost / d output, inverse sigmoid of the output, d output / d inverse sig output and d cost / d inv sig output
   * 3) for each layer, starting from the top, finds d cost / d activation, d cost / d weights and d cost / d biases and calculates the sum magnitude of all d cost / d (weights and biases)
   * 4) change the values in each bias vector and weight matrice by adding (d cost / d that weight or bias) * (cost) / (magnitude of all d cost / d weights and biases)
   * 5) pass the new bias vectors and weight matrices to a method in the file manager to change the sotred values
   * @param playerWon 0 for a loss and 1 for win
   * @param outputArray the probability weights for every possible move, with priority on winning or, if not, drawing moves.
   * @param selectedOutputArrayIndex the move selected from the all possible moves
   * @param inputArray the game state from which the move is played
   */
  public void editWeightsAndBiases(boolean playerWon, double[] outputArray, int selectedOutputArrayIndex, int[] inputArray)
  {
    double cost = 0;
    double[] deltaCostByDeltaBiasVectorC = new double[OUTPUT_ARRAY_SIZE];
    double[] deltaCostByDeltaBiasVectorB = new double[LAYER_ARRAY_SIZE];
    double[] deltaCostByDeltaBiasVectorA = new double[LAYER_ARRAY_SIZE];
    double[][] deltaCostByDeltaWeightMatrixC = new double[OUTPUT_ARRAY_SIZE][LAYER_ARRAY_SIZE];
    double[][] deltaCostByDeltaWeightMatrixB = new double[LAYER_ARRAY_SIZE][LAYER_ARRAY_SIZE];
    double[][] deltaCostByDeltaWeightMatrixA = new double[LAYER_ARRAY_SIZE][INPUT_ARRAY_SIZE];
    double magnitudeOfAllDeltaWeightsAndBiases = 0;

    //if win
    if(playerWon)
    {
      double deltaCostByDeltaOutput[] = new double[OUTPUT_ARRAY_SIZE];
      double inverseSigmoidOfTheOutput[] = new double[OUTPUT_ARRAY_SIZE];
      double deltaOutputByDeltaInverseSigmoidOutput[] = new double[OUTPUT_ARRAY_SIZE];
      double deltaCostByDeltaInverseSigmoidOutput[] = new double[OUTPUT_ARRAY_SIZE];
      double totalCost = 0;

      //iterate over every move
      for(int i = 0; i < OUTPUT_ARRAY_SIZE; i++)
      {
        //check for the index of the selected move
        if(i == selectedOutputArrayIndex)
        {
          //for the index of the selected move, add the square of difference of 1 and the activation at that index, which is <= 1 and >= 0, to the total cost
          totalCost += Math.pow(1 - outputArray[i], 2);
          //therefore dC/dO for this index is <= 0 because a negative number x a positive number between 0 and 1 is a negative number
          //this means increasing the activation reduces the cost -> achieves the goal of reducing the cost
          deltaCostByDeltaOutput[i] = (-2) * (1 - outputArray[i]);
        }
        //for all other moves
        else
        {
          //for the index of all other moves, add the square of difference of 0 and the activation at that index, which is <= 1 and >= 0, to the total cost
          totalCost += Math.pow(outputArray[i], 2);
          //therefore dC/dO is >= 0 because a positive number x a positive number between 0 and 1 is a positive number
          //this means increasing the activation increases the cost -> reducing the activation reduces the cost -> achieves goal of reducing activation
          deltaCostByDeltaOutput[i] = 2 * outputArray[i];
        }

        //for every move you need to find dC/dS^-1(O) to be able to find the dC/(dW & dB) for each weight and bias
        //but first you need to know the inverse sigmoid of the output, i.e. the output before it is squashed, and dO/dS^-1(O) as intermediary values
        inverseSigmoidOfTheOutput[i] = -Math.log((1/outputArray[i]) - 1);
        deltaOutputByDeltaInverseSigmoidOutput[i] = Math.exp(-inverseSigmoidOfTheOutput[i])/Math.pow(1 + Math.exp(-inverseSigmoidOfTheOutput[i]), 2);
        deltaCostByDeltaInverseSigmoidOutput[i] = deltaCostByDeltaOutput[i] * deltaOutputByDeltaInverseSigmoidOutput[i];

        //now you can calculate the dC/dBc
        deltaCostByDeltaBiasVectorC[i] = deltaCostByDeltaInverseSigmoidOutput[i];
      }
      //normalise the cost to find the average cost per move
      cost = totalCost/OUTPUT_ARRAY_SIZE;

      //find delta cost by delta [layers, weights and biases] -> find magnitude of all weights and biases to normalise
      magnitudeOfAllDeltaWeightsAndBiases = Math.pow(deltaCostByDeltaBiasVectorC[selectedOutputArrayIndex], 2);
      for(int i = 0; i < LAYER_ARRAY_SIZE; i++) {
        //Layer 2 and matrix C
        deltaCostByDeltaLayer2Activation[i] = deltaCostByDeltaInverseSigmoidOutput * weightMatrixC[selectedOutputArrayIndex][i];
        deltaCostByDeltaWeightMatrixC[selectedOutputArrayIndex][i] = deltaCostByDeltaInverseSigmoidOutput * layer2[i];
        magnitudeOfAllDeltaWeightsAndBiases += Math.pow(deltaCostByDeltaWeightMatrixC[selectedOutputArrayIndex][i], 2);
      }
    }
    else //loss
    {
      double output = outputArray[selectedOutputArrayIndex];
      cost = output * output;
      double deltaCostByDeltaOutput = 2 * -output;
      double inverseSigmoidOfTheOutput = -Math.log((1/output) - 1);
      double deltaOutputByDeltaInverseSigmoidOutput = Math.exp(-inverseSigmoidOfTheOutput)/Math.pow(1 + Math.exp(-inverseSigmoidOfTheOutput), 2);
      double deltaCostByDeltaInverseSigmoidOutput = deltaCostByDeltaOutput * deltaOutputByDeltaInverseSigmoidOutput;
      //delta cost by delta bias vector C
      deltaCostByDeltaBiasVectorC[selectedOutputArrayIndex] = deltaCostByDeltaInverseSigmoidOutput;
      magnitudeOfAllDeltaWeightsAndBiases = Math.pow(deltaCostByDeltaBiasVectorC[selectedOutputArrayIndex], 2);
      for(int i = 0; i < LAYER_ARRAY_SIZE; i++)
      {
        //Layer 2 and matrix C
        deltaCostByDeltaLayer2Activation[i] = deltaCostByDeltaInverseSigmoidOutput * weightMatrixC[selectedOutputArrayIndex][i];
        deltaCostByDeltaWeightMatrixC[selectedOutputArrayIndex][i] = deltaCostByDeltaInverseSigmoidOutput * layer2[i];
        magnitudeOfAllDeltaWeightsAndBiases += Math.pow(deltaCostByDeltaWeightMatrixC[selectedOutputArrayIndex][i], 2);
      }
    }

    //1, B and A
    for(int i = 0; i < LAYER_ARRAY_SIZE; i++)
    {
      //Layer 1 and matrix and bias vector B
      deltaCostByDeltaBiasVectorB[i] = deltaCostByDeltaLayer2Activation[i];
      magnitudeOfAllDeltaWeightsAndBiases += Math.pow(deltaCostByDeltaBiasVectorB[i], 2);
      for(int j = 0; j < LAYER_ARRAY_SIZE; j++)
      {
        deltaCostByDeltaLayer1Activation[i] += deltaCostByDeltaLayer2Activation[j] * weightMatrixB[j][i];
        deltaCostByDeltaWeightMatrixB[j][i] = deltaCostByDeltaLayer2Activation[j] * layer1[i];
        magnitudeOfAllDeltaWeightsAndBiases += Math.pow(deltaCostByDeltaWeightMatrixB[j][i], 2);
      }

      //Matrix and bias vector A
      deltaCostByDeltaBiasVectorA[i] = deltaCostByDeltaLayer1Activation[i];
      magnitudeOfAllDeltaWeightsAndBiases += Math.pow(deltaCostByDeltaBiasVectorA[i], 2);
      for(int j = 0; j < INPUT_ARRAY_SIZE; j++)
      {
        deltaCostByDeltaWeightMatrixA[i][j] = deltaCostByDeltaLayer1Activation[i] * inputArray[j];
        magnitudeOfAllDeltaWeightsAndBiases += Math.pow(deltaCostByDeltaWeightMatrixA[i][j], 2);
      }
    }
    magnitudeOfAllDeltaWeightsAndBiases = Math.pow(magnitudeOfAllDeltaWeightsAndBiases, 0.5);

    //edit the bias vector and weight matrices
    double c = cost / magnitudeOfAllDeltaWeightsAndBiases;
    biasVectorC[selectedOutputArrayIndex] += deltaCostByDeltaBiasVectorC[selectedOutputArrayIndex] * c;
    for(int i = 0; i < LAYER_ARRAY_SIZE; i++)
    {
      weightMatrixC[selectedOutputArrayIndex][i] += deltaCostByDeltaWeightMatrixC[selectedOutputArrayIndex][i] * c;
      biasVectorB[i] += deltaCostByDeltaBiasVectorB[i] * c;
      biasVectorA[i] += (deltaCostByDeltaBiasVectorA[i] * c);
      for(int j = 0; j < LAYER_ARRAY_SIZE; j++)
      {
        weightMatrixB[i][j] += deltaCostByDeltaWeightMatrixB[i][j] * c;
      }
      for(int j = 0; j < INPUT_ARRAY_SIZE; j++)
      {
        weightMatrixA[i][j] += deltaCostByDeltaWeightMatrixA[i][j] * c;
      }
    }
    fileManager.editValues(biasVectorA, biasVectorB, biasVectorC, weightMatrixA, weightMatrixB, weightMatrixC);
  }

  /**
   * called if a move is legal after the move is played
   */
  public void addInputAndOutputArraysToMemory()
  {
    int[][] tempGameStateMemory = inputArrayMemory;
    double[][] tempMoveMemory = outputArrayMemory;
    inputArrayMemory = new int[inputArrayMemory.length + 1][INPUT_ARRAY_SIZE];
    outputArrayMemory = new double[outputArrayMemory.length + 1][OUTPUT_ARRAY_SIZE];
    for(int i = 0; i < tempGameStateMemory.length; i++)
    {
      inputArrayMemory[i] = tempGameStateMemory[i];
      outputArrayMemory[i] = tempMoveMemory[i];
    }
    inputArrayMemory[inputArrayMemory.length - 1] = inputArray;
    outputArrayMemory[outputArrayMemory.length - 1] = outputArray;
  }
}