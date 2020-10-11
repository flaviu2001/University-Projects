from domain import Discipline
import sqlite3


class DisciplineRepoDatabase:
    def __init__(self, file_name):
        self.connection = sqlite3.connect(file_name)
        self.cursor = self.connection.cursor()
        self.cursor.execute("""CREATE TABLE IF NOT EXISTS disciplines (did INTEGER PRIMARY KEY,
                                                                       name VARCHAR(100))""")
        self.connection.commit()

    @property
    def disciplines(self):
        self.cursor.execute("""SELECT * FROM disciplines;""")
        guys = self.cursor.fetchall()
        return [Discipline(x[0], x[1]) for x in guys]

    def find_discipline(self, did):
        self.cursor.execute("""SELECT * FROM disciplines WHERE did=?;""", (did,))
        guys = self.cursor.fetchall()
        if len(guys) == 0:
            return None
        return [Discipline(x[0], x[1]) for x in guys][0]

    def add_discipline(self, discipline):
        self.cursor.execute("""INSERT INTO disciplines VALUES (?, ?);""", (discipline.did, discipline.name))
        self.connection.commit()

    def rem_discipline(self, discipline):
        self.cursor.execute("""DELETE FROM disciplines WHERE did=?;""", (discipline.did,))
        self.connection.commit()

    def upd_discipline(self, did, name):
        self.cursor.execute("""UPDATE disciplines SET name= ? WHERE did=?;""", (name, did))
