from functions import *


def test_for_add():
    s = []
    assert not add(s, make_score(2, 1, 1))
    assert len(s) == 1
    try:
        add(s, make_score(2, 1, 11))
        add(s, make_score(1, 1, 1), 2)
        assert False
    except:
        pass


def test_for_rem():
    s = []
    add(s, make_score(1, 2, 1))
    rem(s, 0)
    assert s[0] == make_score(0, 0, 0)
    add(s, make_score(4, 4, 4))
    add(s, make_score(1, 2, 3))
    rem(s, 1, 2)
    assert s[1] == make_score(0, 0, 0) and s[2] == make_score(0, 0, 0)
    try:
        rem(s, 1, 7)
        assert False
    except:
        pass


def test_for_rem_score():
    s = []
    add(s, make_score(1, 2, 3))
    add(s, make_score(10, 10, 10))
    add(s, make_score(4, 5, 6))
    assert not rem_score(s, '<', 7)
    assert s[0] == make_score(0, 0, 0) and s[2] == make_score(0, 0, 0) and s[1] != make_score(0, 0, 0)


def test_for_replace():
    s = []
    add(s, make_score(1, 0, 10))
    assert not replace(s, 0, 2, 5)
    assert get_p2(s[0]) == 5


def test_all():
    test_for_add()
    test_for_rem()
    test_for_rem_score()
    test_for_replace()
