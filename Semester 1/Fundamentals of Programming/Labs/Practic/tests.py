from constants import *
import unittest
from game import Game


class Tests(unittest.TestCase):
    def test_place_stars(self):
        g = Game()
        matrix = g._place_stars()
        ones = 0
        for line in matrix:
            for x in line:
                if x == 1:
                    ones += 1
        self.assertTrue(ones == g._number_of_stars)

        for i in range(len(matrix)):
            for j in range(len(matrix[i])):
                if matrix[i][j] == STAR:
                    self.assertFalse(g.near(matrix, i, j, STAR, including_me=False))
