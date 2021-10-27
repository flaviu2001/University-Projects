from domain import Student
import sqlite3


class StudentRepoDatabase:
    def __init__(self, file_name):
        self.connection = sqlite3.connect(file_name)
        self.cursor = self.connection.cursor()
        self.cursor.execute("""CREATE TABLE IF NOT EXISTS students (sid INTEGER PRIMARY KEY,
                                                                    name VARCHAR(30))""")
        self.connection.commit()

    @property
    def students(self):
        self.cursor.execute("""SELECT * FROM students;""")
        guys = self.cursor.fetchall()
        return [Student(x[0], x[1]) for x in guys]

    def find_student(self, sid):
        self.cursor.execute("""SELECT * FROM students WHERE sid=?;""", (sid,))
        guys = self.cursor.fetchall()
        if len(guys) == 0:
            return None
        return [Student(x[0], x[1]) for x in guys][0]

    def add_student(self, student):
        self.cursor.execute("""INSERT INTO students VALUES (?, ?);""", (student.sid, student.name))
        self.connection.commit()

    def rem_student(self, student):
        self.cursor.execute("""DELETE FROM students WHERE sid=?;""", (student.sid,))
        self.connection.commit()

    def upd_student(self, sid, name):
        self.cursor.execute("""UPDATE students SET name= ? WHERE sid=?;""", (name, sid))
