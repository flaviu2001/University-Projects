from Repos.BaseRepos.DisciplineRepo import DisciplineRepo
from domain import Discipline


class DisciplineRepoCSV(DisciplineRepo):
    def __init__(self, file_name):
        """
        Repository for disciplines using persistent storage powered by csv text files.
        :param file_name: string representing the location of the disciplines.csv
        """
        DisciplineRepo.__init__(self)
        self.file_name = file_name
        file = open(self.file_name, "r")
        for line in file.readlines():
            line = line.strip(" \n")
            stuff = line.split(",")
            self.add_discipline(Discipline(int(stuff[0]), stuff[1]))
        file.close()

    def _save_file(self):
        """
        Function to save to file all discipline as csv objects
        """
        file = open(self.file_name, "w")
        for discipline in self._discipline_list:
            file.write("{0},{1}\n".format(discipline.did, discipline.name))
        file.close()

    def add_discipline(self, discipline):
        DisciplineRepo.add_discipline(self, discipline)
        self._save_file()

    def upd_discipline(self, did, name):
        DisciplineRepo.upd_discipline(self, did, name)
        self._save_file()

    def rem_discipline(self, discipline):
        DisciplineRepo.rem_discipline(self, discipline)
        self._save_file()
