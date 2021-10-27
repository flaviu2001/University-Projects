import os
from domain import *
from game import Game
from save import Save
from ai import *


class UI:
    def __init__(self):
        self._game = None

    def _print_recent_moves(self):
        for move in self._game.recent:
            print(move)
        self._game.reset_recent()

    def _show_boards(self):
        os.system('cls' if os.name == 'nt' else 'clear')
        self._print_recent_moves()
        print("{0} cells left for the player".format(self._game.board_ai.remaining_cells))
        print("{0} cells left for the computer".format(self._game.board_player.remaining_cells))
        print("PLAYER BOARD\n{0}\n\nCOMPUTER BOARD - You guess here\n{1}".format(self._game.board_player,
                                                                                 self._game.board_ai))

    def _show_boards_finished(self):
        os.system('cls' if os.name == 'nt' else 'clear')
        print("PLAYER BOARD\n{0}\n\nCOMPUTER BOARD - You guess here\n{1}".format(self._game.board_player.unhidden(),
                                                                                 self._game.board_ai.unhidden()))
        self._print_recent_moves()
        if self._game.board_ai.finished():
            print("Congratulations, you won!")
        if self._game.board_player.finished():
            print("Unfortunately you lost.")

    def exit(self):
        Save(game=self._game).save()
        print("You have quit the game, you may resume it later.")
        exit()

    @staticmethod
    def _player_ships():
        print("Now you need to place your ships on the board.")
        print("Would you like to generate their positions randomly instead? y/N")
        while True:
            cmd = input("> ")
            if cmd not in ("y", "N"):
                if cmd == "exit":
                    print("Cannot exit right now.")
                print("Invalid choice, try again.")
                continue
            if cmd == "y":
                ships = None
            else:
                ships = []
                for ship, frequency in Settings().ships().items():
                    while frequency > 0:
                        print("Enter the coordinates of a {0}. A {0} has {1} cells".format(DICT_NAME[ship],
                                                                                           DICT_LEN[ship]))
                        while True:
                            pair1 = None
                            pair2 = None
                            while True:
                                pos1 = input("The first coordinate of the {0}: ".format(DICT_NAME[ship]))
                                try:
                                    if pos1 == "exit":
                                        raise BoardError("Cannot exit right now")
                                    pair1 = Board.string_to_pair(pos1)
                                    break
                                except BoardError as be:
                                    print(be)
                                    continue
                            while True:
                                pos2 = input("The last coordinate of the {0}: ".format(DICT_NAME[ship]))
                                try:
                                    if pos2 == "exit":
                                        raise BoardError("Cannot exit right now")
                                    pair2 = Board.string_to_pair(pos2)
                                    break
                                except BoardError as be:
                                    print(be)
                                    continue
                            try:
                                s = Ship(pair1, pair2)
                                if len(s) != DICT_LEN[ship]:
                                    raise ShipError("Invalid length")
                                ships.append(s)
                                break
                            except ShipError as se:
                                print(se)
                                pass
                        frequency -= 1
            return ships

    @staticmethod
    def _starting_text():
        print("Welcome to Jack's console Battleship game!")
        print("The game consists in two boards, one is yours and one is your opponent's.")
        print("The first who hits all the enemy's ships wins. To hit a ship specify its coordinates.")
        print("A few examples are A1, D8 or H8.")
        print("To exit the game at any time you may type exit in the console\n")

    def start(self):
        loaded = False
        if saved_game := Save(game=self._game).load():
            choice = input("The program detected an unfinished game from last time, do you want to continue? y/N: ")
            while choice not in ("y", "N"):
                if choice == "exit":
                    print("Cannot exit right now.")
                choice = input("Wrong choice, try again: ")
            if choice == "y":
                self._game = saved_game
                loaded = True
        if not loaded:
            ai = {"easy": EasyAI, "normal": NormalAI, "advanced": AdvancedAI}[Settings().ai()]
            first = {"player": PLAYER,
                     "computer": COMPUTER,
                     "random": random.choice((PLAYER, COMPUTER))
                     }[Settings().first()]
            board_ai = Board()
            self._starting_text()
            board_player = Board(owner=PLAYER, ships=self._player_ships())
            self._game = Game(ai, board_player, board_ai, first)
        while True:
            if self._game.board_ai.finished():
                self._show_boards_finished()
                break
            if self._game.board_player.finished():
                self._show_boards_finished()
                break
            if self._game.turn == PLAYER:
                self._show_boards()
                while True:
                    move = input("Your move: ")
                    if move == "exit":
                        self.exit()
                    try:
                        self._game.player_move(self._game.board_player.string_to_pair(move))
                        break
                    except Exception as e:
                        print(e)
            else:
                self._game.ai_move()
