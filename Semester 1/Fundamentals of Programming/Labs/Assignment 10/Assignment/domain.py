from texttable import Texttable
from exceptions import *
from constants import *
from settings import Settings
import re
from random import randrange


class Ship:
    def __init__(self, starting_pair, ending_pair):
        """
        Class to represent a ship placed on the matrix
        :param starting_pair: pair of coordinates on the matrix signifying one end of the ship
        :param ending_pair: pair of coordinates on the matrix signifying the other end of the ship
        """
        if starting_pair[0] != ending_pair[0] and starting_pair[1] != ending_pair[1]:
            raise ShipError("Invalid bounds!")
        if starting_pair[0] > ending_pair[0]:
            starting_pair[0], ending_pair[0] = ending_pair[0], starting_pair[0]
        if starting_pair[1] > ending_pair[1]:
            starting_pair[1], ending_pair[1] = ending_pair[1], starting_pair[1]
        self.starting_pair = starting_pair
        self.ending_pair = ending_pair
        self.cells = []
        for x in range(starting_pair[0], ending_pair[0]+1):
            for y in range(starting_pair[1], ending_pair[1]+1):
                self.cells.append((x, y))

    def __eq__(self, other):
        if not isinstance(other, Ship):
            return False
        return self.starting_pair == other.starting_pair and self.ending_pair == other.ending_pair

    def __len__(self):
        return len(self.cells)

    def __iter__(self):
        self.n = -1
        return self

    def __next__(self):
        self.n += 1
        if self.n >= len(self.cells):
            raise StopIteration
        return self.cells[self.n]


class Board:
    def __init__(self, owner=COMPUTER, ships=None, height=None, width=None):
        """
        Class to store all the information of one player's matrix
        :param owner: can be COMPUTER or PLAYER
        :param ships: list of ships to begin the game with
        """
        if height is None:
            height = Settings().height()
        if width is None:
            width = Settings().width()
        self.height = height
        self.width = width
        self.owner = owner
        self.matrix = [[EMPTY for _ in range(self.width)] for __ in range(self.height)]
        self.remaining_cells = 0
        if ships is None:
            ships = self.random_ships()
        for ship in ships:
            for cell in ship:
                if self[cell] != 0:
                    raise BoardError("Invalid list of ships")
                self[cell] = SHIP
                self.remaining_cells += 1
        self.ships = ships

    def __getitem__(self, item):
        if type(item) is int:
            return self.matrix[item]
        return self.matrix[item[0]][item[1]]

    def __setitem__(self, item, value):
        if type(item) is int:
            self.matrix[item] = value
        self.matrix[item[0]][item[1]] = value

    def __str__(self):
        if self.owner == PLAYER:
            return self._table_str(DICT_PLAYER)
        return self._table_str(DICT_AI)

    def unhidden(self):
        return self._table_str(DICT_UNHIDDEN)

    def _table_str(self, dictionary):
        text_table = Texttable()
        aux = ["/"]
        for x in range(1, self.width + 1):
            aux += [x]
        text_table.add_row(aux)
        for i in range(self.height):
            aux = [chr(ord('A') + i)]
            if self.owner == PLAYER:
                aux += [dictionary[x] for x in self.matrix[i]]
            else:
                aux += [dictionary[x] for x in self.matrix[i]]
            text_table.add_row(aux)
        return text_table.draw()

    @staticmethod
    def one_ship(occupied_cells, height, width, ship_len):
        cnt = 0
        while True:
            cnt += 1
            if randrange(2) == 0:
                starting_x = randrange(height)
                starting_y = randrange(width - ship_len + 1)
                ending_x = starting_x
                ending_y = starting_y + ship_len - 1
            else:
                starting_x = randrange(height - ship_len + 1)
                starting_y = randrange(width)
                ending_x = starting_x + ship_len - 1
                ending_y = starting_y
            good = True
            for i in range(starting_x, ending_x + 1):
                if not good:
                    break
                for j in range(starting_y, ending_y + 1):
                    if (i, j) in occupied_cells:
                        good = False
                        break
            if not good:
                if cnt >= 100:
                    raise BoardError("Board too small")
                continue
            return Ship((starting_x, starting_y), (ending_x, ending_y))

    @staticmethod
    def random_ships(good_random=True):
        """
        :return: A list of randomly placed ships as specified by the settings.ini file
        """
        ships = []
        occupied_cells = []
        height = Settings().height()
        width = Settings().width()
        for ship, frequency in Settings().ships().items():
            ship_len = DICT_LEN[ship]
            if ship_len > min(height, width):
                raise BoardError("Board too small")
            while frequency > 0:
                frequency -= 1
                possible = []
                if good_random:
                    for _ in range(3):
                        possible.append(Board.one_ship(occupied_cells, height, width, ship_len))

                    def min_dist(ship2):  # a slightly more efficient lee algorithm
                        from collections import deque
                        queue = deque()
                        visited = set()
                        for cell3 in ship2:
                            queue.append((cell3, 0))
                            visited.add(cell3)
                        while len(queue) > 0:
                            x = queue.popleft()
                            if x[0] in occupied_cells:
                                return x[1]
                            dx = [1, -1, 0, 0]
                            dy = [0, 0, 1, -1]
                            for d in range(4):
                                cell3 = (x[0][0]+dx[d], x[0][1]+dy[d])
                                if cell3 not in visited and cell3[0] in range(height) and cell3[1] in range(width):
                                    visited.add(cell3)
                                    queue.append((cell3, x[1]+1))
                        return 0
                    mx = 0
                    for s in possible:
                        mx = max(mx, min_dist(s))
                    for s in possible:
                        if min_dist(s) == mx:
                            ships.append(s)
                            for cell2 in s:
                                occupied_cells.append(cell2)
                            break
                else:
                    ships.append(ship := Board.one_ship(occupied_cells, height, width, ship_len))
                    for cell in ship:
                        occupied_cells.append(cell)
        return ships

    def finished(self):
        """
        :return: True if all ship cells were hit,
        """
        return self.remaining_cells == 0

    def _sunk_ship(self):
        """
        :return: if a ship was fully sunk returns the ship, else none
        """
        for ship in self.ships:
            sunk = True
            for cell in ship:
                if self[cell] != HIT:
                    sunk = False
            if sunk:
                return ship
        return None

    def move(self, pair):
        """
        Makes a move and raises an error if the cell had already been hit
        :param pair: pair of coordinates
        :return: True if hit a ship
        """
        if self[pair] not in (SHIP, EMPTY):
            raise BoardError("Cell already hit")
        if self[pair] == SHIP:
            self.remaining_cells -= 1
            self[pair] = HIT
            if ship := self._sunk_ship():
                for cell in ship:
                    self[cell] = SUNK
                self.ships.remove(ship)
            return True
        else:
            self[pair] = MISS
            return False

    @staticmethod
    def string_to_pair(pos, height=None, width=None):
        """self
        Converts a string of the form A1 or D4 or H8 to the pair of coordinates which it encodes
        :param pos: string
        :param height: max height of board
        :param width: max width of board
        :return: pair
        """
        if height is None:
            height = Settings().height()
        if width is None:
            width = Settings().width()
        if not (mat := re.match("([A-Z])([1-9][0-9]*)$", pos, re.IGNORECASE)):
            raise BoardError("Invalid move.")
        groups = mat.groups()
        if ord(groups[0]) in range(65, 91):
            pair = [ord(groups[0]) - 65]
        else:
            pair = [ord(groups[0]) - 97]
        pair.append(int(groups[1]) - 1)
        if pair[0] not in range(height) or pair[1] not in range(width):
            raise BoardError("Invalid move.")
        return pair
