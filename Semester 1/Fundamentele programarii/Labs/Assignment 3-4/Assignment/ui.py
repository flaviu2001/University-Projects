from tests import *
from copy import deepcopy
from re import match


def display_contestants(contestants):
    print('The list has ' + str(len(contestants)) + ' contestants')
    for x in contestants:
        print('Contestant with scores ' + str(get_p1(x)) + ' ' + str(get_p2(x)) + ' ' + str(
            get_p3(x)) + ' has average ' + str(get_average(x)))


def ui_add(contestants, cmd, stack):
    params = match('add (0|[1-9][0-9]*) (0|[1-9][0-9]*) (0|[1-9][0-9]*)$', cmd).groups()
    try:
        add(contestants, make_score(int(params[0]), int(params[1]), int(params[2])))
        print('Contestant successfully added')
        stack.append(contestants)
    except Exception as ve:
        print(ve)


def ui_insert(contestants, cmd, stack):
    params = match('insert (0|[1-9][0-9]*) (0|[1-9][0-9]*) (0|[1-9][0-9]*) at (0|[1-9][0-9]*)$', cmd).groups()
    try:
        add(contestants, make_score(int(params[0]), int(params[1]), int(params[2])), int(params[3]))
        print('Contestant successfully added')
        stack.append(contestants)
    except Exception as ve:
        print(ve)


def ui_rem(contestants, cmd, stack):
    param = match('remove (0|[1-9][0-9]*)$', cmd).group(1)
    try:
        rem(contestants, int(param))
        print('Contestant successfully removed')
        stack.append(contestants)
    except Exception as ve:
        print(ve)


def ui_rem_range(contestants, cmd, stack):
    params = match('remove (0|[1-9][0-9]*) to (0|[1-9][0-9]*)$', cmd).groups()
    try:
        rem(contestants, int(params[0]), int(params[1]))
        print('Contestant successfully removed')
        stack.append(contestants)
    except Exception as ve:
        print(ve)


def ui_replace(contestants, cmd, stack):
    params = match('replace (0|[1-9][0-9]*) P([123]) with (0|[1-9][0-9]*)$', cmd).groups()
    try:
        replace(contestants, int(params[0]), int(params[1]), int(params[2]))
        print('Score successfully replaced')
        stack.append(contestants)
    except Exception as ve:
        print(ve)


def ui_remove_comparator(contestants, cmd, stack):
    params = match('remove ([<=>]) (0|[1-9][0-9]*)$', cmd).groups()
    try:
        rem_score(contestants, params[0], int(params[1]))
        print('Successfully removed scores')
        stack.append(contestants)
    except Exception as ve:
        print(ve)


def ui_avg(contestants, cmd):
    params = match('avg (0|[1-9][0-9]*) to (0|[1-9][0-9]*)$', cmd).groups()
    if int(params[0]) not in range(len(contestants)) or int(params[1]) not in range(len(contestants)):
        print('Invalid range')
    else:
        print('The average is ' + str(average(contestants, int(params[0]), int(params[1]))))


def ui_min(contestants, cmd):
    params = match('min (0|[1-9][0-9]*) to (0|[1-9][0-9]*)$', cmd).groups()
    if int(params[0]) not in range(len(contestants)) or int(params[1]) not in range(len(contestants)):
        print('Invalid range')
    else:
        print('The minimum is ' + str(minimum(contestants, int(params[0]), int(params[1]))))


def ui_top(contestants, cmd):
    param = match('top (0|[1-9][0-9]*)$', cmd).group(1)
    if int(param) not in range(len(contestants)):
        print('Invalid number')
    else:
        display_contestants(podium(contestants, int(param)))


def ui_top_problem(contestants, cmd):
    params = match('top (0|[1-9][0-9]*) P([123])$', cmd).groups()
    if int(params[0]) not in range(len(contestants)):
        print('Invalid number')
    else:
        display_contestants(podium(contestants, int(params[0]), int(params[1])))


def print_help():
    print('add <P1 score> <P2 score> <P3 score>')
    print('insert <P1 score> <P2 score> <P3 score> at <position>')
    print('remove <position>')
    print('remove <start position> to <end position>')
    print('replace <position> <P1 | P2 | P3> with <new score>')
    print('list')
    print('list sorted')
    print('list [ < | = | > ] <score>')
    print('avg <start position> to <end position>')
    print('min <start position> to <end position>')
    print('top <number>')
    print('top <number> <P1 | P2 | P3>')
    print('remove [ < | = | > ] <score>')
    print('undo')
    print('exit\n')


def start():
    stack = [sample_list()]
    print('This is an app for managing a list of scores of participants')
    print('Type help for a list of commands')
    while True:
        contestants = deepcopy(stack[-1])
        cmd = input("> ")
        if match('add (0|[1-9][0-9]*) (0|[1-9][0-9]*) (0|[1-9][0-9]*)$', cmd):
            ui_add(contestants, cmd, stack)
        elif match('insert (0|[1-9][0-9]*) (0|[1-9][0-9]*) (0|[1-9][0-9]*) at (0|[1-9][0-9]*)$', cmd):
            ui_insert(contestants, cmd, stack)
        elif match('remove (0|[1-9][0-9]*)$', cmd):
            ui_rem(contestants, cmd, stack)
        elif match('remove (0|[1-9][0-9]*) to (0|[1-9][0-9]*)$', cmd):
            ui_rem_range(contestants, cmd, stack)
        elif match('replace (0|[1-9][0-9]*) P([123]) with (0|[1-9][0-9]*)$', cmd):
            ui_replace(contestants, cmd, stack)
        elif match('list$', cmd):
            display_contestants(contestants)
        elif match('list sorted$', cmd):
            display_contestants(list(reversed(sorted(contestants, key=lambda x: get_average(x)))))
        elif match('list ([<=>]) (0|[1-9][0-9]*)$', cmd):
            params = match('list ([<=>]) (0|[1-9][0-9]*)$', cmd).groups()
            display_contestants(return_certain_contestants(contestants, params[0], int(params[1])))
        elif match('avg (0|[1-9][0-9]*) to (0|[1-9][0-9]*)$', cmd):
            ui_avg(contestants, cmd)
        elif match('min (0|[1-9][0-9]*) to (0|[1-9][0-9]*)$', cmd):
            ui_min(contestants, cmd)
        elif match('top (0|[1-9][0-9]*)$', cmd):
            ui_top(contestants, cmd)
        elif match('top (0|[1-9][0-9]*) P([123])$', cmd):
            ui_top_problem(contestants, cmd)
        elif match('remove ([<=>]) (0|[1-9][0-9]*)$', cmd):
            ui_remove_comparator(contestants, cmd, stack)
        elif match('undo$', cmd):
            if len(stack) == 1:
                print('No operations were done before this point')
            else:
                stack.pop()
                print('Operation completed successfully')
        elif match('help$', cmd):
            print_help()
        elif match('exit$', cmd):
            print('Thanks for using the app!')
            break
        else:
            print('Invalid command')
