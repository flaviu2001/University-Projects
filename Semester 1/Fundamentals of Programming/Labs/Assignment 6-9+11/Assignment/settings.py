from configparser import ConfigParser
from Repos.BinaryRepos.StudentRepoBinary import StudentRepoBinary
from Repos.BinaryRepos.DisciplineRepoBinary import DisciplineRepoBinary
from Repos.BinaryRepos.GradesRepoBinary import GradesRepoBinary
from Repos.CSVRepos.StudentRepoCSV import StudentRepoCSV
from Repos.CSVRepos.DisciplineRepoCSV import DisciplineRepoCSV
from Repos.CSVRepos.GradesRepoCSV import GradesRepoCSV
from Repos.DatabaseRepos.StudentRepoDatabase import StudentRepoDatabase
from Repos.DatabaseRepos.DisciplineRepoDatabase import DisciplineRepoDatabase
from Repos.DatabaseRepos.GradesRepoDatabase import GradesRepoDatabase
from Repos.JsonRepos.StudentRepoJson import StudentRepoJson
from Repos.JsonRepos.DisciplineRepoJson import DisciplineRepoJson
from Repos.JsonRepos.GradesRepoJson import GradesRepoJson
from service import Service
from gui import GUI
from ui import UI


class Settings:
    def __init__(self):
        parser = ConfigParser()
        parser.read("files/settings.properties")
        self.ui = None
        if parser.get("options", "ui") == "console":
            self.ui = UI
        elif parser.get("options", "ui") == "gui":
            self.ui = GUI
        repo_style = parser.get("options", "repository")
        if repo_style == "memory":
            self.ui = self.ui(Service())
        elif repo_style == "json":
            self.ui = self.ui(Service(StudentRepoJson(parser.get("options", "students")),
                                      DisciplineRepoJson(parser.get("options", "disciplines")),
                                      GradesRepoJson(parser.get("options", "grades"))))
        elif repo_style == "binary":
            self.ui = self.ui(Service(StudentRepoBinary(parser.get("options", "students")),
                                      DisciplineRepoBinary(parser.get("options", "disciplines")),
                                      GradesRepoBinary(parser.get("options", "grades"))))
        elif repo_style == "csv":
            self.ui = self.ui(Service(StudentRepoCSV(parser.get("options", "students")),
                                      DisciplineRepoCSV(parser.get("options", "disciplines")),
                                      GradesRepoCSV(parser.get("options", "grades"))))
        elif repo_style == "database":
            self.ui = self.ui(Service(StudentRepoDatabase(parser.get("options", "database")),
                                      DisciplineRepoDatabase(parser.get("options", "database")),
                                      GradesRepoDatabase(parser.get("options", "database"))))

    def get_ui(self):
        return self.ui
