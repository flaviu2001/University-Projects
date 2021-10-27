from Repos.BaseRepos.GradesRepo import GradesRepo
from domain import Grade


class GradesRepoCSV(GradesRepo):
    def __init__(self, file_name):
        """
        Repository for grades using persistent storage powered by csv text files.
        :param file_name: string representing the location of the grades.csv
        """
        GradesRepo.__init__(self)
        self.file_name = file_name
        file = open(self.file_name, "r")
        for line in file.readlines():
            line = line.strip(" \n")
            stuff = line.split(",")
            self.add_grade(Grade(int(stuff[0]), int(stuff[1]), int(stuff[2])))
        file.close()

    def _save_file(self):
        """
        Function to save to file all grades as csv objects
        :return:
        """
        file = open(self.file_name, "w")
        for grade in self._grade_list:
            file.write("{0},{1},{2}\n".format(grade.sid, grade.did, grade.grade))
        file.close()

    def add_grade(self, grade):
        GradesRepo.add_grade(self, grade)
        self._save_file()

    def rem_grade(self, grade):
        GradesRepo.rem_grade(self, grade)
        self._save_file()
