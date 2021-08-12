#pragma once

#include <string>
#include <istream>

class Astronomer {
public:
	std::string name;
	std::string constellation;

	friend std::istream& operator>> (std::istream& is, Astronomer& s);
	std::string to_string();
};

class Star {
public:
	std::string name;
	std::string constellation;
	int ra;
	int dec;
	int diameter;

	friend std::istream& operator>> (std::istream& is, Star& s);
	std::string to_string();
};
