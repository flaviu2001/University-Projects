from exceptions import InvalidGrade


class Student:
    def __init__(self, sid, name):
        """
        Creates a student with a given id and name
        :param sid: integer
        :param name: string
        """
        self._sid = sid
        self._name = name

    def __eq__(self, other):
        if not isinstance(other, Student):
            return False
        return self.sid == other.sid

    def __repr__(self):
        return "Student " + self._name + " with id " + str(self._sid)

    def to_dict(self):
        return {"sid": self._sid, "name": self._name}

    @property
    def sid(self):
        return self._sid

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, x):
        self._name = x

    @staticmethod
    def list_of_names():
        try:
            file = open("files/student names.txt", "r")
            names = []
            for x in file.readlines():
                names.append(x[:-1])
            file.close()
            return names
        except FileNotFoundError:
            return ["Jack",
                    "Joe",
                    "Ann",
                    "Mary",
                    "Julie",
                    "Anthony",
                    "Dave",
                    "Lucy",
                    "Allison",
                    "Moriarty",
                    "Keith",
                    "Neil",
                    "Michael",
                    "Dany"]


class Discipline:
    def __init__(self, did, name):
        """
        Creates a discipline with a given id and name
        :param did: integer
        :param name: string
        """
        self._did = did
        self._name = name

    def __eq__(self, other):
        if not isinstance(other, Discipline):
            return False
        return self.did == other.did

    def __repr__(self):
        return "Discipline " + self._name + " with id " + str(self._did)

    def to_dict(self):
        return {"did": self._did, "name": self._name}

    @property
    def did(self):
        return self._did

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, x):
        self._name = x

    @staticmethod
    def list_of_names():
        try:
            file = open("files/discipline names.txt", "r")
            names = []
            for x in file.readlines():
                names.append(x[:-1])
            file.close()
            return names
        except FileNotFoundError:
            return ["Math",
                    "English",
                    "Architecture",
                    "Logic",
                    "Geometry",
                    "Algebra",
                    "Analysis",
                    "Operating systems",
                    "Programming fundamentals",
                    "Spanish",
                    "Physical education",
                    "Graph theory",
                    "Machine learning",
                    "Physics"]


class Grade:
    def __init__(self, sid, did, grade_value):
        """
        Creates a grade with student id = sid, discipline id = did and grade value
        :param sid: integer
        :param did: integer
        :param grade_value: integer
        """
        if grade_value not in range(0, 11):
            raise InvalidGrade("Grade not in range 0, 10")
        self._sid = sid
        self._did = did
        self._grade_value = grade_value

    def __eq__(self, other):
        return self.sid == other.sid and self.did == other.did and self.grade == other.grade

    def to_dict(self):
        return {"sid": self._sid, "did": self._did, "grade": self._grade_value}

    @property
    def sid(self):
        return self._sid

    @property
    def did(self):
        return self._did

    @property
    def grade(self):
        return self._grade_value
