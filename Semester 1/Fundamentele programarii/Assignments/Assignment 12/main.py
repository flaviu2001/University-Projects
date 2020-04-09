nr = None
try:
    nr = int(input("How many parentheses should the strings have?: "))
    if nr < 0:
        raise ValueError
except ValueError:
    print("That's not a valid number")
    exit()

if nr == 0:
    print("There is one solution and it is the empty string.")
    exit()


def back_iterative(n):
    class Thing:
        def __init__(self, stack, k, s):
            self.stack = stack
            self.k = k
            self.s = s
            self.need = 0
            self.v = []
            if s > 0:
                self.need += 1
            if s < n-k:
                self.need += 1
            if k == n:
                self.v = [self.stack]

    rec_stack = [Thing("", 0, 0)]
    while len(rec_stack) > 0:
        last = rec_stack[-1]
        if last.need == 0:
            if len(rec_stack) == 1:
                return last.v
            if rec_stack[-2].k == last.k:
                rec_stack[-3].v += last.v
                rec_stack[-3].need -= 1
            else:
                rec_stack[-2].v += last.v
                rec_stack[-2].need -= 1
            rec_stack.pop()
        else:
            if last.s > 0:
                rec_stack.append(Thing(last.stack + ")", last.k + 1, last.s - 1))
            if last.s < n-last.k:
                rec_stack.append(Thing(last.stack + "(", last.k + 1, last.s + 1))


def back_recursive(n, stack=None, k=0, s=0):
    if n == 0:
        return []
    if stack is None:
        stack = ""
    if k == n:
        return [stack]
    a = []
    if s < n-k:
        a = back_recursive(n, stack+"(", k+1, s+1)
    if s > 0:
        a += back_recursive(n, stack+")", k+1, s-1)
    return a


v = back_recursive(nr)
print("There are {0} strings of length {1}.".format(len(v), nr))
for string in v:
    print(string)
