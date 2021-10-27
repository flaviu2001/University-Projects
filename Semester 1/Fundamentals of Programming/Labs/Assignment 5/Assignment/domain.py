class Student:
    def __init__(self, sid, name, group):
        """
        Creates an object Student with sid(student id) integer, name string and group positive integer
        :param sid: int
        :param name: string
        :param group: positive int
        """
        if group <= 0:
            raise ValueError("Invalid group")
        self._sid = sid
        self._name = name
        self._group = group

    def __str__(self):
        return "Student " + self._name + " with id " + str(self._sid) + " is in group " + str(self._group)

    @property
    def sid(self):
        return self._sid

    @property
    def name(self):
        return self._name

    @property
    def group(self):
        return self._group

    @sid.setter
    def sid(self, value):
        self._sid = value

    @name.setter
    def name(self, value):
        self._name = value

    @group.setter
    def group(self, value):
        self._group = value

    @staticmethod
    def sample_students():
        return [Student(1, "Craciun Flaviu", 912),
                Student(2, "Anton Pann", 914),
                Student(3, "Elon Musk", 911),
                Student(4, "John Doe", 918),
                Student(5, "Max Payne", 916),
                Student(6, "Moldovanu Florin", 911),
                Student(7, "Tony Montana", 915),
                Student(8, "Gennady Korotkevich", 922),
                Student(9, "Ion Pop al Glanetasului", 919),
                Student(10, "Tyler Durden", 936)]
