#pragma once
#include <string>

class Gene
{
private:
	std::string name;
	std::string organismName;
	std::string sequence;

public:
	Gene(): name(""), organismName(""), sequence("") {}
	Gene(std::string n, std::string o, std::string s) : name(n), organismName(o), sequence(s) {}
	std::string getName() const { return this->name; }
	std::string getOrganismName() const { return this->organismName; }
	std::string getSequence() const { return this->sequence; }

	std::string toString();
};

