def create_circle(x, y, r):
    '''
    x, y, r > 0
    Raises value error if circle is invalid
    :param x: Coordinate x
    :param y: Coordinate y
    :param r: Radius r
    :return: Circle object represented as a list [x, y, r]
    '''
    if r <= 0:
        raise ValueError("Radius smaller than 0")
    if x < r or y < r:
        raise ValueError("Circle not in first quadrant")
    return [x, y, r]

def get_x(c):
    return c[0]
def get_y(c):
    return c[1]
def get_r(c):
    return c[2]
def tostr(c):
    return '(' + str(get_x(c))+', ' + str(get_y(c)) + '), r=' + str(get_r(c))

def test_tostr():
    c = create_circle(2, 2, 1)
    assert tostr(c) == '(2, 2), r=1'

def test_init():
    circles = []
    circles.append(create_circle(1, 2, 1))
    circles.append(create_circle(10, 4, 2))
    circles.append(create_circle(5, 5, 5))
    return circles

def test_create_circle():
    c = create_circle(1, 1, 1)
    assert get_x(c) == 1 and get_y(c) == 1 and get_r(c) == 1
    try:
        c = create_circle(1, 1, -1)
        assert False
    except ValueError:
        pass
    try:
        c = create_circle(1, 1, 2)
        assert False
    except ValueError:
        pass


test_create_circle()