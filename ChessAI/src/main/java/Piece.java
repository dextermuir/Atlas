public class Piece {
  public enum Piece_Type { PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING }
  public enum Colour { WHITE, BLACK }
  public static final int NO_OF_PIECE_TYPES = 6;
  public static final int NO_OF_COLOURS = 2;
  public static final int NO_OF_UNIQUE_PIECES = NO_OF_COLOURS * NO_OF_PIECE_TYPES;
  public static final int NO_OF_VALID_WAYS_TO_PROMOTE = 12; // 1-4 are the pieces that can be promoted to, every multiple of four goes from left diagonal to right diagonal
  private Piece_Type pieceType;
  private Colour colour;
  private boolean pieceHasMoved;
  public Piece(Piece_Type pieceType, Colour colour)
  {
    pieceHasMoved = false;
    this.pieceType = pieceType;
    this.colour = colour;
  }
  public Piece_Type getPieceType()
  {
    return pieceType;
  }
  public Colour getColour()
  {
    return colour;
  }
  public void setColour(Colour colour)
  {
    this.colour = colour;
  }
  public boolean getPieceHasMoved()
  {
    return pieceHasMoved;
  }
  public void setPieceHasMovedToTrue()
  {
    pieceHasMoved = true;
  }

  /**
   * called every board update
   * the index is checked by is move legal
   * promotions count as separate from normal moves
   * @param promotionIndex index for the piece type that the promoting pawn will be set as
   */
  public void promote(int promotionIndex)
  {
    if(promotionIndex == 0)
    {
      pieceType = Piece_Type.ROOK;
    }
    if(promotionIndex == 1)
    {
      pieceType = Piece_Type.KNIGHT;
    }
    if(promotionIndex == 2)
    {
      pieceType = Piece_Type.BISHOP;
    }
    if(promotionIndex == 3)
    {
      pieceType = Piece_Type.QUEEN;
    }
  }
}
