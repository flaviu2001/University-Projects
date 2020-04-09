from Repos.BaseRepos.GradesRepo import GradesRepo
from domain import Grade
import pickle


class GradesRepoBinary(GradesRepo):
    def __init__(self, file_name):
        """
        Repository for grades using persistent storage powered by pickle binary files.
        :param file_name: string representing the location of the grades.pickle
        """
        GradesRepo.__init__(self)
        self.file_name = file_name
        print(self._grade_list)
        file = open(self.file_name, "rb")
        data = pickle.load(file)
        for obj in data:
            self.add_grade(Grade(obj["sid"], obj["did"], obj["grade"]))
        file.close()

    def _save_file(self):
        """
        Function to save to file all grades as pickle objects
        :return:
        """
        file = open(self.file_name, "wb")
        pickle.dump([x.to_dict() for x in self._grade_list], file)
        file.close()

    def add_grade(self, grade):
        GradesRepo.add_grade(self, grade)
        self._save_file()

    def rem_grade(self, grade):
        GradesRepo.rem_grade(self, grade)
        self._save_file()
