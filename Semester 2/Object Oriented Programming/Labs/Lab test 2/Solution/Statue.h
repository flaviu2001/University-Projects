#pragma once
#include "string"

class Statue
{
private:
	std::string conjurer;
	std::string model;
	int year;
	std::string color;
public:
	Statue(std::string conjurer = std::string(), std::string model = std::string(), int year = 0, std::string color = std::string());
	std::string getconjurer();
	std::string getmodel();
	int getyear();
	std::string getcolor();
	friend std::istream& operator>>(std::istream& reader, Statue& statue);
};
std::string to_string(Statue targetObject);