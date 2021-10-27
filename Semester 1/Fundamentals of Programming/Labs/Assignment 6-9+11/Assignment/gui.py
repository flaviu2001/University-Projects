import tkinter as tk
from tkinter import END
from tkinter import font as tkfont

from service import Service


class TextScrollCombo(tk.Frame):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.grid_propagate(False)
        self.grid_rowconfigure(0, weight=1)
        self.grid_columnconfigure(0, weight=1)
        self.txt = tk.Text(self)
        self.txt.grid(row=0, column=0, sticky="nsew", padx=2, pady=2)
        scroll = tk.Scrollbar(self, command=self.txt.yview)
        scroll.grid(row=0, column=1, sticky='nsew')
        self.configure(bg="#fceda9")
        self.txt.configure(bg="#fceda9")
        self.txt['yscrollcommand'] = scroll.set
        self.txt.insert(1.0, "Welcome to my student grading app!")


class StatisticsPage(tk.Frame):
    def __init__(self, parent, controller, gui, textbox):
        tk.Frame.__init__(self, parent)
        self.configure(bg="#39d668")
        self.controller = controller
        self.button_height = 0.05
        self.button_width = 0.25
        self.color_page = "#39d668"
        self.color_bg = "#d2b0fc"
        self.color_active = "#bc88fc"
        self.gui = gui
        self.textbox = textbox
        starting_point = 0.2
        space_between = 0.06
        back_button = tk.Button(self, text="Back", font=40,
                                command=lambda: self.controller.show_frame("StartPage"),
                                bg="#fcb088", activebackground="#fc9d6a",
                                highlightbackground=self.color_page)
        back_button.place(relx=0.03, rely=0.03, relheight=0.05, relwidth=0.1)
        button_failing = tk.Button(self, text="Students failing", font=40,
                                   command=self.failing_action,
                                   bg=self.color_bg, activebackground=self.color_active,
                                   highlightbackground=self.color_page)
        button_failing.place(relx=0.1, rely=starting_point,
                             relheight=self.button_height, relwidth=self.button_width)
        button_rank_students = tk.Button(self, text="Rank the students", font=40,
                                         command=self.rank_students_action,
                                         bg=self.color_bg, activebackground=self.color_active,
                                         highlightbackground=self.color_page)
        button_rank_students.place(relx=0.1, rely=starting_point + space_between,
                                   relheight=self.button_height, relwidth=self.button_width)
        button_rank_disciplines = tk.Button(self, text="Rank the disciplines", font=40,
                                            command=self.rank_disciplines_action,
                                            bg=self.color_bg, activebackground=self.color_active,
                                            highlightbackground=self.color_page)
        button_rank_disciplines.place(relx=0.1, rely=starting_point + space_between * 2,
                                      relheight=self.button_height, relwidth=self.button_width)

    def failing_action(self):
        self.gui.gui_students_failing(self.textbox)
        self.controller.show_frame("StartPage")

    def rank_students_action(self):
        self.gui.gui_rank_students(self.textbox)
        self.controller.show_frame("StartPage")

    def rank_disciplines_action(self):
        self.gui.gui_rank_disciplines(self.textbox)
        self.controller.show_frame("StartPage")


