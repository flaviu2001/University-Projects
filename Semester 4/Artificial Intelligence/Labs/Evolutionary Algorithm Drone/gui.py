import time

import pygame

from utils import WHITE, GREEN, BLUE, DIRECTIONS


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


def moving_drone(current_map, path, speed=0.5, mark_seen=True):
    screen = init_pygame((current_map.n * 20, current_map.m * 20))
    drona = pygame.image.load("drona.png")
    for i in range(len(path)):
        screen.blit(image(current_map), (0, 0))
        if mark_seen:
            brick = pygame.Surface((20, 20))
            brick.fill(GREEN)
            for j in range(i + 1):
                for direction in DIRECTIONS:
                    x = path[j][0]
                    y = path[j][1]
                    while ((0 <= x + direction[0] < current_map.n and
                            0 <= y + direction[1] < current_map.m) and
                           current_map.surface[x + direction[0]][y + direction[1]] != 1):
                        x = x + direction[0]
                        y = y + direction[1]
                        screen.blit(brick, (y * 20, x * 20))
        screen.blit(drona, (path[i][1] * 20, path[i][0] * 20))
        pygame.display.flip()
        time.sleep(speed)
    close_pygame()


def just_the_drone(current_map, drone_position):
    screen = init_pygame((current_map.n * 20, current_map.m * 20))
    screen.blit(image(current_map), (0, 0))
    drone = pygame.image.load("drona.png")
    screen.blit(drone, (drone_position[1] * 20, drone_position[0] * 20))
    for _ in range(1000):
        pygame.display.flip()
    close_pygame()


def image(current_map, colour=BLUE, background=WHITE):
    imagine = pygame.Surface((current_map.n * 20, current_map.m * 20))
    brick = pygame.Surface((20, 20))
    brick.fill(colour)
    imagine.fill(background)
    for i in range(current_map.n):
        for j in range(current_map.m):
            if current_map.surface[i][j] == 1:
                imagine.blit(brick, (j * 20, i * 20))
    return imagine
