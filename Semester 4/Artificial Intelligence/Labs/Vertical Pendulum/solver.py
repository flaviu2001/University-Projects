"""
In this file your task is to write the solver function!

"""


class Solver:
    def __init__(self):
        self.theta_ranges = {
            "NVB": (None, -40, -25),
            "NB": (-40, -25, -10),
            "N": (-20, -10, 0),
            "ZO": (-5, 0, 5),
            "P": (0, 10, 20),
            "PB": (10, 25, 40),
            "PVB": (25, 40, None)
        }

        self.omega_ranges = {
            "NB": (None, -8, -3),
            "N": (-6, -3, 0),
            "ZO": (-1, 0, 1),
            "P": (0, 3, 6),
            "PB": (3, 8, None)
        }

        self.fRanges = {
            "NVVB": (None, -32, -24),
            "NVB": (-32, -24, -16),
            "NB": (-24, -16, -8),
            "N": (-16, -8, 0),
            "Z": (-4, 0, 4),
            "P": (0, 8, 16),
            "PB": (8, 16, 24),
            "PVB": (16, 24, 32),
            "PVVB": (24, 32, None)
        }
        self.bValues = {key: value[1] for key, value in self.fRanges.items()}
        self.fuzzy_table = dict()
        self.fuzzy_table["NB"] = dict()
        self.fuzzy_table["NB"]["NB"] = "NVVB"
        self.fuzzy_table["NB"]["N"] = "NVB"
        self.fuzzy_table["NB"]["ZO"] = "NB"
        self.fuzzy_table["NB"]["P"] = "N"
        self.fuzzy_table["NB"]["PB"] = "Z"
        self.fuzzy_table["N"] = dict()
        self.fuzzy_table["N"]["NB"] = "NVB"
        self.fuzzy_table["N"]["N"] = "NB"
        self.fuzzy_table["N"]["ZO"] = "N"
        self.fuzzy_table["N"]["P"] = "Z"
        self.fuzzy_table["N"]["PB"] = "P"
        self.fuzzy_table["ZO"] = dict()
        self.fuzzy_table["ZO"]["NB"] = "NB"
        self.fuzzy_table["ZO"]["N"] = "N"
        self.fuzzy_table["ZO"]["ZO"] = "Z"
        self.fuzzy_table["ZO"]["P"] = "P"
        self.fuzzy_table["ZO"]["PB"] = "PB"
        self.fuzzy_table["P"] = dict()
        self.fuzzy_table["P"]["NB"] = "N"
        self.fuzzy_table["P"]["N"] = "Z"
        self.fuzzy_table["P"]["ZO"] = "P"
        self.fuzzy_table["P"]["P"] = "PB"
        self.fuzzy_table["P"]["PB"] = "PVB"
        self.fuzzy_table["PB"] = dict()
        self.fuzzy_table["PB"]["NB"] = "Z"
        self.fuzzy_table["PB"]["N"] = "P"
        self.fuzzy_table["PB"]["ZO"] = "PB"
        self.fuzzy_table["PB"]["P"] = "PVB"
        self.fuzzy_table["PB"]["PB"] = "PVVB"
        self.fuzzy_table["PVB"] = dict()
        self.fuzzy_table["PVB"]["NB"] = "P"
        self.fuzzy_table["PVB"]["N"] = "PB"
        self.fuzzy_table["PVB"]["ZO"] = "PVB"
        self.fuzzy_table["PVB"]["P"] = "PVVB"
        self.fuzzy_table["PVB"]["PB"] = "PVVB"
        self.fuzzy_table["NVB"] = dict()
        self.fuzzy_table["NVB"]["N"] = "NVVB"
        self.fuzzy_table["NVB"]["ZO"] = "NVB"
        self.fuzzy_table["NVB"]["P"] = "NB"
        self.fuzzy_table["NVB"]["PB"] = "N"
        self.fuzzy_table["NVB"]["NB"] = "NVVB"

    @staticmethod
    def fuzzify(x, left, middle, right):
        if left is not None and left <= x < middle:
            return (x - left) / (middle - left)
        elif right is not None and middle <= x < right:
            return (right - x) / (right - middle)
        elif left is None and x <= middle:
            return 1
        elif right is None and x >= middle:
            return 1
        else:
            return 0

    @staticmethod
    def compute_values(value, ranges):
        to_return = dict()
        for key in ranges:
            to_return[key] = Solver.fuzzify(value, *ranges[key])
        return to_return

    def solver(self, theta, omega):
        theta_values = self.compute_values(theta, self.theta_ranges)
        omega_values = self.compute_values(omega, self.omega_ranges)
        f_values = dict()
        for theta_key in self.fuzzy_table:
            for omega_key, f_value in self.fuzzy_table[theta_key].items():
                value = min(theta_values[theta_key], omega_values[omega_key])
                if f_value not in f_values:
                    f_values[f_value] = value
                else:
                    f_values[f_value] = max(value, f_values[f_value])
        s = sum(f_values.values())
        if s == 0:
            return None
        return sum(f_values[fSet] * self.bValues[fSet] for fSet in f_values.keys()) / s