class SearchPage(tk.Frame):
    def __init__(self, parent, controller, gui, textbox):
        tk.Frame.__init__(self, parent)
        self.configure(bg="#39d668")
        self.controller = controller
        self.button_height = 0.05
        self.button_width = 0.25
        self.color_page = "#39d668"
        self.color_bg = "#d2b0fc"
        self.color_active = "#bc88fc"
        self.id_height = self.button_height
        self.id_width = 0.1
        self.name_width = 0.3
        self.gui = gui
        self.textbox = textbox
        starting_point = 0.2
        space_between = 0.06
        back_button = tk.Button(self, text="Back", font=40,
                                command=lambda: self.controller.show_frame("StartPage"),
                                bg="#fcb088", activebackground="#fc9d6a",
                                highlightbackground=self.color_page)
        back_button.place(relx=0.03, rely=0.03, relheight=0.05, relwidth=0.1)
        self.id_field(starting_point, self.search_student_id, "Search student by id")
        self.name_field(starting_point + space_between, self.search_student_name, "Search student by name")
        self.id_field(starting_point + space_between * 2, self.search_discipline_id, "Search discipline by id")
        self.name_field(starting_point + space_between * 3, self.search_discipline_name, "Search discipline by name")

    def search_student_id(self, entry_id):
        self.gui.gui_search_student_id(self.textbox, entry_id.get())
        entry_id.delete(0, END)
        self.controller.show_frame("StartPage")

    def search_student_name(self, entry_name):
        self.gui.gui_search_student_name(self.textbox, entry_name.get())
        entry_name.delete(0, END)
        self.controller.show_frame("StartPage")

    def search_discipline_id(self, entry_id):
        self.gui.gui_search_discipline_id(self.textbox, entry_id.get())
        entry_id.delete(0, END)
        self.controller.show_frame("StartPage")

    def search_discipline_name(self, entry_name):
        self.gui.gui_search_discipline_name(self.textbox, entry_name.get())
        entry_name.delete(0, END)
        self.controller.show_frame("StartPage")

    # noinspection DuplicatedCode
    def id_field(self, posy, function, button_text):
        entry_id = tk.Entry(self, font=40, bg=self.color_bg, highlightbackground=self.color_page)
        entry_id.place(relx=0.11 + self.button_width, rely=posy, relheight=self.id_height, relwidth=self.id_width)
        entry_id.insert(0, "Id: ")
        entry_id.bind("<FocusIn>", lambda args: entry_id.delete(0, END) if entry_id.get() == "Id: " else None)
        entry_id.bind("<FocusOut>", lambda args: entry_id.insert(0, "Id: ") if entry_id.get() == "" else None)
        button = tk.Button(self, text=button_text, font=40,
                           command=lambda: function(entry_id),
                           bg=self.color_bg, activebackground=self.color_active, highlightbackground=self.color_page)
        button.place(relx=0.1, rely=posy, relheight=self.button_height, relwidth=self.button_width)

    # noinspection DuplicatedCode
    def name_field(self, posy, function, button_text):
        entry_name = tk.Entry(self, font=40, bg=self.color_bg, highlightbackground=self.color_page)
        entry_name.place(relx=0.11 + self.button_width, rely=posy, relheight=self.id_height, relwidth=self.name_width)
        entry_name.insert(0, "Name: ")
        entry_name.bind("<FocusIn>", lambda args: entry_name.delete(0, END) if entry_name.get() == "Name: " else None)
        entry_name.bind("<FocusOut>", lambda args: entry_name.insert(0, "Name: ") if entry_name.get() == "" else None)
        button = tk.Button(self, text=button_text, font=40,
                           command=lambda: function(entry_name),
                           bg=self.color_bg, activebackground=self.color_active, highlightbackground=self.color_page)
        button.place(relx=0.1, rely=posy, relheight=self.button_height, relwidth=self.button_width)


