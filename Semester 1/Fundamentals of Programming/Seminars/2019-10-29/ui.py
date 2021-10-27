from service import Service
class UI:
    # Can the UI do its job without a Service?
    # No => it does not make sense to create the UI without having a Service
    def __init__(self, service):
        self._service = service
        
    def addStar(self):
        # read star location , mass, magnitude
        # check star is valid !
        # call service.addStar(newStar)
        newStar = None
        self._service.addStar(newStar)
        pass
    def sortStars(self):
        pass
    def start(self):
        print('Welcome to star catalogue!')
        # print menu, read user choice, bla bla bla
        # call addStar and sortStars methods
        
s = Service()
ui = UI(s)
ui.start()