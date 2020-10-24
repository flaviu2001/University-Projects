class Service:
    def __init__(self):
        self._students = []

    @property
    def students(self):
        return self._students

    @students.setter
    def students(self, new_list):
        self._students = new_list

    def add_student(self, student):
        """
        Adds a student to the list _students
        :param student: an object of type Student
        :return: None
        """
        for obj in self._students:
            if obj.sid == student.sid:
                raise ValueError("Student with the same id already exists")
        self._students.append(student)

    def filter_students(self, group):
        """
        Removes all students from the list _student with the given group
        :param group: int
        :return: None
        """
        self._students = list(filter(lambda x: x.group != group, self._students))
