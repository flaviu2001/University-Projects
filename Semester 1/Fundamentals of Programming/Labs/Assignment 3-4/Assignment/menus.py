from tests import *
from copy import deepcopy
from ui import display_contestants


def print_menu():
    print('\n1. add a contestant')
    print('2. insert contestant at a position')
    print('3. remove a contestant')
    print('4. remove contestants in a range')
    print('5. replace score at a problem of a contestant')
    print('6. list')
    print('7. list sorted')
    print('8. list contestants with certain scores')
    print('9. average of contestants in a range')
    print('10. min of contestants in a range')
    print('11. list of top x contestants')
    print('12. list of top x contestants at a problem')
    print('13. remove all contestants with certain scores')
    print('14. undo')
    print('15. exit\n')


def menu_add(contestants, stack):
    p1 = input("Score at problem 1: ")
    p2 = input("Score at problem 2: ")
    p3 = input("Score at problem 3: ")
    try:
        p1 = int(p1)
        p2 = int(p2)
        p3 = int(p3)
        add(contestants, make_score(p1, p2, p3))
        print('Contestant successfully added')
        stack.append(contestants)
    except Exception as e:
        print(e)


def menu_insert(contestants, stack):
    p1 = input("Score at problem 1: ")
    p2 = input("Score at problem 2: ")
    p3 = input("Score at problem 3: ")
    pos = input("Position to add at: ")
    try:
        p1 = int(p1)
        p2 = int(p2)
        p3 = int(p3)
        pos = int(pos)
        add(contestants, make_score(p1, p2, p3), pos)
        print('Contestant successfully added')
        stack.append(contestants)
    except Exception as e:
        print(e)


def menu_rem(contestants, stack):
    pos = input('Position to remove: ')
    try:
        pos = int(pos)
        rem(contestants, pos)
        print("Contestant successfully removed")
        stack.append(contestants)
    except Exception as e:
        print(e)


def menu_rem_range(contestants, stack):
    pos1 = input('Left position: ')
    pos2 = input('Right positon: ')
    try:
        pos1 = int(pos1)
        pos2 = int(pos2)
        rem(contestants, pos1, pos2)
        print("Contestants successfully removed")
        stack.append(contestants)
    except Exception as e:
        print(e)


def menu_replace(contestants, stack):
    pos = input('Position to modify: ')
    problem = input('Problem number: ')
    points = input('Points to set: ')
    try:
        pos = int(pos)
        problem = int(problem)
        points = int(points)
        replace(contestants, pos, problem, points)
        print('Score successfully replaced')
        stack.append(contestants)
    except Exception as ve:
        print(ve)


def menu_list_comparator(contestants):
    comp = input('Type one of <, = or >: ')
    points = input('Score to compare: ')
    try:
        points = int(points)
        display_contestants(return_certain_contestants(contestants, comp, points))
    except Exception as e:
        print(e)


def menu_avg(contestants):
    left = input('Left position of range: ')
    right = input('Right position of range: ')
    try:
        left = int(left)
        right = int(right)
        if left not in range(len(contestants)) or right not in range(len(contestants)) or left > right:
            raise ValueError("Invalid positions")
        print('The average is ' + str(average(contestants, left, right)))
    except Exception as e:
        print(e)


def menu_min(contestants):
    left = input('Left position of range: ')
    right = input('Right position of range: ')
    try:
        left = int(left)
        right = int(right)
        if left not in range(len(contestants)) or right not in range(len(contestants)) or left > right:
            raise ValueError("Invalid positions")
        print('The minimum is ' + str(minimum(contestants, left, right)))
    except Exception as e:
        print(e)


def menu_top(contestants):
    num = input('How many people to display: ')
    try:
        num = int(num)
        display_contestants(podium(contestants, num))
    except Exception as e:
        print(e)


def menu_top_problem(contestants):
    num = input('How many people to display: ')
    problem = input('Problem number: ')
    try:
        num = int(num)
        problem = int(problem)
        display_contestants(podium(contestants, num, problem))
    except Exception as e:
        print(e)


def menu_remove_comparator(contestants, stack):
    comp = input('Type one of <, = or >: ')
    score = input('Score to compare: ')
    try:
        score = int(score)
        rem_score(contestants, comp, score)
        print('Successfully removed contestants')
        stack.append(contestants)
    except Exception as e:
        print(e)


def start():
    stack = [sample_list()]
    print('This is an app for managing a list of scores of participants')
    while True:
        print_menu()
        contestants = deepcopy(stack[-1])
        cmd = input("> ")
        if cmd == '1':
            menu_add(contestants, stack)
        elif cmd == '2':
            menu_insert(contestants, stack)
        elif cmd == '3':
            menu_rem(contestants, stack)
        elif cmd == '4':
            menu_rem_range(contestants, stack)
        elif cmd == '5':
            menu_replace(contestants, stack)
        elif cmd == '6':
            display_contestants(contestants)
        elif cmd == '7':
            display_contestants(list(reversed(sorted(contestants, key=lambda x: get_average(x)))))
        elif cmd == '8':
            menu_list_comparator(contestants)
        elif cmd == '9':
            menu_avg(contestants)
        elif cmd == '10':
            menu_min(contestants)
        elif cmd == '11':
            menu_top(contestants)
        elif cmd == '12':
            menu_top_problem(contestants)
        elif cmd == '13':
            menu_remove_comparator(contestants, stack)
        elif cmd == '14':
            if len(stack) == 1:
                print('No operations were done before this point')
            else:
                stack.pop()
                print('Operation completed successfully')
        elif cmd == '15':
            print('Thanks for using the app!')
            break
        else:
            print('Invalid command')
