from Repos.BaseRepos.GradesRepo import GradesRepo
from domain import Grade
import json


class GradesRepoJson(GradesRepo):
    def __init__(self, file_name):
        """
        Repository for grades using persistent storage powered by json text files.
        :param file_name: string representing the location of the grades.json
        """
        GradesRepo.__init__(self)
        self.file_name = file_name
        file = open(self.file_name, "r")
        data = json.load(file)
        for obj in data:
            self.add_grade(Grade(obj["sid"], obj["did"], obj["grade"]))
        file.close()

    def _save_file(self):
        """
        Function to save to file all grades as json objects
        :return:
        """
        file = open(self.file_name, "w")
        json.dump([x.to_dict() for x in self._grade_list], file, indent=4)
        file.close()

    def add_grade(self, grade):
        GradesRepo.add_grade(self, grade)
        self._save_file()

    def rem_grade(self, grade):
        GradesRepo.rem_grade(self, grade)
        self._save_file()
