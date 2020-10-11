from domain import *


def add(contestants, score, pos=-1):
    """
    Adds score to contestants
    :param contestants: List of contestants
    :param score: Scores of contestants (list of 3 integers between 1 and 10)
    :param pos: -1 by default(means the end of list), otherwise the position on which to add object
    :return: None
    """
    if pos == -1:
        pos = len(contestants)
    if pos not in range(len(contestants)+1):
        raise ValueError("Invalid position")
    contestants.insert(pos, score)


def rem(contestants, pos, finalpos=-1):
    """
    Removes the pos-th element  from contestants
    :param contestants: List of contestants
    :param pos: Position in list to delete (or first position in the range pos-finalpos if a range is given
    :param finalpos: -1 if deleting just pos, otherwise the right end of the range to delete
    :return: None
    """
    if finalpos == -1:
        finalpos = pos
    if pos not in range(len(contestants)+1) or finalpos not in range(len(contestants)+1):
        raise ValueError("Invalid position")
    for x in contestants[pos:finalpos + 1]:
        set_all_null(x)


def rem_score(contestants, which, points):
    """
    Resets scores of all contestants with average score less, equal or higher than points, based on which
    :param contestants: List of contestants
    :param which: if < then deletes all contestants less than points, if = equal, if > more
    :param points: How many points to compare to
    :return: None
    """
    if which not in ['<', "=", ">"]:
        raise ValueError("Invalid comparator")
    for x in contestants:
        if which == '<' and get_average(x) < points:
            set_all_null(x)
        elif which == '=' and get_average(x) == points:
            set_all_null(x)
        elif which == '>' and get_average(x) > points:
            set_all_null(x)


def replace(contestants, pos, which, points):
    """
    Replaces the score of participant on position pos and problem which to points
    :param contestants: List of contestants
    :param pos: Position in list
    :param which: Problem 1, 2 or 3
    :param points: Number of points
    :return: None
    """
    if which == 1:
        set_p1(contestants[pos], points)
    elif which == 2:
        set_p2(contestants[pos], points)
    elif which == 3:
        set_p3(contestants[pos], points)
    else:
        raise ValueError("Invalid problem")


def return_certain_contestants(contestants, which, points):
    """
    Makes a list of contestants with average less, equal or more than points
    :param contestants: List of contestants
    :param which: < for less, = for equal, > for more
    :param points: Number of points
    :return: The new list
    """
    if which not in ['<', '=', '>']:
        raise ValueError('Invalid comparator')
    newlist = []
    for x in contestants:
        if which == '<':
            if get_average(x) < points:
                newlist.append(x)
        elif which == '=':
            if get_average(x) == points:
                newlist.append(x)
        else:
            if get_average(x) > points:
                newlist.append(x)
    return newlist


def average(contestants, left, right):
    """
    Returns the average of scores of contestants between positions left and right
    :param contestants: List of contestants
    :param left: Left position
    :param right: Right position
    :return: Float value = the average of the average scores
    """
    if left not in range(len(contestants)) or right not in range(len(contestants)) or left > right:
        raise ValueError('Invalid input')
    now = 0
    for x in contestants[left:right + 1]:
        now += get_average(x)
    return now / (right - left + 1)


def minimum(contestants, left, right):
    """
    Returns the minimum of scores of contestants between positions left and right
    :param contestants: List of contestants
    :param left: Left position
    :param right: Right position
    :return: Float value = the minimum of the average scores
    """
    if left not in range(len(contestants)) or right not in range(len(contestants)) or left > right:
        raise ValueError('Invalid input')
    mn = get_average(contestants[left])
    for x in contestants[left + 1:right + 1]:
        mn = min(mn, get_average(x))
    return mn


def podium(contestants, num, which=-1):
    """
    Returns a list of the best num contestants based on variable which
    :param contestants: List of contestants
    :param num: How many contestants
    :param which: -1 for Average, 1 for first problem, 2 for second problem, 3 for third problem
    :return: Return the new list
    """
    if which == -1:
        return list(reversed(sorted(contestants, key=lambda x: get_average(x))))[0:num]
    elif which == 1:
        return list(reversed(sorted(contestants, key=lambda x: get_p1(x))))[0:num]
    elif which == 2:
        return list(reversed(sorted(contestants, key=lambda x: get_p2(x))))[0:num]
    elif which == 3:
        return list(reversed(sorted(contestants, key=lambda x: get_p3(x))))[0:num]
    else:
        raise ValueError('Invalid argument')
