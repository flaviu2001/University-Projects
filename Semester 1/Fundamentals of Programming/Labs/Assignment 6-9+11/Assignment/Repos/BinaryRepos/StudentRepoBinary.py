from Repos.BaseRepos.StudentRepo import StudentRepo
from domain import Student
import pickle


class StudentRepoBinary(StudentRepo):
    def __init__(self, file_name):
        """
        Repository for students using persistent storage powered by pickle binary files.
        :param file_name: string representing the location of the students.pickle
        """
        StudentRepo.__init__(self)
        self.file_name = file_name
        file = open(self.file_name, "rb")
        for obj in pickle.load(file):
            self.add_student(Student(obj["sid"], obj["name"]))
        file.close()

    def _save_file(self):
        """
        Function to save to file all students as pickle objects
        :return:
        """
        file = open(self.file_name, "wb")
        pickle.dump([x.to_dict() for x in self._student_list], file)
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
