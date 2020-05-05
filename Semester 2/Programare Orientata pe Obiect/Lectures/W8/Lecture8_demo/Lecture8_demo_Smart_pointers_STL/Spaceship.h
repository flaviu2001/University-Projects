#pragma once
#include <string>
#include <iostream>

class Spaceship
{
private:
	std::string name;
	std::string captain;
	std::string type;

public:
	Spaceship() {}
	Spaceship(const std::string& _name, const std::string& _captain, const std::string& _type);
	~Spaceship();

	std::string getName() const { return this->name; }
	std::string getCaptain() const { return this->captain; }
	std::string getType() const { return this->type; }

	friend std::istream& operator>>(std::istream& is, Spaceship& s);
	friend std::ostream& operator<<(std::ostream& os, const Spaceship& s);
};