class ManagePage(tk.Frame):
    def __init__(self, parent, controller, gui, textbox):
        tk.Frame.__init__(self, parent)
        self.configure(bg="#39d668")
        self.controller = controller
        self.button_height = 0.05
        self.button_width = 0.25
        self.color_page = "#39d668"
        self.color_bg = "#d2b0fc"
        self.color_active = "#bc88fc"
        self.id_height = self.button_height
        self.id_width = 0.1
        self.gui = gui
        self.textbox = textbox
        back_button = tk.Button(self, text="Back", font=40,
                                command=lambda: self.controller.show_frame("StartPage"),
                                bg="#fcb088", activebackground="#fc9d6a",
                                highlightbackground=self.color_page)
        back_button.place(relx=0.03, rely=0.03, relheight=0.05, relwidth=0.1)
        starting_point = 0.2
        space_between = 0.06
        self.id_name_field(starting_point, self.student_add_action, "Add a student")
        self.id_name_field(starting_point + space_between, self.discipline_add_action, "Add a discipline")
        self.id_field(starting_point + space_between * 2, self.student_remove_action, "Remove a student")
        self.id_field(starting_point + space_between * 3, self.discipline_remove_action, "Remove a discipline")
        self.id_name_field(starting_point + space_between * 4, self.student_upd_action, "Update a student's name")
        self.id_name_field(starting_point + space_between * 5, self.discipline_upd_action, "Update a discipline's name")
        self.id_id_name_field(starting_point + space_between * 6, self.grade_action, "Grade a student")
        button_undo = tk.Button(self, text="Undo", font=40,
                                command=self.undo_action,
                                bg=self.color_bg, activebackground=self.color_active,
                                highlightbackground=self.color_page)
        button_undo.place(relx=0.1, rely=starting_point + space_between * 7, relheight=0.05, relwidth=0.1)
        button_redo = tk.Button(self, text="Redo", font=40,
                                command=self.redo_action,
                                bg=self.color_bg, activebackground=self.color_active,
                                highlightbackground=self.color_page)
        button_redo.place(relx=0.21, rely=starting_point + space_between * 7, relheight=0.05, relwidth=0.1)

    def student_add_action(self, entry_id, entry_name):
        self.gui.gui_add_student(self.textbox, entry_id.get(), entry_name.get())
        entry_id.delete(0, END)
        entry_name.delete(0, END)
        self.controller.show_frame("StartPage")

    def discipline_add_action(self, entry_id, entry_name):
        self.gui.gui_add_discipline(self.textbox, entry_id.get(), entry_name.get())
        entry_id.delete(0, END)
        entry_name.delete(0, END)
        self.controller.show_frame("StartPage")

    def student_remove_action(self, entry_id):
        self.gui.gui_rem_student(self.textbox, entry_id.get())
        entry_id.delete(0, END)
        self.controller.show_frame("StartPage")

    def discipline_remove_action(self, entry_id):
        self.gui.gui_rem_discipline(self.textbox, entry_id.get())
        entry_id.delete(0, END)
        self.controller.show_frame("StartPage")

    def student_upd_action(self, entry_id, entry_name):
        self.gui.gui_upd_student(self.textbox, entry_id.get(), entry_name.get())
        entry_id.delete(0, END)
        self.controller.show_frame("StartPage")

    def discipline_upd_action(self, entry_id, entry_name):
        self.gui.gui_upd_discipline(self.textbox, entry_id.get(), entry_name.get())
        entry_id.delete(0, END)
        entry_name.delete(0, END)
        self.controller.show_frame("StartPage")

    def grade_action(self, entry_id1, entry_id2, entry_grade):
        self.gui.gui_grade_student(self.textbox, entry_id1.get(), entry_id2.get(), entry_grade.get())
        entry_id1.delete(0, END)
        entry_id2.delete(0, END)
        entry_grade.delete(0, END)
        self.controller.show_frame("StartPage")

    def undo_action(self):
        self.gui.gui_undo(self.textbox)
        self.controller.show_frame("StartPage")

    def redo_action(self):
        self.gui.gui_redo(self.textbox)
        self.controller.show_frame("StartPage")

    # noinspection DuplicatedCode
    def id_field(self, posy, function, button_text):
        entry_id = tk.Entry(self, font=40, bg=self.color_bg, highlightbackground=self.color_page)
        entry_id.place(relx=0.11 + self.button_width, rely=posy, relheight=self.id_height, relwidth=self.id_width)
        entry_id.insert(0, "Id: ")
        entry_id.bind("<FocusIn>", lambda args: entry_id.delete(0, END) if entry_id.get() == "Id: " else None)
        entry_id.bind("<FocusOut>", lambda args: entry_id.insert(0, "Id: ") if entry_id.get() == "" else None)
        button = tk.Button(self, text=button_text, font=40,
                           command=lambda: function(entry_id),
                           bg=self.color_bg, activebackground=self.color_active, highlightbackground=self.color_page)
        button.place(relx=0.1, rely=posy, relheight=self.button_height, relwidth=self.button_width)

    # noinspection DuplicatedCode
    def id_name_field(self, posy, function, button_text):
        entry_id = tk.Entry(self, font=40, bg=self.color_bg, highlightbackground=self.color_page)
        entry_id.place(relx=0.11 + self.button_width, rely=posy, relheight=self.id_height, relwidth=self.id_width)
        entry_id.insert(0, "Id: ")
        entry_id.bind("<FocusIn>", lambda args: entry_id.delete(0, END) if entry_id.get() == "Id: " else None)
        entry_id.bind("<FocusOut>", lambda args: entry_id.insert(0, "Id: ") if entry_id.get() == "" else None)
        entry_name = tk.Entry(self, font=40, bg=self.color_bg, highlightbackground=self.color_page)
        entry_name.place(relx=0.12 + self.button_width + self.id_width, rely=posy,
                         relheight=self.button_height, relwidth=0.3)
        entry_name.insert(0, "Name: ")
        entry_name.bind("<FocusIn>", lambda args: entry_name.delete(0, END) if entry_name.get() == "Name: " else None)
        entry_name.bind("<FocusOut>", lambda args: entry_name.insert(0, "Name: ") if entry_name.get() == "" else None)
        button = tk.Button(self, text=button_text, font=40,
                           command=lambda: function(entry_id, entry_name),
                           bg=self.color_bg, activebackground=self.color_active, highlightbackground=self.color_page)
        button.place(relx=0.1, rely=posy, relheight=self.button_height, relwidth=self.button_width)

    def id_id_name_field(self, posy, function, button_text):
        entry_id1 = tk.Entry(self, font=40, bg=self.color_bg, highlightbackground=self.color_page)
        entry_id1.place(relx=0.11 + self.button_width, rely=posy, relheight=self.id_height, relwidth=self.id_width)
        entry_id1.insert(0, "Student: ")
        entry_id1.bind("<FocusIn>",
                       lambda args: entry_id1.delete(0, END) if entry_id1.get() == "Student: " else None)
        entry_id1.bind("<FocusOut>",
                       lambda args: entry_id1.insert(0, "Student: ") if entry_id1.get() == "" else None)
        entry_id2 = tk.Entry(self, font=40, bg=self.color_bg, highlightbackground=self.color_page)
        entry_id2.place(relx=0.11 + self.button_width + self.id_width + 0.01, rely=posy,
                        relheight=self.id_height, relwidth=self.id_width)
        entry_id2.insert(0, "Discipline: ")
        entry_id2.bind("<FocusIn>",
                       lambda args: entry_id2.delete(0, END) if entry_id2.get() == "Discipline: " else None)
        entry_id2.bind("<FocusOut>",
                       lambda args: entry_id2.insert(0, "Discipline: ") if entry_id2.get() == "" else None)
        entry_grade = tk.Entry(self, font=40, bg=self.color_bg, highlightbackground=self.color_page)
        entry_grade.place(relx=0.13 + self.button_width + self.id_width * 2, rely=posy,
                          relheight=self.id_height, relwidth=self.id_width)
        entry_grade.insert(0, "Grade: ")
        entry_grade.bind("<FocusIn>",
                         lambda args: entry_grade.delete(0, END) if entry_grade.get() == "Grade: " else None)
        entry_grade.bind("<FocusOut>",
                         lambda args: entry_grade.insert(0, "Grade: ") if entry_grade.get() == "" else None)
        button = tk.Button(self, text=button_text, font=40,
                           command=lambda: function(entry_id1, entry_id2, entry_grade),
                           bg=self.color_bg, activebackground=self.color_active, highlightbackground=self.color_page)
        button.place(relx=0.1, rely=posy, relheight=self.button_height, relwidth=self.button_width)


