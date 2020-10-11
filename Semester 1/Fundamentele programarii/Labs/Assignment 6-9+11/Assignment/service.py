import re
from random import choice, randrange
from Repos.BaseRepos.StudentRepo import StudentRepo
from Repos.BaseRepos.DisciplineRepo import DisciplineRepo
from Repos.BaseRepos.GradesRepo import GradesRepo
from utils import *
from domain import *

from exceptions import *


class UndoObject:
    def __init__(self, undo_function, redo_function):
        """
        Object which will be inserted in the undo stack in service
        :param undo_function: function that calls the undo operation
        :param redo_function: function that calls the redo operation
        """
        self.undo_function = undo_function
        self.redo_function = redo_function


class UndoService:
    def __init__(self, student_repo, discipline_repo, grade_repo):
        """
        Service class which maintains the undo and redo operations for student, discipline and grade repos
        :param student_repo: StudentRepo
        :param discipline_repo: DisciplineRepo
        :param grade_repo: GradeRepo
        """
        self._student_repo = student_repo
        self._discipline_repo = discipline_repo
        self._grade_repo = grade_repo
        self._undo_stack = []
        self._undo_pointer = 0

    def register_operation(self, operation):
        """
        Registers the operation and pushes it onto the stack
        :param operation:
        :return:
        """
        self._normalise_stack()
        self._undo_stack.append(operation)
        self._undo_pointer += 1

    def _normalise_stack(self):
        """
        When an operation is executed that is not undo or redo everything beyond self._undo_pointer has to be
        eliminated from the stack. This is what the function does
        :return: None
        """
        while len(self._undo_stack) != self._undo_pointer:
            self._undo_stack.pop()

    def undo(self):
        """
        Undoes the last performed operation
        :return: None
        """
        if self._undo_pointer == 0:
            raise UndoBounds("No operations to undo")
        self._undo_pointer -= 1
        self._undo_stack[self._undo_pointer].undo_function()

    def redo(self):
        """
        Redoes the last undone operation
        :return: None
        """
        if self._undo_pointer == len(self._undo_stack):
            raise UndoBounds("No operations to redo")
        self._undo_stack[self._undo_pointer].redo_function()
        self._undo_pointer += 1


