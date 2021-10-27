import unittest

from exceptions import *
from service import Service
from domain import *


class Tests(unittest.TestCase):
    def test_add_student(self):
        service = Service()
        service._student_repo._student_list = []
        service.add_student(1, "S1")
        self.assertEqual(len(service.students), 1)
        self.assertEqual(service.students[0].sid, 1)
        self.assertEqual(service.students[0].name, "S1")
        with self.assertRaises(UniqueError):
            service.add_student(1, "S2")

    def test_add_discipline(self):
        service = Service()
        service._discipline_repo._discipline_list = []
        service.add_discipline(1, "D1")
        self.assertEqual(len(service.disciplines), 1)
        self.assertEqual(service.disciplines[0].did, 1)
        self.assertEqual(service.disciplines[0].name, "D1")
        with self.assertRaises(UniqueError):
            service.add_discipline(1, "D2")

    def test_rem_student(self):
        service = Service()
        service._student_repo._student_list = []
        service.add_student(1, "S1")
        service.rem_student(1)
        self.assertEqual(len(service.students), 0)
        with self.assertRaises(ExistenceError):
            service.rem_student(2)

    def test_rem_discipline(self):
        service = Service()
        service._discipline_repo._discipline_list = []
        service.add_discipline(1, "D1")
        service.rem_discipline(1)
        self.assertEqual(len(service.disciplines), 0)
        with self.assertRaises(ExistenceError):
            service.rem_discipline(2)

    def test_upd_student(self):
        service = Service()
        service._student_repo._student_list = []
        service.add_student(1, "S1")
        service.upd_student(1, "S2")
        self.assertEqual(service.students[0].name, "S2")
        with self.assertRaises(ExistenceError):
            service.upd_student(2, "S3")

    def test_upd_discipline(self):
        service = Service()
        service._discipline_repo._discipline_list = []
        service.add_discipline(1, "D1")
        service.upd_discipline(1, "D2")
        self.assertEqual(service.disciplines[0].name, "D2")
        with self.assertRaises(ExistenceError):
            service.upd_discipline(2, "D3")

    def test_grade_student(self):
        service = Service()
        service._student_repo._student_list = []
        service._discipline_repo._discipline_list = []
        service._grade_repo._grade_list = []
        service.add_student(1, "S1")
        service.add_discipline(2, "D1")
        service.grade_student(1, 2, 10)
        self.assertEqual(len(service.grades), 1)
        self.assertEqual(service.grades[0], Grade(1, 2, 10))
        self.assertEqual(len(service.list_grades_to_string()), len(service.grades))
        with self.assertRaises(ExistenceError):
            service.grade_student(2, 2, 10)
        with self.assertRaises(ExistenceError):
            service.grade_student(1, 1, 10)

    def test_domain_student(self):
        student = Student(1, "Thomas")
        self.assertEqual(student.sid, 1)
        self.assertEqual(student.name, "Thomas")

    def test_domain_discipline(self):
        discipline = Discipline(1, "Math")
        self.assertEqual(discipline.did, 1)
        self.assertEqual(discipline.name, "Math")

    def test_domain_grade(self):
        grade = Grade(14, 22, 10)
        self.assertEqual(grade.sid, 14)
        self.assertEqual(grade.did, 22)
        self.assertEqual(grade.grade, 10)
        with self.assertRaises(InvalidGrade):
            Grade(1, 2, 11)
        with self.assertRaises(InvalidGrade):
            Grade(2, 3, -2)

    def test_domain_equality(self):
        stud1 = Student(1, "Dany")
        stud2 = Student(1, "Dany")
        stud3 = Student(2, "Dana")
        self.assertEqual(stud1, stud2)
        self.assertNotEqual(stud1, stud3)
        disc1 = Discipline(1, "Maths")
        disc2 = Discipline(1, "Maths")
        disc3 = Discipline(2, "English")
        self.assertEqual(disc1, disc2)
        self.assertNotEqual(disc1, disc3)
        self.assertNotEqual(stud1, disc1)
        self.assertNotEqual(disc2, stud2)

    def test_student_repr(self):
        student = Student(2, "Annabelle")
        self.assertEqual(str(student), "Student Annabelle with id 2")

    def test_discipline_repr(self):
        discipline = Discipline(1, "Math")
        self.assertEqual(str(discipline), "Discipline Math with id 1")

    def test_lookup_student(self):
        service = Service()
        service._student_repo._student_list = [s1:=Student(1, "Jack"), Student(2, "Janny")]
        self.assertEqual(len(service.lookup_student("jac")), 1)
        self.assertEqual(service.find_student(1), s1)

    def test_lookup_discipline(self):
        service = Service()
        service._discipline_repo._discipline_list = [d1:=Discipline(1, "Math"), Discipline(2, "maths")]
        self.assertEqual(len(service.lookup_discipline("math")), 2)
        self.assertEqual(service.find_discipline(1), d1)

    def test_undo(self):
        service = Service()
        service._student_repo._student_list = []
        service._discipline_repo._discipline_list = []
        service._grade_repo._grade_list = []
        with self.assertRaises(UndoBounds):
            service._undo_service.undo()
        service.add_student(1, "Name")
        service.add_discipline(10, "Name2")
        service.undo()
        self.assertEqual(len(service.disciplines), 0)
        service.redo()
        with self.assertRaises(UndoBounds):
            service.redo()
        self.assertEqual(len(service.disciplines), 1)
        service.undo()
        service.undo()
        service.redo()
        service.redo()
        self.assertEqual(len(service.students), 1)
        service.upd_student(1, "Ion")
        service.undo()
        self.assertEqual(service.students[0].name, "Name")
        service.upd_discipline(10, "Mate")
        service.undo()
        self.assertEqual(service.disciplines[0].name, "Name2")
        service.grade_student(1, 10, 2)
        service.undo()
        self.assertEqual(len(service._grade_repo.grades), 0)
        service.redo()
        self.assertEqual(len(service._grade_repo.grades), 1)
        service._student_repo._student_list = []
        service._discipline_repo._discipline_list = []
        service._grade_repo._grade_list = []
        service.add_student(101, "ion")
        service.add_discipline(101, "mate2")
        service.grade_student(101, 101, 10)
        service.rem_student(101)
        self.assertEqual(len(service.grades), 0)
        service.undo()
        self.assertEqual(len(service.grades), 1)
        service.redo()
        self.assertEqual(len(service.grades), 0)
        service.undo()
        service.rem_discipline(101)
        self.assertEqual(len(service.grades), 0)
        service.undo()
        self.assertEqual(len(service.grades), 1)
        service.redo()
        self.assertEqual(len(service.grades), 0)

    def test_statistics(self):
        service = Service()
        service._student_repo._student_list = []
        service._discipline_repo._discipline_list = []
        service._grade_repo._grade_list = []
        service.add_student(1, "Name")
        service.add_discipline(10, "Name2")
        service.grade_student(1, 10, 2)
        self.assertEqual(service.rank_disciplines(), [[10, 2]])
        self.assertEqual(service.rank_students(), [[1, 2]])
        self.assertEqual(service.students_failing(), [[1, 10, 2]])
        self.assertEqual(len(service.rank_disciplines()), len(service.rank_disciplines_to_string()))
        self.assertEqual(len(service.rank_students()), len(service.rank_students_to_string()))
        self.assertEqual(len(service.students_failing()), len(service.students_failing_to_string()))
