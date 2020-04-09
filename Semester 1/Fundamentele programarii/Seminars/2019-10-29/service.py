'''
    1. Add a new star to the catalogue (generate some stars)
    2. Show all stars, sorted by distance from Sun(0,0,0), mass, app. magnitude
    3. Which stars are dangerous for Earth? (weight >10 Sm, AppMagnitude <3)
'''

#where are all the stars?
class Service:
    def __init__(self):
        self._stars = []
    
    def addStar(self, star):
        '''
        addStar [Function to add a new star to the catalogue]
        
        Arguments:
            star {[Star]} -- [a star]
        Raises:
            ValueError - new start too close to existing ones
        '''
        for s in self._stars:
            if s.Location - star.Location < 1:
                raise ValueError('Stars are too close')
        self._stars.append(star)

    def sortStars(self, cmp):
        '''
        sortStars Sort stars by giben parameter
        
        Arguments:
            cmp {function} -- [reference to a comparator function]
        Returns:
            sorted list of stars
        '''
        pass
    def dangerousStars(self, mass, mag):
        '''
        dangerousStars Return list of dangerous stars
        Arguments:
            mass, mag - mass and magnitude
        '''
        pass