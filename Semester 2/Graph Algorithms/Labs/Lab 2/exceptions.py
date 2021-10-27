class VertexError(Exception):
    def __init__(self, message=""):
        super().__init__(message)


class EdgeError(Exception):
    def __init__(self, message=""):
        super().__init__(message)
