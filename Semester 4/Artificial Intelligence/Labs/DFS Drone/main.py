import save_manager
from random import randrange
from domain import Environment, Drone, DroneMap, MOVE_AUTOMATICALLY
from gui import Gui
from service import Service


def main():
    environment = Environment()
    environment.generate_random_map()
    save_manager.save_environment(environment, "test.map")

    rows = environment.get_rows()
    columns = environment.get_columns()
    x = randrange(0, rows)
    y = randrange(0, columns)
    while environment.wall_at(x, y):
        x = randrange(0, rows)
        y = randrange(0, columns)
    drone = Drone(x, y)
    drone_map = DroneMap(rows, columns)
    service = Service(environment, drone_map, drone)
    gui = Gui(service, MOVE_AUTOMATICALLY)
    gui.start()


if __name__ == "__main__":
    main()
