from Repos.BaseRepos.StudentRepo import StudentRepo
from domain import Student
import json


class StudentRepoJson(StudentRepo):
    def __init__(self, file_name):
        """
        Repository for students using persistent storage powered by json text files.
        :param file_name: string representing the location of the students.json
        """
        StudentRepo.__init__(self)
        self.file_name = file_name
        file = open(self.file_name, "r")
        for obj in json.load(file):
            self.add_student(Student(obj["sid"], obj["name"]))
        file.close()

    def _save_file(self):
        """
        Function to save to file all students as json objects
        """
        file = open(self.file_name, "w")
        json.dump([x.to_dict() for x in self._student_list], file, indent=4)
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
