from constants import ROWS, COLUMNS
from controller import Controller
from gui import moving_drone
from model import DroneMap
from params import Params
from tqdm import trange

params = Params()
battery = params.battery
drone_map = DroneMap(ROWS, COLUMNS, battery)
if params.file:
    drone_map.file_init()
else:
    drone_map.random_init()
controller = Controller(drone_map)
solution = None
for _ in trange(params.iterations):
    current_solution = controller.iterate()
    if solution is None or solution.coverage() < current_solution.coverage():
        solution = current_solution
print(f"The identified solution sees {solution.coverage()} cells")
moving_drone(controller.drone_map, solution.path, battery)
