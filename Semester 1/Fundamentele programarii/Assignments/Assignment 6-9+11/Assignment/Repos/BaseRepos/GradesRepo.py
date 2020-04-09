from utils import *


class GradesRepo:
    def __init__(self, grade_list=None):
        if grade_list is None:
            grade_list = []
        if grade_list is Container:
            self._grade_list = grade_list
        else:
            self._grade_list = Container(grade_list)

    @property
    def grades(self):
        return self._grade_list

    def add_grade(self, grade):
        """
        Appends grade to self._grade_list
        :param grade: type Grade
        :return: None
        """
        self._grade_list.append(grade)

    def rem_grade(self, grade):
        """
        Removes a grade from self._grade_list
        :param grade: type Grade
        :return: None
        """
        self._grade_list.remove(grade)
