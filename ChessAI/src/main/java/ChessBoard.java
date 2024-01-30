import java.util.logging.Level;
import java.util.logging.Logger;
public class ChessBoard {
  Logger logger = Logger.getLogger(Main.class.getName());
  private Piece[][] board;
  public static final int NO_OF_ROWS = 8;
  public static final int NO_OF_COLUMNS = 8;
  public static final int NO_OF_SQUARES = NO_OF_ROWS * NO_OF_COLUMNS;
  public static final int NO_OF_SQUARES_THE_CURRENT_EN_PASSANT_PIECE_CAN_BE_ON = 8;
  public static final int NO_OF_SQUARES_TO_PROMOTE_FROM = 8;
  public static final int NO_OF_CASTLING_PRIVILEGES = 4;
  private Piece pieceThatCanBeTakenEnPassant;

  //White pieces
  private Piece wp1;
  private Piece wp2;
  private Piece wp3;
  private Piece wp4;
  private Piece wp5;
  private Piece wp6;
  private Piece wp7;
  private Piece wp8;
  private Piece wr1;
  private Piece wr2;
  private Piece wk1;
  private Piece wk2;
  private Piece wb1;
  private Piece wb2;
  private Piece wq1;

  //Black pieces
  private Piece wki;
  private Piece bp1;
  private Piece bp2;
  private Piece bp3;
  private Piece bp4;
  private Piece bp5;
  private Piece bp6;
  private Piece bp7;
  private Piece bp8;
  private Piece br1;
  private Piece br2;
  private Piece bk1;
  private Piece bk2;
  private Piece bb1;
  private Piece bb2;
  private Piece bq1;
  private Piece bki;