class StartPage(tk.Frame):
    def __init__(self, parent, controller, gui):
        tk.Frame.__init__(self, parent)
        color_page = "#ace5d9"
        color_bg = "#fcb088"
        color_active = "#fc9d6a"
        self.configure(bg=color_page)
        self.controller = controller
        self.textbox = TextScrollCombo(self)
        self.textbox.place(relx=0.1, rely=0.5, relwidth=0.8, relheight=0.4)
        button_height = 0.05
        button_width = 0.24
        button1 = tk.Button(self, text="Manage data", font=40,
                            command=lambda: controller.show_frame("ManagePage"),
                            bg=color_bg, activebackground=color_active, highlightbackground=color_page)
        button1.place(relx=0.1, rely=0.05, relheight=button_height, relwidth=button_width)
        button2 = tk.Button(self, text="Show all students", font=40,
                            command=lambda: gui.gui_show_students(self.textbox),
                            bg=color_bg, activebackground=color_active, highlightbackground=color_page)
        button2.place(relx=0.1, rely=0.11, relheight=button_height, relwidth=0.24)
        button3 = tk.Button(self, text="Show all disciplines", font=40,
                            command=lambda: gui.gui_show_disciplines(self.textbox),
                            bg=color_bg, activebackground=color_active, highlightbackground=color_page)
        button3.place(relx=0.1, rely=0.17, relheight=button_height, relwidth=button_width)
        button4 = tk.Button(self, text="Show all grades", font=40,
                            command=lambda: gui.gui_show_grades(self.textbox),
                            bg=color_bg, activebackground=color_active, highlightbackground=color_page)
        button4.place(relx=0.1, rely=0.23, relheight=button_height, relwidth=button_width)
        button5 = tk.Button(self, text="Search", font=40,
                            command=lambda: controller.show_frame("SearchPage"),
                            bg=color_bg, activebackground=color_active, highlightbackground=color_page)
        button5.place(relx=0.1, rely=0.29, relheight=button_height, relwidth=button_width)
        button6 = tk.Button(self, text="Statistics", font=40,
                            command=lambda: controller.show_frame("StatisticsPage"),
                            bg=color_bg, activebackground=color_active, highlightbackground=color_page)
        button6.place(relx=0.1, rely=0.35, relheight=button_height, relwidth=button_width)


