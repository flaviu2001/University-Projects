import random
from matplotlib import pyplot


def parse_csv(file_name):
    points = dict()
    with open(file_name, "r") as file:
        file.readline()
        while line := file.readline()[:-1]:
            tokens = line.split(",")
            points[(float(tokens[1]), float(tokens[2]))] = tokens[0]
    return points


def dist(a, b):
    return (a[0] - b[0]) ** 2 + (a[1] - b[1]) ** 2


def closest_centroid(centroids, point):
    centroid_sol = centroids[0]
    for centroid in centroids[1:]:
        if dist(centroid, point) < dist(centroid_sol, point):
            centroid_sol = centroid
    return centroid_sol


def new_centroids(points, k):
    minx = min([point[0] for point in points])
    maxx = max([point[0] for point in points])
    miny = min([point[1] for point in points])
    maxy = max([point[1] for point in points])
    centroids = [[0, 0] for _ in range(k)]
    centroids_cnt = [0 for _ in range(k)]
    for point in points:
        centroids_cnt[points[point]] += 1
        centroid = centroids[points[point]]
        centroid[0] += point[0]
        centroid[1] += point[1]
    to_return = []
    for i, centroid in enumerate(centroids):
        if centroids_cnt[i] != 0:
            to_return.append((centroid[0] / centroids_cnt[i], centroid[1] / centroids_cnt[i]))
        else:
            to_return.append((random.random() * (maxx - minx) + minx, random.random() * (maxy - miny) + miny))
    return to_return


def solve(points, k):
    solution = dict()
    minx = min([point[0] for point in points])
    maxx = max([point[0] for point in points])
    miny = min([point[1] for point in points])
    maxy = max([point[1] for point in points])
    centroids = []
    for _ in range(k):
        centroids.append((random.random() * (maxx - minx) + minx, random.random() * (maxy - miny) + miny))
    iterations = 100
    for i in range(iterations):
        for point in points:
            centroid = closest_centroid(centroids, point)
            solution[point] = centroids.index(centroid)
        if i != iterations-1:
            centroids = new_centroids(solution, k)
    return solution, centroids


def get_stats(good_points, computed_points, k):
    guys = [dict() for _ in range(k)]
    for point in good_points:
        if good_points[point] not in guys[computed_points[point]]:
            guys[computed_points[point]][good_points[point]] = 0
        guys[computed_points[point]][good_points[point]] += 1
    return guys


def main():
    good_points = parse_csv("dataset.csv")
    colors = ["red", "blue", "green", "yellow"]
    k = 4
    points, centroids = solve(good_points, k)
    stats = get_stats(good_points, points, k)
    accuracy = sum([max(stat.values()) for stat in stats]) / len(good_points)
    print(stats)
    print(f"accuracy: {accuracy}")

    for point in points:
        pyplot.scatter(point[0], point[1], color=colors[points[point]])
    for point in centroids:
        pyplot.scatter(point[0], point[1], color="black")
    pyplot.show()


if __name__ == "__main__":
    main()
