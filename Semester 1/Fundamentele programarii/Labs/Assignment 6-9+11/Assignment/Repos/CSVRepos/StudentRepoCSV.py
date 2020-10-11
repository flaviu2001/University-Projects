from Repos.BaseRepos.StudentRepo import StudentRepo
from domain import Student


class StudentRepoCSV(StudentRepo):
    def __init__(self, file_name):
        """
        Repository for students using persistent storage powered by csv text files.
        :param file_name: string representing the location of the students.csv
        """
        StudentRepo.__init__(self)
        self.file_name = file_name
        file = open(self.file_name, "r")
        for line in file.readlines():
            line = line.strip(" \n")
            stuff = line.split(",")
            self.add_student(Student(int(stuff[0]), stuff[1]))
        file.close()

    def _save_file(self):
        """
        Function to save to file all students as csv objects
        """
        file = open(self.file_name, "w")
        for student in self._student_list:
            file.write("{0},{1}\n".format(student.sid, student.name))
        file.close()

    def add_student(self, student):
        StudentRepo.add_student(self, student)
        self._save_file()

    def upd_student(self, sid, name):
        StudentRepo.upd_student(self, sid, name)
        self._save_file()

    def rem_student(self, student):
        StudentRepo.rem_student(self, student)
        self._save_file()
