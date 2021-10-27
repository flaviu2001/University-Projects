from save import *
from constants import *
import os


class Game:
    def __init__(self, ai, board_player, board_ai, first):
        """
        Holds information about the game overall, such as the ai calculating moves, the 2 matrices and who goes first
        :param ai: class from ai.py
        :param board_player: Board
        :param board_ai: Board
        :param first: PLAYER or COMPUTER
        """
        self._ai = ai
        self._board_player = board_player
        self._board_ai = board_ai
        self.turn = first
        self.recent = []
        Save(game=self).save()

    @property
    def ai(self):
        return self._ai

    @property
    def board_player(self):
        return self._board_player

    @property
    def board_ai(self):
        return self._board_ai

    def player_move(self, pair):
        """
        Performs the move of the player
        :param pair: pair
        """
        if not (hit := self._board_ai.move(pair)):
            self.turn = COMPUTER
        self.recent.append("Player {0} on {1}{2}".format("hit" if hit else "miss",
                                                         chr(ord('A')+pair[0]),
                                                         pair[1]+1))
        Save(game=self).save()
        if self._board_ai.finished():
            os.remove(SAVEFILE_PATH)

    def ai_move(self):
        """
        Performs the move of the ai
        """
        while True:
            pair = self._ai(self._board_player).move()
            hit = self._board_player.move(pair)
            self.recent.append("Computer {0} on {1}{2}".format("hit" if hit else "miss",
                                                               chr(ord('A') + pair[0]),
                                                               pair[1]+1))
            if not hit or self._board_player.finished():
                break
        self.turn = PLAYER
        Save(game=self).save()
        if self._board_player.finished():
            os.remove(SAVEFILE_PATH)

    def reset_recent(self):
        self.recent.clear()
