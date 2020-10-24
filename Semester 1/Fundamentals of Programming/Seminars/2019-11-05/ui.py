from game import Game
from game import test_generate_number


class UI:
    def __init__(self, game):
        self._game = game

    @staticmethod
    def _print_menu():
        print("1. New game")
        print("2. Exit\n")

    def _play_game(self):
        while True:
            x = input("Your guess: ")
            try:
                x = int(x)
            except ValueError:
                print("That's not a number\n")
                continue
            try:
                guess = self._game.guess(x)
                if self._game.correct_guess(guess):
                    print("You are truly worthy, claim thine prize and rejoice\n")
                    break
                print(str(guess[0]) + " bulls and " + str(guess[1]) + " cows\n")
            except ValueError as ve:
                print(ve)
                break
            except TypeError as te:
                print(te)

    def start(self):
        print("You have opened the best game ever written by human minds")
        print("Your job mortal is to guess my " + str(self._game.number_of_digits) + " non-repeating digits number")
        print("Fail and you shall perish. Conquer and you shall live forever as a champion\n")
        first_time = True
        while True:
            self._print_menu()
            choice = input("> ")
            if choice == "1":
                if first_time:
                    first_time = False
                else:
                    self._game = Game()
                self._play_game()
            elif choice == "2":
                print("Thanks for playing")
                break


test_generate_number()
UI(Game()).start()
