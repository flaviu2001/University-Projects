from configparser import ConfigParser
from constants import *
from exceptions import *


class Settings:
    def __init__(self):
        self._parser = ConfigParser()
        self._parser.read(SETTINGS_PATH)

    def valid(self):
        """
        Checks whether the settings.ini file is written correctly, otherwise some error will raise
        """
        self.ships()
        self.height()
        self.width()
        self.first()
        self.ai()
        self.ui()

    def ships(self):
        """
        :return: A dictionary of how many of each ship the board must contain
        """
        ca = int(self._parser.get("settings", "carriers"))
        ba = int(self._parser.get("settings", "battleships"))
        cr = int(self._parser.get("settings", "cruisers"))
        de = int(self._parser.get("settings", "destroyers"))
        if sum((ca, ba, cr, de)) == 0:
            raise SettingsError("There should be at least a ship")
        for s in (ca, ba, cr, de):
            if s < 0:
                raise SettingsError("No negative ships")
        return {CARRIER: ca, BATTLESHIP: ba, CRUISER: cr, DESTROYER: de}

    def set_ships(self, dictionary):
        """
        Sets the ships as given by the dictionary in the settings.ini file
        :param dictionary: dictionary similar to the one in self.ships
        """
        for key, value in dictionary.items():
            if value < 0:
                raise SettingsError("No negative ships")
        self._parser.set("settings", "carriers", str(dictionary[CARRIER]))
        self._parser.set("settings", "battleships", str(dictionary[BATTLESHIP]))
        self._parser.set("settings", "cruisers", str(dictionary[CRUISER]))
        self._parser.set("settings", "destroyers", str(dictionary[DESTROYER]))
        self._save()

    def height(self):
        """
        :return: The height of the board specified in the settings.ini file
        """
        x = int(self._parser.get("settings", "height"))
        if x not in range(0, 27):
            raise SettingsError("Invalid height")
        return x

    def set_height(self, value):
        """
        Sets the height of the board in the settings.ini file
        :param value: new height
        """
        if value not in range(0, 27):
            raise SettingsError("Invalid height")
        self._parser.set("settings", "height", str(value))
        self._save()

    def width(self):
        """
        :return: The width of the board specified in the settings.ini file
        """
        x = int(self._parser.get("settings", "width"))
        if x not in range(0, 19):
            raise SettingsError("Invalid width")
        return x

    def set_width(self, value):
        """
        Sets the width of the board in the settings.ini file
        :param value: new height
        """
        if value not in range(0, 19):
            raise SettingsError("Invalid width")
        self._parser.set("settings", "width", str(value))
        self._save()

    def first(self):
        """
        :return: The first who needs to move specified in the settings.ini file
        """
        first = self._parser.get("settings", "first")
        if first not in ("player", "computer", "random"):
            raise SettingsError("Invalid choice")
        return first

    def set_first(self, value):
        """
        Sets the first who needs to move specified in the settings.ini file
        :param value: string in ("player", "computer", "random")
        """
        if value not in ("player", "computer", "random"):
            raise SettingsError("Invalid choice")
        self._parser.set("settings", "first", value)
        self._save()

    def ai(self):
        """
        :return: The difficulty the ai will have in the new game specified in the settings.ini file
        """
        ai = self._parser.get("settings", "difficulty")
        if ai not in ("easy", "normal", "advanced"):
            raise SettingsError("Invalid choice")
        return ai

    def set_ai(self, value):
        """
        Sets the difficulty specified in the settings.ini file
        :param value:
        """
        if value not in ("easy", "normal", "advanced"):
            raise SettingsError("Invalid choice")
        self._parser.set("settings", "difficulty", value)
        self._save()

    def ui(self):
        """
        :return: The the ui the new game will use specified in the settings.ini file
        """
        aux = self._parser.get("settings", "ui")
        if aux not in ("ui", "gui"):
            raise SettingsError("Invalid choice")
        return aux

    def _save(self):
        """
        Writes the contents of ConfigParser to file
        :return:
        """
        file = open("settings.ini", "w")
        self._parser.write(file)
        file.close()
