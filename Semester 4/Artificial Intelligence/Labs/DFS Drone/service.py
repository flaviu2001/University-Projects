from constants import DIRECTIONS


class Service:
    def __init__(self, environment, drone_map, drone):
        self.__environment = environment
        self.__drone_map = drone_map
        self.__drone = drone
        self.__generator = None
        self.__visited_cells = None
        self.__mark_detected_walls()

    def __mark_detected_walls(self):
        walls = self.__environment.read_udm_sensors(self.__drone.x, self.__drone.y)
        self.__drone_map.mark_detected_walls(walls, self.__drone.x, self.__drone.y)

    def __dfs_generator(self, x, y, visited_cells, my_map):
        visited_cells.add((x, y))
        for direction in DIRECTIONS:
            x2 = x + direction[0]
            y2 = y + direction[1]
            if 0 <= x2 < self.get_rows() and 0 <= y2 < self.get_columns() and\
                    (x2, y2) not in visited_cells and my_map[x2][y2] == 0:
                yield x2, y2
                yield from self.__dfs_generator(x2, y2, visited_cells, my_map)
                yield x, y

    def get_rows(self):
        return self.__environment.get_rows()

    def get_columns(self):
        return self.__environment.get_columns()

    def get_environment(self):
        return self.__environment

    def get_drone_map(self):
        return self.__drone_map

    def get_drone(self):
        return self.__drone

    def move_dfs(self):
        if self.__generator is None:
            self.__visited_cells = set()
            self.__generator = self.__dfs_generator(self.__drone.x, self.__drone.y,
                                                    self.__visited_cells, self.__drone_map.get_surface())
        try:
            self.__drone.move(next(self.__generator))
            self.__mark_detected_walls()
            return True
        except StopIteration:
            self.__drone.move((None, None))
            self.__mark_detected_walls()
            return False

    def move_by_hand(self, direction):
        x = self.__drone.x + DIRECTIONS[direction][0]
        y = self.__drone.y + DIRECTIONS[direction][1]
        if 0 <= x < self.get_rows() and 0 <= y < self.get_columns() and self.__drone_map.get_surface()[x][y] == 0:
            self.__drone.move((x, y))
        self.__mark_detected_walls()
