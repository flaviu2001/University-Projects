from random import randint, randrange

from constants import ROWS, COLUMNS
from domain import Drone, DroneMap
from gui import GUI
from service import Service


def main():
    drone_map = DroneMap()
    drone_map.set_random_map()
    # drone_map.load_map("res/test.map")
    n = drone_map.n
    m = drone_map.m
    sx = randrange(0, n)
    sy = randrange(0, m)
    while drone_map.surface[sx][sy] == 1:
        sx = randrange(0, n)
        sy = randrange(0, m)
    drone = Drone(sx, sy)
    fx = randrange(0, n)
    fy = randrange(0, m)
    while drone_map.surface[fx][fy] == 1 or fx == sx and fy == sy:
        fx = randrange(0, n)
        fy = randrange(0, m)
    service = Service(drone_map, drone, sx, sy, fx, fy)
    gui = GUI(service)
    gui.start()


if __name__ == "__main__":
    main()
