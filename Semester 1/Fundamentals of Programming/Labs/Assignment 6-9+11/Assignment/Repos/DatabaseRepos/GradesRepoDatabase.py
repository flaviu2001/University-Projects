from domain import Grade
import sqlite3


class GradesRepoDatabase:
    def __init__(self, file_name):
        self.connection = sqlite3.connect(file_name)
        self.cursor = self.connection.cursor()
        self.cursor.execute("""CREATE TABLE IF NOT EXISTS grades (internal_id INTEGER PRIMARY KEY,
                                                                  sid INTEGER,
                                                                  did INTEGER,
                                                                  grade INTEGER)""")
        self.connection.commit()
        self.cursor.execute("""SELECT MAX(internal_id) from grades""")
        self._increment = self.cursor.fetchall()[0][0]+1

    @property
    def grades(self):
        self.cursor.execute("""SELECT * FROM grades;""")
        guys = self.cursor.fetchall()
        return [Grade(x[1], x[2], x[3]) for x in guys]

    def add_grade(self, grade):
        self._increment += 1
        self.cursor.execute("""INSERT INTO grades VALUES (?, ?, ?, ?);""",
                            (self._increment, grade.sid, grade.did, grade.grade))
        self.connection.commit()

    def rem_grade(self, grade):
        self.cursor.execute("""DELETE FROM grades WHERE internal_id in
        (select internal_id from grades where sid=? and did=? and grade=? limit 1);""",
                            (grade.sid, grade.did, grade.grade))
        self.connection.commit()
