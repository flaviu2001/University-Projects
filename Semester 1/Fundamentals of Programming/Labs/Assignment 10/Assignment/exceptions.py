class ShipError(Exception):
    def __init__(self, message=""):
        super().__init__(message)


class BoardError(Exception):
    def __init__(self, message=""):
        super().__init__(message)


class GameError(Exception):
    def __init__(self, message=""):
        super().__init__(message)


class AIError(Exception):
    def __init__(self, message=""):
        super().__init__(message)


class SettingsError(Exception):
    def __init__(self, message=""):
        super().__init__(message)