  /**
   * calls constructor on each piece and places them on the board
   */
  public ChessBoard()
  {
    pieceThatCanBeTakenEnPassant = null;

    //Create white pieces
    wp1 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.WHITE);
    wp2 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.WHITE);
    wp3 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.WHITE);
    wp4 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.WHITE);
    wp5 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.WHITE);
    wp6 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.WHITE);
    wp7 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.WHITE);
    wp8 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.WHITE);
    wr1 = new Piece(Piece.Piece_Type.ROOK, Piece.Colour.WHITE);
    wr2 = new Piece(Piece.Piece_Type.ROOK, Piece.Colour.WHITE);
    wk1 = new Piece(Piece.Piece_Type.KNIGHT, Piece.Colour.WHITE);
    wk2 = new Piece(Piece.Piece_Type.KNIGHT, Piece.Colour.WHITE);
    wb1 = new Piece(Piece.Piece_Type.BISHOP, Piece.Colour.WHITE);
    wb2 = new Piece(Piece.Piece_Type.BISHOP, Piece.Colour.WHITE);
    wq1 = new Piece(Piece.Piece_Type.QUEEN, Piece.Colour.WHITE);
    wki = new Piece(Piece.Piece_Type.KING, Piece.Colour.WHITE);

    //Create black pieces
    bp1 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.BLACK);
    bp2 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.BLACK);
    bp3 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.BLACK);
    bp4 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.BLACK);
    bp5 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.BLACK);
    bp6 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.BLACK);
    bp7 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.BLACK);
    bp8 = new Piece(Piece.Piece_Type.PAWN, Piece.Colour.BLACK);
    br1 = new Piece(Piece.Piece_Type.ROOK, Piece.Colour.BLACK);
    br2 = new Piece(Piece.Piece_Type.ROOK, Piece.Colour.BLACK);
    bk1 = new Piece(Piece.Piece_Type.KNIGHT, Piece.Colour.BLACK);
    bk2 = new Piece(Piece.Piece_Type.KNIGHT, Piece.Colour.BLACK);
    bb1 = new Piece(Piece.Piece_Type.BISHOP, Piece.Colour.BLACK);
    bb2 = new Piece(Piece.Piece_Type.BISHOP, Piece.Colour.BLACK);
    bq1 = new Piece(Piece.Piece_Type.QUEEN, Piece.Colour.BLACK);
    bki = new Piece(Piece.Piece_Type.KING, Piece.Colour.BLACK);

    //Set their initial positions
    board = new Piece[][]{
        {wr1 , wk1 , wb1 , wq1 , wki , wb2 , wk2 , wr2 },
        {wp1 , wp2 , wp3 , wp4 , wp5 , wp6 , wp7 , wp8 },
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {bp1 , bp2 , bp3 , bp4 , bp5 , bp6 , bp7 , bp8 },
        {br1 , bk1 , bb1 , bq1 , bki , bb2 , bk2 , br2 },
    };
    logger.log(Level.INFO, "chessboard initialised");
    printBoard();
  }

  public Piece[][] getBoard()
  {
    return board;
  }

  /**
   * completely changes the board to the state specified in the method
   */
  public void setupTestBoard1()
  {
    board = new Piece[][]{
        {null, null, null, null, wq1 , wb2 , null, wr2 },
        {null, null, wp3 , br1 , wki , null, wp7 , wp8 },
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, wp6 , bk1 },
        {null, null, null, null, null, null, bp6 , null},
        {null, null, null, null, null, null, null, null},
        {null, null, bp3 , null, bki , null, wk1 , bp8 },
        {null, null, null, wr1 , bq1 , bb2 , null, br2 },
    };
    printBoard();
  }

  /**
   * Prints the board to the console
   */
  public void printBoard()
  {
    System.out.println("-------------------------------------------------");
    for(int i = NO_OF_ROWS - 1; i > -1; i--)
    {
      for(int j = 0; j < NO_OF_COLUMNS; j++)
      {
        System.out.print("| ");
        if(board[i][j] != null)
        {
          if(board[i][j].getColour() == Piece.Colour.BLACK)
          {
            System.out.print("b");
          }
          else
          {
            System.out.print("w");
          }

          if(board[i][j].getPieceType() == Piece.Piece_Type.PAWN)
          {
            System.out.print("pn ");
          }
          if(board[i][j].getPieceType() == Piece.Piece_Type.ROOK)
          {
            System.out.print("rk ");
          }
          if(board[i][j].getPieceType() == Piece.Piece_Type.KNIGHT)
          {
            System.out.print("kn ");
          }
          if(board[i][j].getPieceType() == Piece.Piece_Type.BISHOP)
          {
            System.out.print("bp ");
          }
          if(board[i][j].getPieceType() == Piece.Piece_Type.QUEEN)
          {
            System.out.print("qu ");
          }
          if(board[i][j].getPieceType() == Piece.Piece_Type.KING)
          {
            System.out.print("ki ");
          }
        }
        else
        {
          System.out.print("    ");
        }
      }
      System.out.println("|\n-------------------------------------------------");
    }
  }

  public void printBoard(Piece[][] board)
  {
    System.out.println("-------------------------------------------------");
    for(int i = NO_OF_ROWS - 1; i > -1; i--)
    {
      for(int j = 0; j < NO_OF_COLUMNS; j++)
      {
        System.out.print("| ");
        if(board[i][j] != null)
        {
          if(board[i][j].getColour() == Piece.Colour.BLACK)
          {
            System.out.print("b");
          }
          else
          {
            System.out.print("w");
          }

          if(board[i][j].getPieceType() == Piece.Piece_Type.PAWN)
          {
            System.out.print("pn ");
          }
          if(board[i][j].getPieceType() == Piece.Piece_Type.ROOK)
          {
            System.out.print("rk ");
          }
          if(board[i][j].getPieceType() == Piece.Piece_Type.KNIGHT)
          {
            System.out.print("kn ");
          }
          if(board[i][j].getPieceType() == Piece.Piece_Type.BISHOP)
          {
            System.out.print("bp ");
          }
          if(board[i][j].getPieceType() == Piece.Piece_Type.QUEEN)
          {
            System.out.print("qu ");
          }
          if(board[i][j].getPieceType() == Piece.Piece_Type.KING)
          {
            System.out.print("ki ");
          }
        }
        else
        {
          System.out.print("    ");
        }
      }
      System.out.println("|\n-------------------------------------------------");
    }
  }

  /**
   * @return a temporary version of the current board altered by one move
   * @param pieceInitialRow row number of the position that will be vacated
   * @param pieceInitialColumn column number of the piece that will be vacated
   * @param pieceTargetRow row number of the position that the new piece will occupy
   * @param pieceTargetColumn column number of the position the new piece will occupy
   */
  public Piece[][] tempBoard(int pieceInitialRow, int pieceInitialColumn, int pieceTargetRow, int pieceTargetColumn)
  {
    Piece[][] tempBoard = new Piece[8][8];

    for(int i = 0; i < NO_OF_ROWS; i++)
    {
      for(int j = 0; j < NO_OF_COLUMNS; j++)
      {
        if(board[i][j] != null)
        {
          tempBoard[i][j] = board[i][j];
        }
      }
    }
    tempBoard[pieceTargetRow][pieceTargetColumn] = tempBoard[pieceInitialRow][pieceInitialColumn];
    tempBoard[pieceInitialRow][pieceInitialColumn] = null;
    return tempBoard;
  }

  /**
   * flips the board around (swap every pieces colour and inverts their positions across the horizontal center line of the board)
   * called by game every turn so that the neural network can make every move from whites perspective
   * calling this twice is the same as no times
   */
  public void flipBoard()
  {
    Piece[][] tempBoard = new Piece[8][8];
    for(int i = 0; i < NO_OF_ROWS; i++)
    {
      for(int j = 0; j < NO_OF_COLUMNS; j++)
      {
        if(board[i][j] != null && board[i][j].getColour() == Piece.Colour.WHITE)
        {
          tempBoard[(NO_OF_ROWS - 1) - i][j] = board[i][j];
          tempBoard[(NO_OF_ROWS - 1) - i][j].setColour(Piece.Colour.BLACK);
        }
        else if(board[i][j] != null && board[i][j].getColour() == Piece.Colour.BLACK)
        {
          tempBoard[(NO_OF_ROWS - 1) - i][j] = board[i][j];
          tempBoard[(NO_OF_ROWS - 1) - i][j].setColour(Piece.Colour.WHITE);
        }
      }
    }
    this.board = tempBoard;
  }

  /**
   * updates the board with the new move applied
   * @param pieceInitialRow row number of the position that will be vacated
   * @param pieceInitialColumn column number of the piece that will be vacated
   * @param pieceTargetRow row number of the position that the new piece will occupy
   * @param pieceTargetColumn column number of the position the new piece will occupy
   * @param promotionIndex specifies the piece type the pawn will promote to if it will reach the last rank this turn
   */
  public void updateBoard(int pieceInitialRow, int pieceInitialColumn, int pieceTargetRow, int pieceTargetColumn, int promotionIndex)
  {
    board[pieceTargetRow][pieceTargetColumn] = board[pieceInitialRow][pieceInitialColumn];
    board[pieceTargetRow][pieceTargetColumn].promote(promotionIndex);
    board[pieceTargetRow][pieceTargetColumn].setPieceHasMovedToTrue();
    board[pieceInitialRow][pieceInitialColumn] = null;

    if (board[pieceTargetRow][pieceTargetColumn].getPieceType() == Piece.Piece_Type.PAWN &&
        ((pieceInitialColumn != 0 && board[pieceInitialRow][pieceInitialColumn - 1] == pieceThatCanBeTakenEnPassant) ||
        (pieceInitialColumn != 7 && board[pieceInitialRow][pieceInitialColumn + 1] == pieceThatCanBeTakenEnPassant)) &&
        board[pieceTargetRow - 1][pieceTargetColumn] == pieceThatCanBeTakenEnPassant &&
        pieceThatCanBeTakenEnPassant != null)
    {
      board[pieceTargetRow - 1][pieceTargetColumn] = null;
    }
    pieceThatCanBeTakenEnPassant = null;
    if (board[pieceTargetRow][pieceTargetColumn].getPieceType() == Piece.Piece_Type.PAWN &&
        (pieceInitialRow - pieceTargetRow == 2 || pieceInitialRow - pieceTargetRow == -2))
    {
      pieceThatCanBeTakenEnPassant = board[pieceTargetRow][pieceTargetColumn];
    }
    if (board[pieceTargetRow][pieceTargetColumn].getPieceType() == Piece.Piece_Type.KING && (pieceInitialColumn - pieceTargetColumn) == 2)
    {
      board[pieceTargetRow][pieceTargetColumn + 1] = board[pieceTargetRow][pieceTargetColumn - 2];
      board[pieceTargetRow][pieceTargetColumn + 1].setPieceHasMovedToTrue();
      board[pieceTargetRow][pieceTargetColumn - 2] = null;
    }
    if (board[pieceTargetRow][pieceTargetColumn].getPieceType() == Piece.Piece_Type.KING && (pieceInitialColumn - pieceTargetColumn) == -2)
    {
      board[pieceTargetRow][pieceTargetColumn - 1] = board[pieceTargetRow][pieceTargetColumn + 1];
      board[pieceTargetRow][pieceTargetColumn - 1].setPieceHasMovedToTrue();
      board[pieceTargetRow][pieceTargetColumn + 1] = null;
    }
  }

  /**
   * always check the move is legal before updating the board
   * @return if the move is legal
   * @param pieceColour colour of the piece being attempted to be moved
   * @param pieceInitialRow row number of the piece being attempted to be moved
   * @param pieceInitialColumn column number of the piece being attempted to be moved
   * @param pieceTargetRow row number of the position the piece is trying to reach
   * @param pieceTargetColumn column number of the position the piece is trying to reach
   * @param promotionIndex 0 = rook; 1 = knight; 2 = bishop; 3 = queen; anything else is not a promotion attempt (e.g. -1)
   */
  public boolean isMoveLegal(int pieceInitialRow, int pieceInitialColumn, int pieceTargetRow, int pieceTargetColumn, int promotionIndex)
  {
    if(pieceInitialColumn < 0 || pieceInitialColumn > 7 || pieceInitialRow < 0 || pieceInitialRow > 7 ||
        pieceTargetColumn < 0 || pieceTargetColumn > 7 || pieceTargetRow < 0 || pieceTargetRow > 7)
    {
      return false;
    }
    if(board[pieceInitialRow][pieceInitialColumn] == null)
    {
      return false;
    }
    if(board[pieceInitialRow][pieceInitialColumn].getColour() != Piece.Colour.WHITE)
    {
      return false;
    }
    if(board[pieceTargetRow][pieceTargetColumn] != null && board[pieceTargetRow][pieceTargetColumn].getColour() == Piece.Colour.WHITE)
    {
      return false;
    }
    if(checkCheck(tempBoard(pieceInitialRow, pieceInitialColumn, pieceTargetRow, pieceTargetColumn)))
    {
      return false;
    }
    if(board[pieceInitialRow][pieceInitialColumn].getPieceType() == Piece.Piece_Type.PAWN)
    {
      if (pieceTargetRow == 7 && (promotionIndex < 0 || promotionIndex > 3))
      {
        return false;
      }
      if (board[pieceTargetRow][pieceTargetColumn] == null && pieceInitialColumn == pieceTargetColumn &&
          (pieceInitialRow + 1 == pieceTargetRow ||
          (pieceInitialRow == 1 && board[2][pieceTargetColumn] == null && pieceTargetRow == 3)))
      {
        return true;
      }
      if ((pieceInitialColumn + 1 == pieceTargetColumn || pieceInitialColumn - 1 == pieceTargetColumn) &&
          pieceInitialRow + 1 == pieceTargetRow && board[pieceTargetRow][pieceTargetColumn] != null &&
          board[pieceTargetRow][pieceTargetColumn].getColour() == Piece.Colour.BLACK)
      {
        return true;
      }
      if ((pieceInitialColumn + 1 == pieceTargetColumn || pieceInitialColumn - 1 == pieceTargetColumn) &&
          pieceInitialRow + 1 == pieceTargetRow && board[pieceInitialRow][pieceTargetColumn] != null &&
          board[pieceInitialRow][pieceTargetColumn] == pieceThatCanBeTakenEnPassant &&
          board[pieceInitialRow][pieceTargetColumn].getColour() == Piece.Colour.BLACK)
      {
        return true;
      }
      return false;
    }
    if(board[pieceInitialRow][pieceInitialColumn].getPieceType() == Piece.Piece_Type.ROOK)
    {
      if(squareWithinRangeOfRook(board, pieceInitialRow, pieceInitialColumn, pieceTargetRow, pieceTargetColumn))
      {
        return true;
      }
      return false;
    }
    if(board[pieceInitialRow][pieceInitialColumn].getPieceType() == Piece.Piece_Type.KNIGHT)
    {
      if(((pieceInitialColumn - pieceTargetColumn == 2 || pieceInitialColumn - pieceTargetColumn == -2) &&
          (pieceInitialRow - pieceTargetRow == 1 || pieceInitialRow - pieceTargetRow == -1)) ||
          ((pieceInitialColumn - pieceTargetColumn == 1 || pieceInitialColumn - pieceTargetColumn == -1) &&
              (pieceInitialRow - pieceTargetRow == 2 || pieceInitialRow - pieceTargetRow == -2)))
      {
        return true;
      }
      return false;
    }
    if(board[pieceInitialRow][pieceInitialColumn].getPieceType() == Piece.Piece_Type.BISHOP)
    {
      if(squareWithinRangeOfBishop(board, pieceInitialRow, pieceInitialColumn, pieceTargetRow, pieceTargetColumn))
      {
        return true;
      }
      return false;
    }
    if(board[pieceInitialRow][pieceInitialColumn].getPieceType() == Piece.Piece_Type.QUEEN)
    {
      if(squareWithinRangeOfBishop(board, pieceInitialRow, pieceInitialColumn, pieceTargetRow, pieceTargetColumn) ||
         squareWithinRangeOfRook(board, pieceInitialRow, pieceInitialColumn, pieceTargetRow, pieceTargetColumn))
      {
        return true;
      }
      return false;
    }
    if(board[pieceInitialRow][pieceInitialColumn].getPieceType() == Piece.Piece_Type.KING)
    {
      if(pieceInitialColumn - pieceTargetColumn >= -1 && pieceInitialColumn - pieceTargetColumn <= 1 &&
        pieceInitialRow - pieceTargetRow >= -1 && pieceInitialRow - pieceTargetRow <= 1)
      {
        return true;
      }
      if(!checkCheck(board) && !board[pieceInitialRow][pieceInitialColumn].getPieceHasMoved() && pieceTargetRow == 0)
      {
        if(canCastleKingside() && pieceTargetColumn == 6)
        {
          return true;
        }
        if(canCastleQueenside() && pieceTargetColumn == 2)
        {
          return true;
        }
      }
      return false;
    }
    logger.log(Level.SEVERE, "cant determine if the move is legal");
    return false;
  }

  /**
   * @return if the specified king is in check
   * @param change colour of the king who is to be determined as in check or not in check
   * @param board the board to which this method is applied
   */
  public boolean checkCheck(Piece[][] board)
  {
    int[] kingPosition = findKingPosition(board);
    for(int i = 0; i < NO_OF_ROWS; i++)
    {
      for(int j = 0; j < NO_OF_COLUMNS; j++)
      {
        if(board[i][j] != null && board[i][j].getColour() == Piece.Colour.BLACK)
        {
          if(board[i][j].getPieceType() == Piece.Piece_Type.PAWN &&
              (j + 1 == kingPosition[1] || j - 1 == kingPosition[1]) && i - 1 == kingPosition[0])
          {
            return true;
          }
          if((board[i][j].getPieceType() == Piece.Piece_Type.ROOK ||
              board[i][j].getPieceType() == Piece.Piece_Type.QUEEN) &&
              squareWithinRangeOfRook(board, kingPosition[0], kingPosition[1], i, j))
          {
            return true;
          }
          if(board[i][j].getPieceType() == Piece.Piece_Type.KNIGHT &&
              (((i - kingPosition[0]) == 2 || (i - kingPosition[0]) == -2) &&
              ((j - kingPosition[1]) == 1 || (j - kingPosition[1] == -1))) ||
              (((i - kingPosition[0]) == 1 || (i - kingPosition[0]) == -1)) &&
              ((j - kingPosition[1]) == 2 || (j - kingPosition[1]) == -2))
          {
            return true;
          }
          if((board[i][j].getPieceType() == Piece.Piece_Type.BISHOP ||
              board[i][j].getPieceType() == Piece.Piece_Type.QUEEN) &&
              squareWithinRangeOfBishop(board, kingPosition[0], kingPosition[1], i, j))
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * @return position of king of specified colour
   * @param kingColour colour of the king whose position is to be located
   */
  private int[] findKingPosition(Piece[][] board)
  {
    for(int i = 0; i < NO_OF_ROWS; i++)
    {
      for(int j = 0; j < NO_OF_COLUMNS; j++)
      {
        if(board[i][j] != null && board[i][j].getPieceType() == Piece.Piece_Type.KING && board[i][j].getColour() == Piece.Colour.WHITE)
        {
          return new int[]{i,j};
        }
      }
    }
    return new int[2];
  }

  /**
   * @return if the bishop can reach the specified square from its current position
   * @param pieceInitialRow row number of the bishops current position
   * @param pieceInitialColumn column number of the bishops current position
   * @param pieceTargetRow row number of the position the bishop is trying to reach
   * @param pieceTargetColumn column number of the position the bishop is trying to reach
   *
   */
  private boolean squareWithinRangeOfBishop(Piece[][] board, int pieceInitialRow, int pieceInitialColumn, int pieceTargetRow, int pieceTargetColumn)
  {
    if((pieceInitialColumn - pieceTargetColumn == pieceInitialRow - pieceTargetRow) && pieceInitialRow < pieceTargetRow)
    {
      for(int i = 1; i < (pieceTargetRow - pieceInitialRow); i++)
      {
        if(board[pieceInitialRow + i][pieceInitialColumn + i] != null)
        {
          return false;
        }
      }
      return true;
    }
    if((pieceInitialColumn - pieceTargetColumn == pieceInitialRow - pieceTargetRow) && pieceInitialRow > pieceTargetRow)
    {
      for(int i = 1; i < (pieceInitialRow - pieceTargetRow); i++)
      {
        if(board[pieceInitialRow - i][pieceInitialColumn - i] != null)
        {
          return false;
        }
      }
      return true;
    }
    if((pieceInitialColumn - pieceTargetColumn == -(pieceInitialRow - pieceTargetRow)) && pieceInitialRow < pieceTargetRow)
    {
      for(int i = 1; i < (pieceTargetRow - pieceInitialRow); i++)
      {
        if(board[pieceInitialRow + i][pieceInitialColumn - i] != null)
        {
          return false;
        }
      }
      return true;
    }
    if((pieceInitialColumn - pieceTargetColumn == -(pieceInitialRow - pieceTargetRow)) && pieceInitialRow > pieceTargetRow)
    {
      for(int i = 1; i < (pieceInitialRow - pieceTargetRow); i++)
      {
        if(board[pieceInitialRow - i][pieceInitialColumn + i] != null)
        {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * @return if the rook can reach the specified square from its current position
   * @param pieceInitialRow row number of the rooks current position
   * @param pieceInitialColumn column number of the rooks current position
   * @param pieceTargetRow row number of the position the rook is trying to reach
   * @param pieceTargetColumn column number of the position the rook is trying to reach
   */
  private boolean squareWithinRangeOfRook(Piece[][] board, int pieceInitialRow, int pieceInitialColumn, int pieceTargetRow, int pieceTargetColumn)
  {
    if(pieceInitialColumn == pieceTargetColumn && pieceInitialRow < pieceTargetRow)
    {
      for(int i = pieceInitialRow + 1; i < pieceTargetRow; i++)
      {
        if(board[i][pieceInitialColumn] != null)
        {
          return false;
        }
      }
      return true;
    }
    if(pieceInitialColumn == pieceTargetColumn && pieceInitialRow > pieceTargetRow)
    {
      for(int i = pieceTargetRow + 1; i < pieceInitialRow; i++)
      {
        if(board[i][pieceInitialColumn] != null)
        {
          return false;
        }
      }
      return true;
    }
    if(pieceInitialRow == pieceTargetRow && pieceInitialColumn < pieceTargetColumn)
    {
      for(int j = pieceInitialColumn + 1; j < pieceTargetColumn; j++)
      {
        if(board[pieceInitialRow][j] != null)
        {
          return false;
        }
      }
      return true;
    }
    if(pieceInitialRow == pieceTargetRow && pieceInitialColumn > pieceTargetColumn)
    {
      for(int j = pieceTargetColumn + 1; j < pieceInitialColumn; j++)
      {
        if(board[pieceInitialRow][j] != null)
        {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * @return if the king of the specified colour can castle queenside
   * @param kingColour the colour of the king that is trying to castle queenside
   */
  public boolean canCastleQueenside()
  {
    if(board[0][0] == null || board[0][0].getPieceHasMoved())
    {
      return false;
    }
    if(board[0][1] != null || board[0][2] != null || board[0][3] != null)
    {
      return false;
    }
    if(checkCheck(tempBoard(0, 4, 0, 2)) ||
        checkCheck(tempBoard(0, 4, 0, 3)))
    {
      return false;
    }
    return true;
  }

  /**
   * @return if the king of the specified colour can castle kingside
   * @param kingColour the colour of the king that is trying to castle kingside
   */
  public boolean canCastleKingside()
  {
    if(board[0][7] == null || board[0][7].getPieceHasMoved())
    {
      return false;
    }
    if(board[0][5] != null || board[0][6] != null)
    {
      return false;
    }
    if(checkCheck(tempBoard(0, 4, 0, 5)) ||
        checkCheck(tempBoard(0, 4, 0, 6)))
    {
      return false;
    }
    return true;
  }

  /**
   * @return if opposite colour player doesn't have any legal moves
   */
  public boolean oppositeColourKingIsInStalemateOrCheckmate()
  {
    flipBoard();
    for (int pI = -1; pI < 1; pI++) //only need to check one non promoting pi and one promoting pi
    {
      for (int i1 = 0; i1 < NO_OF_ROWS; i1++)
      {
        for (int j1 = 0; j1 < NO_OF_COLUMNS; j1++)
        {
          if (board[i1][j1] != null && board[i1][j1].getColour() == Piece.Colour.WHITE)
          {
            for (int i2 = 0; i2 < NO_OF_ROWS; i2++)
            {
              for (int j2 = 0; j2 < NO_OF_COLUMNS; j2++)
              {
                if (isMoveLegal(i1, j1, i2, j2, pI))
                {
                  flipBoard();
                  return false;
                }
              }
            }
          }
        }
      }
    }
    printBoard();
    flipBoard();
    return true;
  }

  /**
   * @return if there is insufficient material for checkmate on both sides
   */
  public boolean thereIsInsufficientMaterial()
  {
    int whiteKnightCounter = 0;
    int blackKnightCounter = 0;
    int whiteWhiteSquaredBishopCounter = 0;
    int whiteBlackSquaredBishopCounter = 0;
    int blackWhiteSquaredBishopCounter = 0;
    int blackBlackSquaredBishopCounter = 0;
    for(int i = 0; i < NO_OF_ROWS; i++)
    {
      for(int j = 0; j < NO_OF_COLUMNS; j++)
      {
        if(board[i][j] != null && (board[i][j].getPieceType() == Piece.Piece_Type.PAWN || board[i][j].getPieceType() == Piece.Piece_Type.ROOK ||
            board[i][j].getPieceType() == Piece.Piece_Type.QUEEN))
        {
          return false;
        }
        else if(board[i][j] != null && board[i][j].getPieceType() == Piece.Piece_Type.KNIGHT && board[i][j].getColour() == Piece.Colour.WHITE)
        {
          whiteKnightCounter++;
        }
        else if(board[i][j] != null && board[i][j].getPieceType() == Piece.Piece_Type.KNIGHT && board[i][j].getColour() == Piece.Colour.BLACK)
        {
          blackKnightCounter++;
        }
        else if(board[i][j] != null && board[i][j].getPieceType() == Piece.Piece_Type.BISHOP && board[i][j].getColour() == Piece.Colour.WHITE)
        {
          if((i + j) % 2 == 0)
          {
            whiteWhiteSquaredBishopCounter++;
          }
          else
          {
            whiteBlackSquaredBishopCounter++;
          }
        }
        else if(board[i][j] != null && board[i][j].getPieceType() == Piece.Piece_Type.BISHOP && board[i][j].getColour() == Piece.Colour.BLACK)
        {
          if((i + j) % 2 == 0)
          {
            blackWhiteSquaredBishopCounter++;
          }
          else
          {
            blackBlackSquaredBishopCounter++;
          }
        }
        if(whiteKnightCounter >= 2 || blackKnightCounter >= 2 ||
            (whiteKnightCounter == 1 && (whiteWhiteSquaredBishopCounter >= 1 || whiteBlackSquaredBishopCounter >= 1)) ||
            (blackKnightCounter == 1 && (blackWhiteSquaredBishopCounter >= 1 || blackBlackSquaredBishopCounter >= 1)))
        {
          return false;
        }
        if((whiteWhiteSquaredBishopCounter >= 1 && whiteBlackSquaredBishopCounter>= 1) ||
            (blackWhiteSquaredBishopCounter >= 1 && blackBlackSquaredBishopCounter>= 1))
        {
          return false;
        }
      }
    }
    return true;
  }

  public Piece getPieceThatCanBeTakenEnPassant()
  {
    return pieceThatCanBeTakenEnPassant;
  }
}