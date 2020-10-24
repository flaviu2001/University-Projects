from utils import *


class StudentRepo:
    def __init__(self, student_list=None):
        if student_list is None:
            student_list = []
        if student_list is Container:
            self._student_list = student_list
        else:
            self._student_list = Container(student_list)

    @property
    def students(self):
        return self._student_list

    def find_student(self, sid):
        """
        Iterates through self.student_list and if a student with id=sid is found that object is returned,
        otherwise returns None
        :param sid:
        :return: type Student if object is found, None otherwise
        """
        aux = my_filter(self._student_list, lambda x: x.sid == sid)
        # aux = [x for x in self._student_list if x.sid == sid]
        return None if len(aux) == 0 else aux[0]

    def add_student(self, student):
        """
        Adds a student to self._student_list
        :param student: type Student
        :return: None
        """
        self._student_list.append(student)

    def rem_student(self, student):
        """
        Removes the student
        :param student: type Student
        :return: None
        """
        self._student_list.remove(student)

    def upd_student(self, sid, name):
        """
        Sets the name of student with id=sid to name
        :param sid: integer
        :param name: string
        :return: None
        """
        student = self.find_student(sid)
        student.name = name
