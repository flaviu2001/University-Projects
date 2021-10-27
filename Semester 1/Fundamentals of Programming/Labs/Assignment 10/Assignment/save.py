import pickle
from constants import *


class Save:
    def __init__(self, save_path=SAVEFILE_PATH, game=None):
        self.game = game
        self.save_path = save_path

    def load(self):
        """
        :return: The Game in the save if it exists, else None
        """
        try:
            file = open(self.save_path, "rb")
            ret = pickle.load(file)
            file.close()
            return ret
        except FileNotFoundError:
            return None

    def save(self):
        """
        Writes a Game object to save file
        :return:
        """
        file = open(self.save_path, "wb")
        pickle.dump(self.game, file)
        file.close()
