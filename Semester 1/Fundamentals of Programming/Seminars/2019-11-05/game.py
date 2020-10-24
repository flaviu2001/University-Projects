from random import choice


def test_generate_number():
    game = Game()
    x = game._generate_number()
    assert len(set(str(x))) == game.number_of_digits


class Game:
    def __init__(self):
        self.number_of_digits = 4
        self._number = self._generate_number()
        self._guesses = []
        self._game_over = False

    def _generate_number(self):
        v = [1, 2, 3, 4, 5, 6, 7, 8, 9]
        x = choice(v)
        v.remove(x)
        v.append(0)
        for i in range(self.number_of_digits - 1):
            y = choice(v)
            x = x*10+y
            v.remove(y)
        return x

    def _is_repeated_guess(self, number):
        return number in self._guesses

    def correct_guess(self, guess):
        return guess == [len(str(self._number)), 0]

    def guess(self, number):
        if self._game_over:
            raise ValueError("Game over already\n")
        if self._is_repeated_guess(number):
            self._game_over = True
            raise ValueError("Number has been repeated\n")
        if len(str(number)) != self.number_of_digits:
            raise TypeError("That's not " + str(self.number_of_digits) + " digits, try again\n")
        if len(set(str(number))) != self.number_of_digits:
            raise TypeError("Your guess contains repeating digits\n")
        s1 = str(self._number)
        s2 = str(number)
        bulls = 0
        for i in range(min(len(s1), len(s2))):
            if s1[i] == s2[i]:
                bulls += 1
        cows = len(set(s1).intersection(set(s2)))
        cows -= bulls
        self._guesses.append(number)
        return [bulls, cows]
