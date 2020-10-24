from texttable import Texttable


class Board:  # domain
    def __init__(self):
        # empty square - 0
        # X - 1
        # 0 - -1
        self.size = 3
        self._data = [0] * self.size ** 2
        self._moves = 0

    def get(self, x, y):
        return self._data[x * self.size + y]

    def won(self):
        for i in range(0, len(self._data), self.size):
            row = self._data[self.size * i:self.size * i + self.size]
            col = self._data[i:len(self._data):self.size]
            if abs(sum(row)) == self.size or abs(sum(col)) == self.size:
                return True
        d = self._data
        x = 0
        y = 0
        s = 0
        for i in range(self.size):
            s += d[self.size * x + y]
            x += 1
            y += 1
        if abs(s) == self.size:
            return True
        x = 0
        y = self.size - 1
        s = 0
        for i in range(self.size):
            s += d[self.size * x + y]
            x += 1
            y -= 1
        if abs(s) == self.size:
            return True
        return False
        # 0, 1, 2
        # 3, 4, 5
        # 6, 7, 8

    def tie(self):
        return self._moves == len(self._data) and self.won()

    def move(self, x, y, symbol):
        # x,y in [0,1,2], symbol in [X,O]
        if x not in range(self.size) or y not in range(self.size):
            raise ValueError("Move not inside the board!")
        if self._data[self.size * x + y] != 0:
            raise ValueError("Square is already taken!")
        if symbol not in ['X', 'O']:
            raise ValueError("Bad symbol!")
        d = {'X': 1, 'O': -1}
        self._moves += 1
        self._data[self.size * x + y] = d[symbol]

    def __str__(self):
        t = Texttable()
        d = {-1: 'O', 0: ' ', 1: 'X'}
        for i in range(0, len(self._data), self.size):
            row = self._data[i:i + self.size]
            t.add_row([d[x] for x in row])
        return t.draw()


# decide the computer's next move
class SimpletonComputer:  # a kind of computer player
    @staticmethod
    def calculate_move(board):
        for i in range(board.size):
            for j in range(board.size):
                if board.get(i, j) == 0:
                    return i, j
        raise ValueError("Board is full!")


class Game:  # a kind of controller/service
    def __init__(self, board, computer):
        self._board = board
        self._computer = computer

    @property
    def board(self):
        return self._board

    def player_move(self, x, y):
        self._board.move(x, y, 'X')

    def computer_move(self):
        move = self._computer.calculate_move(self._board)
        # Computer must generate valid moves only
        # this should raise no exceptions
        self._board.move(move[0], move[1], 'O')


class UI:
    def __init__(self, game):
        self._game = game

    @staticmethod
    def _read_player_move():
        # Return the (x,y) tuple that represents the player's move
        # > 1 2
        while True:
            try:
                cmd = input("> ").split(" ")
                if len(cmd) > 2:
                    raise ValueError("Too many coordinates!")
                return int(cmd[0]), int(cmd[1])
            except Exception as e:
                print(e)

    def start(self):
        b = self._game.board
        player_move = True
        while True:
            # while condition must be checked after each move
            if player_move:
                print(b)
                try:
                    move = self._read_player_move()
                    self._game.player_move(move[0], move[1])
                except Exception as e:
                    print(e)
                    continue
            else:
                self._game.computer_move()
            if b.won():
                if player_move:
                    print("Congrats, you won!")
                    print(b)
                else:
                    print("Better luck next time!")
                    print(b)
                break
            if b.tie():
                print("Tie")
                print(b)
                break
            player_move = not player_move


UI(Game(Board(), SimpletonComputer())).start()
