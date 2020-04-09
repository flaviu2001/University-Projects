class BagIterator:
	def __init__(self, bag):
		self._bag = bag
		self._cPosition = 0
		self._cFrequency = 1

	def next(self):
		if self._cPosition == len(self._bag._elements)
			raise ValueError
		if self._cFrequency == self.bag._frequencies[self._cPosition]
			self._cPosition += 1
			self._cFrequency = 1
		else self._cFrequency += 1

class Bag:
	def __init__(self):
		self._elements = []
		self._frequencies = []

	def add(self, value):
		now = -1
		for i in range(len(self._elements)):
			if self._elements[i] == value:
				now = i
				break
		if now == -1:
			self._elements.append(value)
			self._frequencies.append(1)
		else:
			self._frequencies[now] += 1

	def remove(self, value):
		now = -1
		for i in range(len(self._elements)):
			if self._elements[i] == value:
				now = i
				break
		if now == -1:
			return False
		self._frequencies[now] -= 1
		if self._frequencies[now] == 0:
			del self._elements[now]
			del self._frequencies[now]

	def iterator(self):
		return BagIterator(self)
