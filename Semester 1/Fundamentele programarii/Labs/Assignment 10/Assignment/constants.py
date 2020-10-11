PLAYER = 10
COMPUTER = 11

EMPTY = 0
SHIP = 1
HIT = 2
MISS = 3
SUNK = -1

EMPTY_SPRITE = "sprites/empty.png"
SHIP_SPRITE = "sprites/ship.png"
HIT_SPRITE = "sprites/hit.png"
MISS_SPRITE = "sprites/miss.png"
SUNK_SPRITE = "sprites/sunk.png"
WINNER_SPRITE = "sprites/winner.png"
LOSER_SPRITE = "sprites/loser.png"
PLAYER_SPRITE = "sprites/player.png"
COMPUTER_SPRITE = "sprites/computer.png"

SETTINGS_PATH = "settings.ini"
SAVEFILE_PATH = "savefile.sav"

BATTLESHIP = 20
CRUISER = 21
DESTROYER = 22
CARRIER = 23

DICT_AI = {SUNK: 'u', EMPTY: '.', SHIP: '.', HIT: '!', MISS: 'x'}
DICT_PLAYER = {SUNK: 'u', EMPTY: '.', SHIP: 'S', HIT: '!', MISS: 'x'}
DICT_UNHIDDEN = {SUNK: 'u', EMPTY: '.', SHIP: 'S', HIT: '!', MISS: 'x'}
DICT_LEN = {CARRIER: 5, BATTLESHIP: 4, CRUISER: 3, DESTROYER: 2}
DICT_NAME = {CARRIER: "Carrier", BATTLESHIP: "Battleship", CRUISER: "Cruiser", DESTROYER: "Destroyer"}
DICT_SPRITE = {EMPTY: EMPTY_SPRITE, SHIP: SHIP_SPRITE, HIT: HIT_SPRITE, MISS: MISS_SPRITE, SUNK: SUNK_SPRITE}
DICT_SPRITE_PLAYER = {EMPTY: EMPTY_SPRITE, SHIP: EMPTY_SPRITE, HIT: HIT_SPRITE, MISS: MISS_SPRITE, SUNK: SUNK_SPRITE}
