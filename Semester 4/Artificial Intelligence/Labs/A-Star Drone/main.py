from random import randrange

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
    service1 = Service(drone_map, drone, sx, sy, fx, fy, 1)
    service2 = Service(drone_map, drone, sx, sy, fx, fy, 2)
    service3 = Service(drone_map, drone, sx, sy, fx, fy, 3)
    gui = GUI([service1, service2, service3])
    gui.start()


if __name__ == "__main__":
    main()
