from Repos.BaseRepos.DisciplineRepo import DisciplineRepo
from domain import Discipline
import pickle


class DisciplineRepoBinary(DisciplineRepo):
    def __init__(self, file_name):
        """
        Repository for disciplines using persistent storage powered by pickle binary files.
        :param file_name: string representing the location of the disciplines.pickle
        """
        DisciplineRepo.__init__(self)
        self.file_name = file_name
        file = open(self.file_name, "rb")
        for obj in pickle.load(file):
            self.add_discipline(Discipline(obj["did"], obj["name"]))
        file.close()

    def _save_file(self):
        """
        Function to save to file all disciplines as pickle objects
        :return:
        """
        file = open(self.file_name, "wb")
        pickle.dump([x.to_dict() for x in self._discipline_list], file)
        file.close()

    def add_discipline(self, discipline):
        DisciplineRepo.add_discipline(self, discipline)
        self._save_file()

    def upd_student(self, did, name):
        DisciplineRepo.upd_discipline(self, did, name)
        self._save_file()

    def rem_discipline(self, discipline):
        DisciplineRepo.rem_discipline(self, discipline)
        self._save_file()
