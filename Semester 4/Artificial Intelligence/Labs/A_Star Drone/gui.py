import time

import pygame
from constants import BLUE, WHITE, GREEN, COLUMN_SIZE, ROW_SIZE


class GUI:
    def __init__(self, service):
        self.service = service

    def start(self):
        pygame.init()
        logo = pygame.image.load("res/logo32x32.png")
        pygame.display.set_icon(logo)
        pygame.display.set_caption("Path in simple environment")
        screen = pygame.display.set_mode((self.service.drone_map.m * COLUMN_SIZE,
                                          self.service.drone_map.n * ROW_SIZE))
        screen.fill(WHITE)
        while not self.service.finished_simulation:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    return
            self.service.drone_next_move()
            screen.blit(self.drone_map_image(), (0, 0))
            pygame.display.flip()
            time.sleep(0.03)
        running = True
        while running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT or event.type == pygame.KEYDOWN:
                    running = False
        pygame.quit()

    def drone_map_image(self, colour=BLUE, background=WHITE):
        image = pygame.Surface((self.service.drone_map.m * COLUMN_SIZE,
                                self.service.drone_map.n * ROW_SIZE))
        brick = pygame.Surface((COLUMN_SIZE, ROW_SIZE))
        brick.fill(colour)
        image.fill(background)
        for i in range(self.service.drone_map.n):
            for j in range(self.service.drone_map.m):
                if self.service.drone_map.surface[i][j] == 1:
                    image.blit(brick, (j * COLUMN_SIZE, i * ROW_SIZE))
        mark = pygame.Surface((COLUMN_SIZE, ROW_SIZE))
        mark.fill(GREEN)
        for move in self.service.incomplete_path:
            image.blit(mark, (move[1] * COLUMN_SIZE, move[0] * ROW_SIZE))
        letter_s = pygame.transform.scale(pygame.image.load("res/letter_s.png"), (COLUMN_SIZE, ROW_SIZE))
        image.blit(letter_s, (self.service.initial_y * COLUMN_SIZE, self.service.initial_x * ROW_SIZE))
        letter_f = pygame.transform.scale(pygame.image.load("res/letter_f.png"), (COLUMN_SIZE, ROW_SIZE))
        image.blit(letter_f, (self.service.final_y * COLUMN_SIZE, self.service.final_x * ROW_SIZE))
        drona = pygame.transform.scale(pygame.image.load("res/drona.png"), (COLUMN_SIZE, ROW_SIZE))
        image.blit(drona, (self.service.drone.y * COLUMN_SIZE, self.service.drone.x * ROW_SIZE))
        return image
