import random

n = 1000
matrix = []
for _ in range(n):
    x = random.random() * 20 - 10
    y = random.random() * 20 - 10
    matrix.append((x, y))
with open("training.csv", "w") as file:
    file.write(f"{n}\n")
    for line in matrix:
        file.write(f"{line[0]},{line[1]}\n")
