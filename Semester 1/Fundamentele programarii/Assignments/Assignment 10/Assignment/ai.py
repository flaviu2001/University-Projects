import random
from exceptions import *
from constants import *


class EasyAI:  # Random hits
    def __init__(self, board):
        self._board = board

    def move(self):
        """
        Calculates the next move given the state of the board
        :return: The pair of coordinates
        """
        pair = (random.randrange(self._board.height), random.randrange(self._board.width))
        while self._board[pair] not in (SHIP, EMPTY):
            pair = (random.randrange(self._board.height), random.randrange(self._board.width))
        return pair


class NormalAI:  # Hunt and target+parity
    def __init__(self, board):
        self._board = board

    def _hunt(self):
        pair = (random.randrange(self._board.height), random.randrange(self._board.width))
        while self._board[pair] not in (SHIP, EMPTY) or (pair[0]+pair[1]) % 2 == 1:
            pair = (random.randrange(self._board.height), random.randrange(self._board.width))
        return pair

    @staticmethod
    def left(pair):
        return pair[0], pair[1] - 1

    @staticmethod
    def right(pair):
        return pair[0], pair[1] + 1

    @staticmethod
    def up(pair):
        return pair[0] - 1, pair[1]

    @staticmethod
    def down(pair):
        return pair[0] + 1, pair[1]

    def valid(self, pair):
        return pair[0] in range(self._board.height) and pair[1] in range(self._board.width)

    def _target(self, cell):
        horizontal = True
        vertical = True

        def try_direction(direction, pair):
            next_cell = direction(pair)
            while self.valid(next_cell) and self._board[next_cell] == HIT:
                next_cell = direction(next_cell)
            if self.valid(next_cell) and self._board[next_cell] in (SHIP, EMPTY):
                return next_cell

        def do_horizontal():
            if _ := try_direction(self.left, cell):
                return _
            if _ := try_direction(self.right, cell):
                return _

        def do_vertical():
            if _ := try_direction(self.up, cell):
                return _
            if _ := try_direction(self.down, cell):
                return _

        if (self.valid(self.left(cell)) and self._board[self.left(cell)] == HIT) or\
                (self.valid(self.right(cell)) and self._board[self.right(cell)] == HIT):
            vertical = False
        if (self.valid(self.down(cell)) and self._board[self.down(cell)] == HIT) or\
                (self.valid(self.up(cell)) and self._board[self.up(cell)] == HIT):
            horizontal = False
        if horizontal:
            if aux := do_horizontal():
                return aux
        if vertical:
            if aux := do_vertical():
                return aux
        if aux := do_horizontal():
            return aux
        if aux := do_vertical():
            return aux
        raise AIError("Unexpected internal error")

    def move(self):
        """
        Calculates the next move given the state of the board
        :return: The pair of coordinates
        """
        for i in range(self._board.height):
            for j in range(self._board.width):
                if self._board[(i, j)] == HIT:
                    return self._target((i, j))
        return self._hunt()


class AdvancedAI(NormalAI):  # Heat map + hunt and target
    def __init__(self, board):
        NormalAI.__init__(self, board)

    def _hunt(self):
        heat_map = [[0 for _ in range(self._board.width)] for __ in range(self._board.height)]
        for ship in self._board.ships:
            for direction in (self.up, self.down, self.left, self.right):
                for i in range(self._board.height):
                    for j in range(self._board.width):
                        good = True
                        cell = (i, j)
                        visited = []
                        for _ in range(len(ship)):
                            if not self.valid(cell) or self._board[cell] not in (EMPTY, SHIP):
                                good = False
                                break
                            visited.append((i, j))
                            cell = direction(cell)
                        if good:
                            for iter_cell in visited:
                                heat_map[iter_cell[0]][iter_cell[1]] += 1
        mx = 0
        choices = []
        for i in range(self._board.height):
            for j in range(self._board.width):
                if self._board[(i, j)] in (EMPTY, SHIP):
                    mx = max(mx, heat_map[i][j])
        for i in range(self._board.height):
            for j in range(self._board.width):
                if heat_map[i][j] == mx and self._board[(i, j)] in (EMPTY, SHIP):
                    choices.append((i, j))
        return random.choice(choices)
