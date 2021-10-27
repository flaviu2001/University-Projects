class Params:
    def __init__(self):
        with open("params.txt", 'r') as file:
            line = file.readline().strip()
            self.iterations = int(line.replace("iterations:", ""))
            line = file.readline().strip()
            self.ants = int(line.replace("number of ants:", ""))
            line = file.readline().strip()
            self.alpha = float(line.replace("alpha:", ""))
            line = file.readline().strip()
            self.beta = float(line.replace("beta:", ""))
            line = file.readline().strip()
            self.rho = float(line.replace("rho:", ""))
            line = file.readline().strip()
            self.q0 = float(line.replace("q0:", ""))
            line = file.readline().strip()
            self.battery = int(line.replace("battery:", ""))
            line = file.readline().strip()
            self.file = line.replace("file:", "") == "yes"
