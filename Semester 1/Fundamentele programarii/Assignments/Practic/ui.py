from game import Game
import re
from os import system


class UI:
    def __init__(self, game=None):
        if game is None:
            game = Game()
        self._game = game

    def start(self):
        while True:
            if self._game.lost():
                print("You have lost the game:(")
                break
            if self._game.won():
                print("You have won the game, congrats!")
                break

            print(self._game)
            cmd = input("Enter your command: ")
            system("clear")
            if mat := re.match("warp ([A-Z][0-9])$", cmd):
                try:
                    self._game.warp(mat.group(1))
                    print("Successfully warped!")
                except ValueError as ve:
                    print(ve)
            elif mat := re.match("fire ([A-Z][0-9])$", cmd):
                try:
                    if self._game.fire(mat.group(1)):
                        print("You've downed a blingon.", end="")
                        if not self._game.won():
                            print(" Watch out, they've repositioned!", end="")
                        print()
                    else:
                        print("You missed.")
                except ValueError as ve:
                    print(ve)
            elif cmd == "cheat":
                self._game.cheat()
                print("You've turned the cheats on.")
            else:
                print("Invalid command")


UI().start()
