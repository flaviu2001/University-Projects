#pragma once

template <typename T, typename U>
class Pair
{
private:
	T first;
	U second;

public:
	Pair(T f, U s) : first(f), second(s) {}
	T getFirst() const { return this->first; }
	U getSecond() const { return this->second; }
};