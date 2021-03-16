import math
import random
from queue import PriorityQueue

from constants import DIRECTIONS


class Service:
    def __init__(self, drone_map, drone, initial_x, initial_y, final_x, final_y, which):
        self.drone_map = drone_map
        self.drone = drone
        self.initial_x = initial_x
        self.initial_y = initial_y
        self.final_x = final_x
        self.final_y = final_y
        self.path = None
        if which == 1:
            self.path = self.search_greedy()
        elif which == 2:
            self.path = self.search_a_star()
        else:
            self.path = self.search_simulated_annealing()
        self.iterator = iter(self.path)
        self.incomplete_path = [next(self.iterator)]
        self.finished_simulation = False

    def best_first_search(self, f):
        inf = self.drone_map.n + self.drone_map.m
        distances = [[inf for _ in range(self.drone_map.m)] for _ in range(self.drone_map.n)]
        value = [[0 for _ in range(self.drone_map.m)] for _ in range(self.drone_map.n)]
        prev = [[(i, j) for j in range(self.drone_map.m)] for i in range(self.drone_map.n)]
        visited = [[False for _ in range(self.drone_map.m)] for _ in range(self.drone_map.n)]
        # noinspection PyTypeChecker
        distances[self.initial_x][self.initial_y] = 0
        visited[self.initial_x][self.initial_y] = True
        pq = PriorityQueue()
        pq.put((0, (self.initial_x, self.initial_y)))
        while not pq.empty():
            item = pq.get()
            if value[item[1][0]][item[1][1]] != item[0]:
                continue
            if item == (self.final_x, self.final_y):
                break
            for direction in DIRECTIONS:
                neighbour = (item[1][0] + direction[0], item[1][1] + direction[1])
                if self.drone_map.drone_fits(*neighbour) and not visited[neighbour[0]][neighbour[1]]:
                    prev[neighbour[0]][neighbour[1]] = item[1]
                    visited[neighbour[0]][neighbour[1]] = True
                    # noinspection PyTypeChecker
                    distances[neighbour[0]][neighbour[1]] = distances[item[1][0]][item[1][1]] + 1
                    value[neighbour[0]][neighbour[1]] = f(neighbour, distances, self.final_x, self.final_y)
                    pq.put((f(neighbour, distances, self.final_x, self.final_y), neighbour))
        if prev[self.final_x][self.final_y] == (self.final_x, self.final_y):
            return []
        path = []
        now = self.final_x, self.final_y
        while now != (self.initial_x, self.initial_y):
            path.append(now)
            now = prev[now[0]][now[1]]
        path.append(now)
        return list(reversed(path))

    def search_a_star(self):
        return self.best_first_search(lambda neighbour, distances, fx, fy: distances[neighbour[0]][neighbour[1]] +
                                      self.__dist((fx, fy), neighbour))

    def search_greedy(self):
        return self.best_first_search(
            lambda neighbour, distances, fx, fy: self.__dist((fx, fy), neighbour))

    @staticmethod
    def __dist(p1, p2):
        return abs(p1[0] - p2[0]) + abs(p1[1] - p2[1])

    def search_simulated_annealing(self, kmax=1000, initial_temperature=1000):
        pair = (self.drone.x, self.drone.y)
        path = [pair]
        for k in range(kmax):
            if pair == (self.final_x, self.final_y):
                return path
            temperature = initial_temperature / (k+1)
            neighbours = []
            for direction in DIRECTIONS:
                new_pair = (pair[0] + direction[0], pair[1] + direction[1])
                if self.drone_map.drone_fits(*new_pair):
                    neighbours.append(new_pair)
            neighbour = random.choice(neighbours)
            delta = self.__dist(neighbour, (self.final_x, self.final_y)) - self.__dist(pair,
                                                                                       (self.final_x, self.final_y))
            prob = math.exp(-delta/temperature)
            print(delta, prob, random.uniform(0, 1))
            if random.uniform(0, 1) < prob:
                pair = neighbour
                path.append(pair)
        return path

    def drone_next_move(self):
        try:
            self.incomplete_path.append(new_pos := next(self.iterator))
            self.drone.move(*new_pos)
        except StopIteration:
            self.finished_simulation = True
