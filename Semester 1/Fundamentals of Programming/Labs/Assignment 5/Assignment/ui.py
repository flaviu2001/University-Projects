from service import Service
from domain import Student
from copy import deepcopy
from tests import test_all


class UI:
    def __init__(self, service):
        self._service = service
        self._history = [Student.sample_students()]

    def ui_add_student(self):
        sid = input("Input the id of your student: ")
        try:
            sid = int(sid)
        except ValueError:
            print("Invalid number")
            return
        name = input("Input the name of your student: ")
        group = input("Input the group of your student: ")
        try:
            group = int(group)
        except ValueError:
            print("Invalid number")
            return
        try:
            self._service.add_student(Student(sid, name, group))
            self._history.append(self._service.students)
            print("Student successfully added\n")
        except Exception as e:
            print(e)

    def ui_show_students(self):
        print("The list has " + str(len(self._service.students)) + " students")
        for s in self._service.students:
            print(s)
        print()

    def ui_filter(self):
        group = input("Input the group to delete from list: ")
        try:
            group = int(group)
        except ValueError:
            print("Invalid number")
        self._service.filter_students(group)
        self._history.append(self._service.students)
        print("List successfully filtered\n")

    @staticmethod
    def print_menu():
        print("1. Add a new student to the list")
        print("2. Show the list")
        print("3. Remove the students with a certain group")
        print("4. Undo")
        print("5. Exit\n")

    def start(self):
        while True:
            UI.print_menu()
            choice = input("> ")
            self._service.students = deepcopy(self._history[-1])
            if choice == "1":
                self.ui_add_student()
            elif choice == "2":
                self.ui_show_students()
            elif choice == "3":
                self.ui_filter()
            elif choice == "4":
                if len(self._history) == 1:
                    print("There are no operations to undo\n")
                else:
                    self._history.pop()
                    print("Operation successfully undone\n")
            elif choice == "5":
                break
            else:
                print("Invalid choice\n")


test_all()
serv = Service()
ui = UI(serv)
ui.start()
