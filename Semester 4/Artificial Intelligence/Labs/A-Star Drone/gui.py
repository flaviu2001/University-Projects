import time

import pygame
from constants import BLUE, WHITE, GREEN, COLUMN_SIZE, ROW_SIZE, BLACK, RED


class GUI:
    def __init__(self, services):
        self.services = services

    def start(self):
        pygame.init()
        logo = pygame.image.load("res/logo32x32.png")
        pygame.display.set_icon(logo)
        pygame.display.set_caption("Path in simple environment")
        screen = pygame.display.set_mode((self.services[0].drone_map.m * COLUMN_SIZE,
                                          self.services[0].drone_map.n * ROW_SIZE))
        screen.fill(WHITE)
        while any(not service.finished_simulation for service in self.services):
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    return
            for service in self.services:
                if not service.finished_simulation:
                    service.drone_next_move()
            screen.blit(self.drone_map_image(), (0, 0))
            pygame.display.flip()
            time.sleep(0.01)
        running = True
        while running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT or event.type == pygame.KEYDOWN:
                    running = False
        pygame.quit()

    def drone_map_image(self, colour=BLUE, background=WHITE):
        n = self.services[0].drone_map.n
        m = self.services[0].drone_map.m
        image = pygame.Surface((m * COLUMN_SIZE,
                               n * ROW_SIZE))
        brick = pygame.Surface((COLUMN_SIZE, ROW_SIZE))
        brick.fill(colour)
        image.fill(background)
        for i in range(n):
            for j in range(m):
                if self.services[0].drone_map.surface[i][j] == 1:
                    image.blit(brick, (j * COLUMN_SIZE, i * ROW_SIZE))
        colours = iter([GREEN, RED, BLACK])
        for service in self.services:
            mark = pygame.Surface((COLUMN_SIZE, ROW_SIZE))
            mark.fill(next(colours))
            mark.set_alpha(80)
            for move in service.incomplete_path:
                image.blit(mark, (move[1] * COLUMN_SIZE, move[0] * ROW_SIZE))
        letter_s = pygame.transform.scale(pygame.image.load("res/letter_s.png"), (COLUMN_SIZE, ROW_SIZE))
        image.blit(letter_s, (self.services[0].initial_y * COLUMN_SIZE, self.services[0].initial_x * ROW_SIZE))
        letter_f = pygame.transform.scale(pygame.image.load("res/letter_f.png"), (COLUMN_SIZE, ROW_SIZE))
        image.blit(letter_f, (self.services[0].final_y * COLUMN_SIZE, self.services[0].final_x * ROW_SIZE))
        drona = pygame.transform.scale(pygame.image.load("res/drona.png"), (COLUMN_SIZE, ROW_SIZE))
        image.blit(drona, (self.services[0].drone.y * COLUMN_SIZE, self.services[0].drone.x * ROW_SIZE))
        return image
