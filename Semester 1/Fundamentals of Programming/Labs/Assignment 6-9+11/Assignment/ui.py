from service import Service


class UI:
    def __init__(self, service=None):
        if service is None:
            service = Service()
        self._service = service

    @staticmethod
    def list_students(students):
        print("There are " + str(len(students)) + " students in total.")
        for i in students:
            print(i)

    @staticmethod
    def list_disciplines(disciplines):
        print("There are " + str(len(disciplines)) + " disciplines in total")
        for i in disciplines:
            print(i)

    def list_grades(self):
        s = self._service.list_grades_to_string()
        print("There are " + str(len(s)) + " grades in total")
        for i in s:
            print(i+"\n")

    def ui_add_student(self):
        try:
            sid = int(input("Input the id of your new student: "))
            name = input("Input the name of your new student: ")
            self._service.add_student(sid, name)
            print("Student successfully added!")
        except Exception as e:
            print(e)

    def ui_add_discipline(self):
        try:
            did = int(input("Input the id of your new discipline: "))
            name = input("Input the name of your new discipline: ")
            self._service.add_discipline(did, name)
            print("Discipline successfully added!")
        except Exception as e:
            print(e)

    def ui_rem_student(self):
        try:
            sid = int(input("Input the id of the student you want to remove: "))
            self._service.rem_student(sid)
            print("Student successfully removed!")
        except Exception as e:
            print(e)

    def ui_rem_discipline(self):
        try:
            did = int(input("Input the id of the discipline you want to remove: "))
            self._service.rem_discipline(did)
            print("Discipline successfully removed!")
        except Exception as e:
            print(e)

    def ui_upd_student(self):
        try:
            sid = int(input("Input the id of the student you want to update: "))
            name = input("Input the new name: ")
            self._service.upd_student(sid, name)
            print("Student successfully updated!")
        except Exception as e:
            print(e)

    def ui_upd_discipline(self):
        try:
            did = int(input("Input the id of the discipline you want to update: "))
            name = input("Input the new name: ")
            self._service.upd_discipline(did, name)
            print("Discipline successfully updated!")
        except Exception as e:
            print(e)

    def ui_undo(self):
        try:
            self._service.undo()
            print("Operation successfully undone!")
        except Exception as e:
            print(e)

    def ui_redo(self):
        try:
            self._service.redo()
            print("Operation successfully undone!")
        except Exception as e:
            print(e)

    def ui_grade_student(self):
        try:
            sid = int(input("Input the id of the student you want to grade: "))
            did = int(input("Input the id of the discipline: "))
            number = int(input("Input the grade you wish to give your student: "))
            self._service.grade_student(sid, did, number)
            print("Student successfully graded!")
        except Exception as e:
            print(e)

    def ui_lookup_student(self):
        name = input("Input the name of the student you wish to look up: ")
        self.list_students(self._service.lookup_student(name))

    def ui_lookup_discipline(self):
        name = input("Input the name of the discipline you wish to look up: ")
        self.list_disciplines(self._service.lookup_discipline(name))

    def ui_find_student_by_id(self):
        try:
            sid = int(input("Input the id of the student you wish to find: "))
            student = self._service.find_student(sid)
            if not student:
                print("No student with such id")
            else:
                print(student)
        except Exception as e:
            print(e)

    def ui_find_discipline_by_id(self):
        try:
            did = int(input("Input the id of the discipline you wish to find: "))
            discipline = self._service.find_discipline(did)
            if not discipline:
                print("No discipline with such id")
            else:
                print(discipline)
        except Exception as e:
            print(e)

    def ui_manage_lists(self):
        self._print_submenu1()
        choice2 = input("> ")
        if choice2 == "1":
            self.ui_add_student()
        elif choice2 == "2":
            self.ui_add_discipline()
        elif choice2 == "3":
            self.ui_rem_student()
        elif choice2 == "4":
            self.ui_rem_discipline()
        elif choice2 == "5":
            self.ui_upd_student()
        elif choice2 == "6":
            self.ui_upd_discipline()
        elif choice2 == "7":
            self.list_students(self._service.students)
        elif choice2 == "8":
            self.list_disciplines(self._service.disciplines)
        elif choice2 == "0":
            pass
        else:
            print("Invalid choice")

    def ui_statistics(self):
        self._print_submenu8()
        choice2 = input("> ")
        if choice2 == "1":
            s = self._service.students_failing_to_string()
            for x in s:
                print(x)
            if len(s) == 0:
                print("There are no students failing")
        elif choice2 == "2":
            for x in self._service.rank_students_to_string():
                print(x)
        elif choice2 == "3":
            for x in self._service.rank_disciplines_to_string():
                print(x)
        elif choice2 == "0":
            pass
        else:
            print("Invalid choice")

    @staticmethod
    def _print_submenu1():
        print("\n1. Add a student")
        print("2. Add a discipline")
        print("3. Remove a student")
        print("4. Remove a discipline")
        print("5. Update a student")
        print("6. Update a discipline")
        print("7. List the students")
        print("8. List the disciplines")
        print("0. Abandon\n")

    @staticmethod
    def _print_submenu8():
        print("\n1. All students failing at one or more disciplines")
        print("2. Students with the best situation")
        print("3. Average grade for each discipline for disciplines which have at least one grade")
        print("0. Abandon\n")

    @staticmethod
    def _print_menu():
        print("\n1. Manage the list of students and available disciplines")
        print("2. Grade a student")
        print("3. View all grades")
        print("4. Search for a student by name")
        print("5. Search for a student by id")
        print("6. Search for a discipline by name")
        print("7. Search for a discipline by id")
        print("8. Create statistics")
        print("9. Undo")
        print("10. Redo")
        print("0. Exit the program\n")

    def start(self):
        print("Welcome to my students register management app!")
        while True:
            self._print_menu()
            choice = input("> ")
            if choice == "1":
                self.ui_manage_lists()
            elif choice == "2":
                self.ui_grade_student()
            elif choice == "3":
                self.list_grades()
            elif choice == "4":
                self.ui_lookup_student()
            elif choice == "5":
                self.ui_find_student_by_id()
            elif choice == "6":
                self.ui_lookup_discipline()
            elif choice == "7":
                self.ui_find_discipline_by_id()
            elif choice == "8":
                self.ui_statistics()
            elif choice == "9":
                self.ui_undo()
            elif choice == "10":
                self.ui_redo()
            elif choice == "0":
                print("Thanks for using my program!")
                break
            else:
                print("Invalid choice")
