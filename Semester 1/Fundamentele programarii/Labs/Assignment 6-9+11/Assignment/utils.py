def sort(thing, key=None, function=None, reverse=False):
    def default_key(obj):
        return obj

    def default_compare(obj1, obj2):
        return obj1 < obj2
    from copy import deepcopy
    new_thing = deepcopy(thing)
    if key is None:
        key = default_key
    if function is None:
        function = default_compare
    pos = 1
    while pos < len(new_thing):
        if pos == 0 or not function(key(new_thing[pos]), key(new_thing[pos - 1])):
            pos += 1
        else:
            new_thing[pos - 1], new_thing[pos] = new_thing[pos], new_thing[pos - 1]
            pos -= 1
    if reverse:
        i = 0
        j = len(new_thing)-1
        while i < j:
            new_thing[i], new_thing[j] = new_thing[j], new_thing[i]
            i += 1
            j -= 1
    return new_thing


def my_filter(iterable, accept):
    new_list = type(iterable)()
    for x in iterable:
        if accept(x):
            new_list.append(x)
    return new_list


class Container:
    def __init__(self, new_list=None):
        if new_list is None:
            new_list = []
        self._thing = new_list

    def __len__(self):
        return len(self._thing)

    def __setitem__(self, key, value):
        self._thing[key] = value

    def __getitem__(self, item):
        return self._thing[item]

    def __delitem__(self, key):
        del self._thing[key]

    def __iter__(self):
        self.key = -1
        return self

    def __next__(self):
        self.key += 1
        if self.key >= len(self._thing):
            raise StopIteration
        return self._thing[self.key]

    def append(self, item):
        self._thing.append(item)

    def remove(self, item):
        self._thing.remove(item)