class Service:
    def __init__(self, student_repo=None, discipline_repo=None, grade_repo=None):
        self._student_repo = student_repo
        self._discipline_repo = discipline_repo
        self._grade_repo = grade_repo
        if student_repo is None:
            self._student_repo = StudentRepo(self._generate_students())
        if discipline_repo is None:
            self._discipline_repo = DisciplineRepo(self._generate_disciplines())
        if grade_repo is None:
            self._grade_repo = GradesRepo(self._generate_grades())
        self._undo_service = UndoService(self._student_repo, self._discipline_repo, self._grade_repo)

    @property
    def students(self):
        return self._student_repo.students

    @property
    def disciplines(self):
        return self._discipline_repo.disciplines

    @property
    def grades(self):
        return self._grade_repo.grades

    @staticmethod
    def _generate_students():
        """
        Generates a list with 10 random students
        :return: list with 10 objects of type Student
        """
        names = Student.list_of_names()
        students = []
        for i in range(min(30, len(names))):
            name = choice(names)
            names.remove(name)
            sid = randrange(1, 1000)
            while sid in [x.sid for x in students]:
                sid = randrange(1, 1000)
            students.append(Student(sid, name))
        return students

    @staticmethod
    def _generate_disciplines():
        """
        Generates a list with 10 random disciplines
        :return: list with 10 objects of type Discipline
        """
        names = Discipline.list_of_names()
        disciplines = []
        for i in range(min(20, len(names))):
            name = choice(names)
            names.remove(name)
            did = randrange(1, 1000)
            while did in [x.did for x in disciplines]:
                did = randrange(1, 1000)
            disciplines.append(Discipline(did, name))
        return disciplines

    def _generate_grades(self):
        """
        Generates a list with 10 random grades
        :return: list with 10 objects of type Grade
        """
        grades = []
        for i in range(100):
            sid = choice(self._student_repo.students).sid
            did = choice(self._discipline_repo.disciplines).did
            grade = randrange(0, 11)
            grades.append(Grade(sid, did, grade))
        return grades

    def find_student(self, sid):
        """
        Looks for a student with id=sid and returns one if it exists, None otherwise
        :param sid: int
        """
        return self._student_repo.find_student(sid)

    def find_discipline(self, did):
        """
        Looks for a discipline with id=did and returns one if it exists, None otherwise
        :param did: int
        """
        return self._discipline_repo.find_discipline(did)

    def add_student(self, sid, name):
        """
        Adds a student by calling the student repo
        :param sid: int
        :param name: string
        :return: None
        """
        student = Student(sid, name)
        if self._student_repo.find_student(sid):
            raise UniqueError("Student with id already exists")
        self._undo_service.register_operation(UndoObject(lambda: self._student_repo.rem_student(student),
                                                         lambda: self._student_repo.add_student(student)))
        self._student_repo.add_student(student)

    def add_discipline(self, did, name):
        """
        Adds a discipline by calling the discipline repo
        :param did: int
        :param name: string
        :return: None
        """
        discipline = Discipline(did, name)
        if self._discipline_repo.find_discipline(did):
            raise UniqueError("Discipline with id already exists")
        self._undo_service.register_operation(UndoObject(lambda: self._discipline_repo.rem_discipline(discipline),
                                                         lambda: self._discipline_repo.add_discipline(discipline)))
        self._discipline_repo.add_discipline(discipline)

    def rem_student(self, sid):
        """
        Removes the student with id=sid from the student_repo
        :param sid: integer
        :return: None
        """
        student = self._student_repo.find_student(sid)
        if not student:
            raise ExistenceError("No student with given id")
        grades = list(filter(lambda x: x.sid == sid, self._grade_repo.grades))

        def undo_function():
            self._student_repo.add_student(student)
            for grade in grades:
                self._grade_repo.add_grade(grade)

        def redo_function():
            self._student_repo.rem_student(student)
            for grade in grades:
                self._grade_repo.rem_grade(grade)

        self._undo_service.register_operation(UndoObject(undo_function, redo_function))
        self._student_repo.rem_student(student)
        for y in [x for x in self._grade_repo.grades if x.sid == sid]:
            self._grade_repo.rem_grade(y)

    def rem_discipline(self, did):
        """
        Removes the discipline with id=did from the discipline repo
        :param did:
        :return: None
        """
        discipline = self._discipline_repo.find_discipline(did)
        if not discipline:
            raise ExistenceError("No discipline with given id")
        grades = list(filter(lambda x: x.did == did, self._grade_repo.grades))

        def undo_function():
            self._discipline_repo.add_discipline(discipline)
            for grade in grades:
                self._grade_repo.add_grade(grade)

        def redo_function():
            self._discipline_repo.rem_discipline(discipline)
            for grade in grades:
                self._grade_repo.rem_grade(grade)

        self._undo_service.register_operation(UndoObject(undo_function, redo_function))
        self._discipline_repo.rem_discipline(discipline)
        for y in [x for x in self._grade_repo.grades if x.did == did]:
            self._grade_repo.rem_grade(y)

    def upd_student(self, sid, name):
        """
        Sets the name of student from student_repo with id=sid to name
        :param sid: integer
        :param name: string
        :return: None
        """
        student = self._student_repo.find_student(sid)
        if not student:
            raise ExistenceError("No student with given id")
        old_name = student.name
        self._undo_service.register_operation(
            UndoObject(lambda: self._student_repo.upd_student(sid, old_name),
                       lambda: self._student_repo.upd_student(sid, name))
        )
        self._student_repo.upd_student(sid, name)

    def upd_discipline(self, did, name):
        """
        Sets the name of discipline from discipline_repo with id=did to name
        :param did: integer
        :param name: string
        :return: None
        """
        discipline = self._discipline_repo.find_discipline(did)
        if not discipline:
            raise ExistenceError("No discipline with given id")
        old_name = discipline.name
        self._undo_service.register_operation(
            UndoObject(lambda: self._discipline_repo.upd_discipline(did, old_name),
                       lambda: self._discipline_repo.upd_discipline(did, name))
        )
        self._discipline_repo.upd_discipline(did, name)

    def grade_student(self, sid, did, grade_value):
        """
        Adds a grade to the list from grade_repo
        :param sid: int
        :param did: int
        :param grade_value: int
        :return: None
        """
        grade = Grade(sid, did, grade_value)
        if not self._student_repo.find_student(grade.sid):
            raise ExistenceError("No student with given id")
        if not self._discipline_repo.find_discipline(grade.did):
            raise ExistenceError("No discipline with given id")
        self._undo_service.register_operation(UndoObject(lambda: self._grade_repo.rem_grade(grade),
                                                         lambda: self._grade_repo.add_grade(grade)))
        self._grade_repo.add_grade(grade)

    def lookup_student(self, name):
        """
        Returns a list of all students who match the name with the parameter name, case insensitive
        :param name: string
        :return: list
        """
        return [student for student in self._student_repo.students if re.search(name, student.name, re.IGNORECASE)]

    def lookup_discipline(self, name):
        """
        Returns a list of all disciplines who match the name with the parameter name, case insensitive
        :param name: string
        :return: list
        """
        return [discipline for discipline in self._discipline_repo.disciplines
                if re.search(name, discipline.name, re.IGNORECASE)]

    def list_grades_to_string(self):
        """
        :return: list of user readable strings made from each grade in the list of grades
        """
        s = []
        for i in self._grade_repo.grades:
            s.append("Student {0} with id={1} has grade {2} at discipline {3} with id={4}".format(
                [x.name for x in self._student_repo.students if x.sid == i.sid][0],
                i.sid,
                i.grade,
                [x.name for x in self._discipline_repo.disciplines if x.did == i.did][0],
                i.did
            ))
        return s

    def _grouping_students(self):
        """
        Returns a dictionary with key as student.sid and value as another dictionary with key as discipline.did and
        value as the average grade at that discipline for that student
        :return: dict
        """
        d_cnt = {}
        d_sum = {}
        for g in self._grade_repo.grades:
            if g.sid not in d_cnt:
                d_cnt[g.sid] = {}
            if g.did not in d_cnt[g.sid]:
                d_cnt[g.sid][g.did] = 0
            if g.sid not in d_sum:
                d_sum[g.sid] = {}
            if g.did not in d_sum[g.sid]:
                d_sum[g.sid][g.did] = 0
            d_cnt[g.sid][g.did] += 1
            d_sum[g.sid][g.did] += g.grade
        for key in d_sum:
            for key2 in d_sum[key]:
                d_sum[key][key2] /= d_cnt[key][key2]
        return d_sum

    def students_failing(self):
        """
        Returns a list of triples(triples are lists with 3 elements), first element is student.sid, second element is
        discipline.did and third element is the grade of that student at that discipline. Returns only the triples with
        grade < 5(the failing grade)
        :return: list
        """
        d = self._grouping_students()
        v = []
        for key in d:
            for key2 in d[key]:
                if d[key][key2] < 5:
                    v.append([key, key2, d[key][key2]])
        return v

    def students_failing_to_string(self):
        """
        :return: list of user readable strings for each failing student
        """
        s = []
        for x in self.students_failing():
            s.append("Student {0} is failing at {1} with grade {2}".format(self.find_student(x[0]).name,
                                                                           self.find_discipline(x[1]).name,
                                                                           x[2]))
        return s

    def rank_students(self):
        """
        returns a list of pairs(pairs are lists of 2 elements), first element is student.sid, second element is
        weighted average grade of that student across all disciplines sorted in descending order of grade
        :return: list
        """
        d = self._grouping_students()
        v = []
        for key in d:
            v.append([key, 0])
            for key2 in d[key]:
                v[-1][1] += d[key][key2]
            v[-1][1] /= len(d[key])
        return sort(v, key=lambda x: x[1], reverse=True)

    def rank_students_to_string(self):
        """
        :return: list of user readable strings for the ranking of students
        """
        s = []
        for x in self.rank_students():
            s.append("Student {0} has an aggregated average of {1}".format(self.find_student(x[0]).name, x[1]))
        return s

    def rank_disciplines(self):
        """
        returns a list of pairs(pairs are lists of 2 elements), first element is discipline.sid, second element is
        average grade of that discipline across all grades sorted in descending order of grade
        :return: list
        """
        d = self._grouping_students()
        d2_sum = {}
        d2_cnt = {}
        for sid in d:
            for did in d[sid]:
                if did not in d2_sum:
                    d2_sum[did] = 0
                d2_sum[did] += d[sid][did]
                if did not in d2_cnt:
                    d2_cnt[did] = 0
                d2_cnt[did] += 1
        for did in d2_sum:
            d2_sum[did] /= d2_cnt[did]
        v = []
        for key, value in d2_sum.items():
            v.append([key, value])
        return sort(v, key=lambda x: x[1], reverse=True)

    def rank_disciplines_to_string(self):
        """
        :return: list of user readable strings for the ranking of disciplines
        """
        s = []
        for x in self.rank_disciplines():
            s.append("Discipline {0} has average grade of {1} across all students".format(
                self.find_discipline(x[0]).name, x[1]))
        return s

    def undo(self):
        """
        undo function from service
        """
        self._undo_service.undo()

    def redo(self):
        """
        redo function from service
        """
        self._undo_service.redo()
