def getID(student):
    return student[0]
def getName(student):
    return student[1]
def getGrade(student):
    return student[2]


def makeStudent(sid, name, grade):
    '''
    :param sid: id
    :param name: string of len >= 3
    :param grade: int between 1 and 10
    :return:
        success - return the student
        error - return None
    '''
    if len(name) < 3:
        return  None
    grade = int(grade)
    if(grade < 1 or grade > 10):
        return  None
    return [sid, name, grade]

def findStudent(studentList, sid):
    '''
    Finds a student with id equal to sid in studentList
    :param studentList: A list consisting of objects of type student
    :param sid: The id of the student we're searching
    :return:
        An object student if sid is found
        None if no such id exists
    '''
    for i in studentList:
        if getID(i) == sid:
            return i
    return None

def addStudent(studentList, student):
    '''
    This function appends student to student list if student is not already present
    :param studentList: A list consisting of objects of type student
    :param student: An object of type student
    :return:
        0 - success
        1 - Duplicate student id
    '''
    if findStudent(studentList, getID(student)) != None:
        return 1
    studentList.append(student)
    return  0

def testAddStudent():
    slist = []
    s1 = makeStudent(1, 'Marie', 10)
    assert addStudent(slist, s1) == 0
    assert slist == [s1]
    assert addStudent(slist, s1) == 1
    assert slist == [s1]

def readCommand():
    # 1. Separate command word from list of params
    # 2. Identify params
    # 3. Return tuple (command, list of params)
    cmd = input()
    #cmd = 'add'
    idx = cmd.find(' ')
    if idx == -1:
        return (cmd, [])
    command = cmd[:idx]
    params = cmd[idx:]
    params = params.split(',')
    for i in range(len(params)):
        params[i] = params[i].strip()
    return (command, params)

def add_student_ui(studentList, params):
    if len(params) != 3:
        print('Bad student parameters')
        return
    s = makeStudent(params[0], params[1], params[2])
    if s is None:
        print('Invalid student data')
        return
    if addStudent(studentList, s) == 1:
        print('Duplicate student id!')


def start():
    studentList = []
    while True:
        print(studentList)
        cmdtuple = readCommand()
        cmd = cmdtuple[0]
        params = cmdtuple[1]
        if cmd == 'add':
            add_student_ui(studentList, params)
        elif cmd == 'exit':
            break
        else:
            print('Bad command')


start()