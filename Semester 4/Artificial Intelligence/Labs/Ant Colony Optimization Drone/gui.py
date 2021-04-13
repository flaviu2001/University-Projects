import time
from copy import deepcopy

import pygame

from constants import WHITE, GREEN, DIRECTIONS, BLUE, RED, COLUMN_SIZE, ROW_SIZE


def init_pygame(dimension):
    pygame.init()
    logo = pygame.image.load("logo32x32.png")
    pygame.display.set_icon(logo)
    pygame.display.set_caption("drone exploration with AE")
    screen = pygame.display.set_mode(dimension)
    screen.fill(WHITE)
    return screen


def close_pygame():
    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
    pygame.quit()


def moving_drone(current_map, path, battery, speed=0.5):
    screen = init_pygame((current_map.m * COLUMN_SIZE, current_map.n * ROW_SIZE))
    drona = pygame.transform.scale(pygame.image.load("drona.png"), (COLUMN_SIZE, ROW_SIZE))
    brick = pygame.Surface((COLUMN_SIZE, ROW_SIZE))
    brick.fill(GREEN)
    brick.set_alpha(48)
    sighted_cell = pygame.Surface((COLUMN_SIZE, ROW_SIZE))
    sighted_cell.fill(RED)
    sighted_cell.set_alpha(128)
    sensor = pygame.transform.scale(pygame.image.load("sensor.png"), (COLUMN_SIZE, ROW_SIZE))
    battery += 1
    running = True
    current_map.surface[path[0][0]][path[0][1]] = 0
    for cell_i in range(len(path)):
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
        if not running:
            break
        screen.blit(image(current_map), (0, 0))
        battery -= path[cell_i][2] + 1
        print(f"Battery left: {battery}")
        for j in range(cell_i+1):
            screen.blit(brick, (path[j][1] * COLUMN_SIZE, path[j][0] * ROW_SIZE))
        for cell in path[:cell_i+1]:
            if current_map.surface[cell[0]][cell[1]] == 1:
                i = cell[0]
                j = cell[1]
                neighbours = [[i, j], [i, j], [i, j], [i, j]]
                for _ in range(cell[2]):
                    for direction_index in range(len(DIRECTIONS)):
                        new_neighbour = deepcopy(neighbours[direction_index])
                        new_neighbour[0] += DIRECTIONS[direction_index][0]
                        new_neighbour[1] += DIRECTIONS[direction_index][1]
                        if 0 <= new_neighbour[0] < current_map.n and 0 <= new_neighbour[1] < current_map.m:
                            if current_map.surface[new_neighbour[0]][new_neighbour[1]] != 2:
                                neighbours[direction_index] = new_neighbour
                                screen.blit(sighted_cell, (new_neighbour[1] * COLUMN_SIZE, new_neighbour[0] * ROW_SIZE))
        for i in range(current_map.n):
            for j in range(current_map.m):
                if current_map.surface[i][j] == 1:
                    screen.blit(sensor, (j * COLUMN_SIZE, i * ROW_SIZE))
        screen.blit(drona, (path[cell_i][1] * COLUMN_SIZE, path[cell_i][0] * ROW_SIZE))
        pygame.display.flip()
        time.sleep(speed)
    close_pygame()


def image(current_map, colour=BLUE, background=WHITE):
    imagine = pygame.Surface((current_map.m * COLUMN_SIZE, current_map.n * ROW_SIZE))
    brick = pygame.Surface((COLUMN_SIZE, ROW_SIZE))
    brick.fill(colour)
    imagine.fill(background)
    for i in range(current_map.n):
        for j in range(current_map.m):
            if current_map.surface[i][j] == 2:
                imagine.blit(brick, (j * COLUMN_SIZE, i * ROW_SIZE))
    return imagine
