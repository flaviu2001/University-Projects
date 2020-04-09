from utils import *


class DisciplineRepo:
    def __init__(self, discipline_list=None):
        if discipline_list is None:
            discipline_list = []
        if discipline_list is Container:
            self._discipline_list = discipline_list
        else:
            self._discipline_list = Container(discipline_list)

    @property
    def disciplines(self):
        return self._discipline_list

    def find_discipline(self, did):
        """
        Iterates through self._discipline_list and if a discipline with id=did is found that object is returned,
        otherwise returns None
        :param did:
        :return: type Discipline if object is found, None otherwise
        """
        aux = my_filter(self._discipline_list, lambda x: x.did == did)
        # aux = [x for x in self._discipline_list if x.did == did]
        return None if len(aux) == 0 else aux[0]

    def add_discipline(self, discipline):
        """
        Adds a discipline of type Discipline to self._discipline_list
        :param discipline: type Discipline
        :return: None
        """
        self._discipline_list.append(discipline)

    def rem_discipline(self, discipline):
        """
        Removes the discipline
        :param discipline: type Discipline
        :return: None
        """
        self._discipline_list.remove(discipline)

    def upd_discipline(self, did, name):
        """
        Sets the name of discipline with id=did to name
        :param did: integer
        :param name: string
        :return: None
        """
        discipline = self.find_discipline(did)
        discipline.name = name