class MainMenu(tk.Tk):
    def __init__(self, gui):
        tk.Tk.__init__(self)
        self.geometry("900x600")
        self.title_font = tkfont.Font(family='Helvetica', size=18, weight="bold", slant="italic")
        self.title("Grade management application")
        container = tk.Frame(self)
        container.pack(side="top", fill="both", expand=True)
        container.grid_rowconfigure(0, weight=1)
        container.grid_columnconfigure(0, weight=1)
        self.frames = {}
        for F in (StartPage, ManagePage, SearchPage, StatisticsPage):
            page_name = F.__name__
            if F == StartPage:
                frame = F(parent=container, controller=self, gui=gui)
            else:
                frame = F(parent=container, controller=self, gui=gui, textbox=self.frames["StartPage"].textbox)
            self.frames[page_name] = frame
            frame.grid(row=0, column=0, sticky="nsew")
        self.show_frame("StartPage")

    def show_frame(self, page_name):
        frame = self.frames[page_name]
        frame.tkraise()


class GUI:
    def __init__(self, service=None):
        if service is None:
            service = Service()
        self._service = service

    def gui_show_students(self, textbox):
        textbox.txt.delete(1.0, END)
        show = "There are " + str(len(self._service.students)) + " students in total.\n"
        for i in self._service.students:
            show += str(i) + "\n"
        textbox.txt.insert(1.0, show)

    def gui_show_disciplines(self, textbox):
        textbox.txt.delete(1.0, END)
        show = "There are " + str(len(self._service.disciplines)) + " disciplines in total.\n"
        for i in self._service.disciplines:
            show += str(i) + "\n"
        textbox.txt.insert(1.0, show)

    def gui_show_grades(self, textbox):
        textbox.txt.delete(1.0, END)
        show = "There are " + str(len(self._service.grades)) + " grades in total\n"
        for i in self._service.list_grades_to_string():
            show += i+"\n"
        textbox.txt.insert(1.0, show)

    def gui_add_student(self, textbox, sid, name):
        textbox.txt.delete(1.0, END)
        try:
            sid = int(sid)
            self._service.add_student(sid, name)
            textbox.txt.insert(1.0, "Student successfully added!")
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_add_discipline(self, textbox, did, name):
        textbox.txt.delete(1.0, END)
        try:
            did = int(did)
            self._service.add_discipline(did, name)
            textbox.txt.insert(1.0, "Discipline successfully added!")
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_rem_student(self, textbox, sid):
        textbox.txt.delete(1.0, END)
        try:
            sid = int(sid)
            self._service.rem_student(sid)
            textbox.txt.insert(1.0, "Student successfully removed!")
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_rem_discipline(self, textbox, did):
        textbox.txt.delete(1.0, END)
        try:
            did = int(did)
            self._service.rem_discipline(did)
            textbox.txt.insert(1.0, "Discipline successfully removed!")
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_upd_student(self, textbox, sid, name):
        textbox.txt.delete(1.0, END)
        try:
            sid = int(sid)
            self._service.upd_student(sid, name)
            textbox.txt.insert(1.0, "Student successfully updated!")
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_upd_discipline(self, textbox, did, name):
        textbox.txt.delete(1.0, END)
        try:
            did = int(did)
            self._service.upd_discipline(did, name)
            textbox.txt.insert(1.0, "Discipline successfully updated!")
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_grade_student(self, textbox, sid, did, grade):
        textbox.txt.delete(1.0, END)
        try:
            sid = int(sid)
            did = int(did)
            grade = int(grade)
            self._service.grade_student(sid, did, grade)
            textbox.txt.insert(1.0, "Student Successfully graded!")
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_undo(self, textbox):
        textbox.txt.delete(1.0, END)
        try:
            self._service.undo()
            textbox.txt.insert(1.0, "Operation successfully undone!")
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_redo(self, textbox):
        textbox.txt.delete(1.0, END)
        try:
            self._service.redo()
            textbox.txt.insert(1.0, "Operation successfully redone!")
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_search_student_id(self, textbox, sid):
        textbox.txt.delete(1.0, END)
        try:
            sid = int(sid)
            student = self._service.find_student(sid)
            if student:
                textbox.txt.insert(1.0, student)
            else:
                textbox.txt.insert(1.0, "There is no student with the given id")
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_search_student_name(self, textbox, name):
        textbox.txt.delete(1.0, END)
        try:
            students = self._service.lookup_student(name)
            show = "There are " + str(len(students)) + " students in total.\n"
            for i in students:
                show += str(i) + "\n"
            textbox.txt.insert(1.0, show)
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_search_discipline_id(self, textbox, did):
        textbox.txt.delete(1.0, END)
        try:
            did = int(did)
            discipline = self._service.find_discipline(did)
            if discipline:
                textbox.txt.insert(1.0, discipline)
            else:
                textbox.txt.insert(1.0, "There is no discipline with the given id")
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_search_discipline_name(self, textbox, name):
        textbox.txt.delete(1.0, END)
        try:
            disciplines = self._service.lookup_discipline(name)
            show = "There are " + str(len(disciplines)) + " disciplines in total.\n"
            for i in disciplines:
                show += str(i) + "\n"
            textbox.txt.insert(1.0, show)
        except Exception as e:
            textbox.txt.insert(1.0, e)

    def gui_students_failing(self, textbox):
        textbox.txt.delete(1.0, END)
        show = ""
        for x in self._service.students_failing_to_string():
            show += x+"\n"
        if show == "":
            show = "There are no students are failing"
        textbox.txt.insert(1.0, show)

    def gui_rank_students(self, textbox):
        textbox.txt.delete(1.0, END)
        show = ""
        for x in self._service.rank_students_to_string():
            show += x+"\n"
        textbox.txt.insert(1.0, show)

    def gui_rank_disciplines(self, textbox):
        textbox.txt.delete(1.0, END)
        show = ""
        for x in self._service.rank_disciplines_to_string():
            show += x+"\n"
        textbox.txt.insert(1.0, show)

    def start(self):
        root = MainMenu(self)
        root.mainloop()
