# ChessAi
(started 04/2023 - finished --/----)

Functionality: It plays itself multiple times, using each game as data to change the matrices of weights and biases that determine the chance of a move being played, therefore improving after each game.

Setup: The Main file only contains the main method. The Game file contains the game loop and tests which can be called by main. The Chessboard file contains the means to hold the state of a board, and methods that can be used to manipualte and read information from a board. The Piece file contains the means to hold the state of a chess piece (which is mainly static), and methods that can be used to manipulate and read the information from a piece. The NeuralNetworkManager contains methods that can be used to caluculate the probabilities for a move to be played from a certain position and methods for backpropagation that will alter how these probabilities are calculated using multivariable calculus. The FileManager class contains methods that can be used to read and write to the files containing the weights and biases of a Neural Network.

