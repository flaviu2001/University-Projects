def get_p1(score):
    return score[0]


def get_p2(score):
    return score[1]


def get_p3(score):
    return score[2]


def get_average(score):
    return (get_p1(score) + get_p2(score) + get_p3(score)) / 3


def set_p1(score, n):
    if n not in range(0, 11):
        raise ValueError("Invalid score")
    score[0] = n


def set_p2(score, n):
    if n not in range(0, 11):
        raise ValueError("Invalid score")
    score[1] = n


def set_p3(score, n):
    if n not in range(0, 11):
        raise ValueError("Invalid score")
    score[2] = n


def set_all_null(score):
    set_p1(score, 0)
    set_p2(score, 0)
    set_p3(score, 0)


def make_score(p1, p2, p3):
    """
    Constructor for a contestant
    :param p1: int - points for first problem
    :param p2: int - points for second problem
    :param p3: int - points for third problem
    :return: list of [p1, p2, p3]
    """
    if p1 not in range(0, 11):
        raise ValueError('Invalid score')
    if p2 not in range(0, 11):
        raise ValueError('Invalid score')
    if p3 not in range(0, 11):
        raise ValueError('Invalid score')
    return [p1, p2, p3]


def sample_list():
    return [make_score(4, 9, 5),
            make_score(9, 10, 8),
            make_score(10, 1, 1),
            make_score(1, 2, 7),
            make_score(7, 5, 8),
            make_score(8, 8, 8),
            make_score(10, 1, 9),
            make_score(4, 4, 3),
            make_score(4, 5, 8),
            make_score(10, 10, 10)]
