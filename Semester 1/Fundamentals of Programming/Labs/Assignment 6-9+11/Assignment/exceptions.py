class UniqueError(Exception):
    def __init__(self, message):
        super().__init__(message)


class ExistenceError(Exception):
    def __init__(self, message):
        super().__init__(message)


class UndoBounds(Exception):
    def __init__(self, message):
        super().__init__(message)


class InvalidGrade(Exception):
    def __init__(self, message):
        super().__init__(message)
