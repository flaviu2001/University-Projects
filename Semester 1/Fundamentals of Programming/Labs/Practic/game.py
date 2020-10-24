import re
import texttable
from constants import *
import random


class Game:
    def __init__(self):
        self._width = 8
        self._height = 8
        self._number_of_stars = 10
        self._blingons = 3
        self._matrix = self._place_stars()
        self._endeavour = self._place_endeavour()
        self._place_blingon()
        self._cheated = False
        self._game_over = False

    @staticmethod
    def near(matrix, i, j, bad_neighbour, including_me=True):
        dx = [-1, -1, -1, 0, 0, 1, 1, 1]
        dy = [-1, 0, 1, -1, 1, -1, 0, 1]
        if including_me:
            dx.append(0)
            dy.append(0)

        def valid(pair):
            return pair[0] in range(len(matrix)) and pair[1] in range(len(matrix[0]))

        for a in range(len(dx)):
            i2 = i+dx[a]
            j2 = j+dy[a]
            if valid((i2, j2)) and matrix[i2][j2] == bad_neighbour:
                return True
        return False

    def _place_stars(self):
        """
        This function will generate a height x width empty matrix with randomly placed values of 1 on it.
        The values of 1 are considered stars and they are not adjacent on row, column or diagonal
        :return: matrix of 0s and 1s
        """
        matrix = [[EMPTY for _ in range(self._width)] for __ in range(self._height)]
        for _ in range(self._number_of_stars):
            i = random.randrange(self._height)
            j = random.randrange(self._width)
            while self.near(matrix, i, j, STAR):
                i = random.randrange(self._height)
                j = random.randrange(self._width)
            matrix[i][j] = STAR
        return matrix

    def _place_endeavour(self):
        i = random.randrange(self._height)
        j = random.randrange(self._width)
        while self._matrix[i][j] == STAR:
            i = random.randrange(self._height)
            j = random.randrange(self._width)
        self._matrix[i][j] = ENDEAVOUR
        return i, j

    def _place_blingon(self):
        for i in range(self._height):
            for j in range(self._width):
                if self._matrix[i][j] == BLINGON:
                    self._matrix[i][j] = EMPTY
        for _ in range(self._blingons):
            i = random.randrange(self._height)
            j = random.randrange(self._width)
            while self._matrix[i][j] != EMPTY:
                i = random.randrange(self._height)
                j = random.randrange(self._width)
            self._matrix[i][j] = BLINGON

    def string_to_pair(self, string):
        if not re.match("[A-Z][0-9]$", string):
            raise ValueError("Invalid format for the position.")
        a = ord(string[0])-ord('A')
        b = ord(string[1])-ord('1')
        if a not in range(self._height) or b not in range(self._width):
            raise ValueError("Location out of matrix.")
        return a, b

    def fire(self, location):
        location = self.string_to_pair(location)
        if not self.near(self._matrix, location[0], location[1], ENDEAVOUR, including_me=False):
            raise ValueError("Target not adjacent to Endeavour.")
        if self._matrix[location[0]][location[1]] == BLINGON:
            self._blingons -= 1
            self._place_blingon()
            return True
        return False

    def warp(self, location):
        location = self.string_to_pair(location)
        if location == self._endeavour:
            raise ValueError("Target is the same cell as the Endeavour")
        if not (location[0] == self._endeavour[0] or location[1] == self._endeavour[1] or
                abs(location[0]-self._endeavour[0]) == abs(location[1]-self._endeavour[1])):
            raise ValueError("Target not on the same row, column or diagonal as the Endeavour.")
        if not location[0] in range(self._height) and location[1] in range(self._width):
            raise ValueError("Target not inside the matrix.")

        def sgn(n):
            if n > 0:
                return 1
            if n < 0:
                return -1
            return 0

        a = sgn(location[0]-self._endeavour[0])
        b = sgn(location[1]-self._endeavour[1])
        x, y = self._endeavour
        while True:
            if self._matrix[x][y] == STAR:
                raise ValueError("Star is on the way")
            x += a
            y += b
            if (x-a, y-b) == location:
                break
        if self._matrix[location[0]][location[1]] == BLINGON:
            self._game_over = True
            raise ValueError("You have landed on a blingon.")
        self._matrix[self._endeavour[0]][self._endeavour[1]] = EMPTY
        self._matrix[location[0]][location[1]] = ENDEAVOUR
        self._endeavour = location

    def cheat(self):
        self._cheated = True

    def won(self):
        return self._blingons == 0

    def lost(self):
        return self._game_over

    def __str__(self):
        if self._cheated:
            aux = CHEAT_DICTIONARY
        else:
            aux = NORMAL_DICTIONARY
        text = texttable.Texttable()
        row = list(range(0, self._width+1))
        text.add_row(row)
        for i in range(self._height):
            row = [chr(ord('A')+i)]
            for j in range(self._width):
                elem = aux[self._matrix[i][j]]
                if self._matrix[i][j] == BLINGON and self.near(self._matrix, i, j, ENDEAVOUR, False):
                    elem = CHEAT_DICTIONARY[self._matrix[i][j]]
                row.append(elem)
            text.add_row(row)
        return text.draw()
