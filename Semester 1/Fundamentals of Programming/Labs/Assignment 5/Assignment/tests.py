from service import Service
from domain import Student


def test_add_student(serv=Service()):
    serv.students = []
    serv.add_student(Student(1, "test", 911))
    assert len(serv.students) == 1
    try:
        serv.add_student(Student(1, "test2", 912))
        assert False
    except ValueError:
        assert True


def test_filter_student(serv=Service()):
    serv.students = [Student(1, "t1", 911), Student(2, "t2", 912), Student(3, "t3", 914)]
    serv.filter_students(911)
    assert len(serv.students) == 2
    serv.filter_students(913)
    assert len(serv.students) == 2


def test_all():
    serv = Service()
    test_add_student(serv)
    test_filter_student(serv)
