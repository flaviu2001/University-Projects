from domain import *
from settings import *
from ai import *
from unittest import TestCase


def sim(ai):
    board = Board()
    cnt = 0
    while not board.finished():
        cnt += 1
        board.move(ai(board).move())
    return cnt


def score(ai):
    sum2 = 0
    cnt2 = 0
    for _ in range(10):
        sum2 += sim(ai)
        cnt2 += 1
    return sum2 / cnt2


class Tests(TestCase):
    def test_ship(self):
        with self.assertRaises(ShipError):
            Ship((1, 2), (2, 3))
        Ship((1, 2), (1, 3))

    def test_board(self):
        board = Board()
        supposed = 0
        for ship, frequency in Settings().ships().items():
            supposed += frequency*DICT_LEN[ship]
        self.assertEqual(supposed, board.remaining_cells)
        board.move((0, 0))
        with self.assertRaises(BoardError):
            board.move((0, 0))
        for i in range(board.height):
            for j in range(board.width):
                if (i, j) != (0, 0):
                    board.move((i, j))
        self.assertTrue(board.finished())

    def test_ai(self):
        for ai in (EasyAI, NormalAI, AdvancedAI):
            board = Board()
            board.move(pair := ai(board).move())
            self.assertTrue(pair[0] in range(board.height))
            self.assertTrue(pair[1] in range(board.width))
        all_cells = Board().remaining_cells
        width = Board().width
        height = Board().height
        for ai in (EasyAI, NormalAI, AdvancedAI):
            s = score(ai)
            self.assertFalse(s < all_cells or s > width*height)
