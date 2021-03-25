from matplotlib import pyplot

from controller import *
from gui import *
from repository import *


def main():
    repository = Repository()
    controller = Controller(repository)
    loaded_map = False
    parameters_setup = False
    program_solved = False
    population_size = None
    individual_size = None
    number_of_runs = None
    battery = None
    path = None
    fitness_avg = None
    fitness_max = None
    speed = None
    while True:
        print("1. map options")
        print("2. EA options")
        print("0. exit")
        choice1 = input("> ")
        if choice1 == "1":
            print("1. create a random map")
            print("2. load a map")
            print("3. save a map")
            print("4. visualise map")
            choice2 = input("> ")
            if choice2 == "1":
                loaded_map = True
                fill = float(input("fill factor: "))
                controller.repository.load_random_map(fill)
            elif choice2 == "2":
                loaded_map = True
                controller.repository.load_map()
            elif choice2 == "3":
                if loaded_map:
                    controller.repository.save_map()
                else:
                    print("Map not yet loaded")
            elif choice2 == "4":
                if loaded_map:
                    just_the_drone(controller.repository.drone_map, (controller.repository.drone_map.x, controller.repository.drone_map.y))
                else:
                    print("Map not yet loaded")
            else:
                print("Incorrect choice")
        elif choice1 == "2":
            if not loaded_map:
                print("Map not yet loaded")
                continue
            print("1. parameters setup")
            print("2. run the solver once")
            print("3. run the solver many times and compute mean and standard deviation")
            print("4. visualise the statistics")
            print("5. view the drone on a path")
            choice2 = input("> ")
            if choice2 == "1":
                parameters_setup = True
                population_size = int(input("population size: "))
                individual_size = int(input("individual size: "))
                number_of_runs = int(input("number of runs: "))
                battery = int(input("battery: "))
                speed = float(input("speed of drone in seconds per move: "))
            elif choice2 == "2":
                if not parameters_setup:
                    print("Parameters not set up")
                    continue
                if program_solved:
                    print("Program already solved")
                    continue
                start = time.time()
                path, fitness_avg, fitness_max, solution_fitness = controller.solver(population_size, individual_size,
                                                                                     number_of_runs,
                                                                                     battery)
                end = time.time()
                print(f"Evolutionary algorithm ran in {end-start} seconds")
                print(f"It found a run with {len(path)-1} moves and discovered {solution_fitness} cells")
                program_solved = True
            elif choice2 == "3":
                if not parameters_setup:
                    print("Parameters not set up")
                    continue
                values = []
                for i in range(30):
                    seed(i)
                    _, _, _, fitness = controller.solver(population_size, individual_size, number_of_runs, battery)
                    values.append(fitness)
                avg = statistics.mean(values)
                stdev = statistics.stdev(values)
                print(f"Average solution fitness was found to be {avg} and it has a stdev of {stdev}")
                pyplot.plot(values)
                pyplot.ylim([0, None])
                pyplot.show()
            elif choice2 == "4":
                if not program_solved:
                    print("Program not yet solved")
                    continue
                pyplot.plot(fitness_avg)
                pyplot.plot(fitness_max)
                pyplot.show()
            elif choice2 == "5":
                moving_drone(controller.repository.drone_map, path, speed=speed)
            else:
                print("Incorrect choice")
        elif choice1 == "0":
            break
        else:
            print("Incorrect choice")


if __name__ == "__main__":
    main()


# create a menu
#   1. map options:
#         a. create random map
#         b. load a map
#         c. save a map
#         d visualise map
#   2. EA options:
#         a. parameters setup
#         b. run the solver
#         c. visualise the statistics
#         d. view the drone moving on a path
#              function gui.movingDrone(currentMap, path, speed, mark_seen)
#              ATTENTION! the function doesn't check if the path passes trough walls
