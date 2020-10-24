from Seminar4.functions import *

def add_circle_ui(circles):
    x = int(input('x='))
    y = int(input('y='))
    r = int(input('r='))
    add_circle(circles, x, y, r)

def show_circles_ui(circles):
    for c in circles:
        print(tostr(c))

def print_menu():
    print("1. Add a circle")
    print("3. Show circles")
    print("0. Exit")

def start():
    circles = test_init()
    commands = {'1':add_circle_ui, '3':show_circles_ui}
    while True:
        print_menu()
        cmd = input('Command: ')
        if cmd == '0':
            return
        if cmd in commands:
            try:
                commands[cmd](circles)
            except ValueError as ve:
                print(ve)
        else:
            print('Bad command or file name')

start()