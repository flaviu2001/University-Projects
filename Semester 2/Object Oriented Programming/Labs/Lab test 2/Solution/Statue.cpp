#include "Statue.h"
#include <sstream>

Statue::Statue(std::string conjurer, std::string model, int year, std::string color) : conjurer(conjurer), model(model), year(year), color(color)
{
}

std::string Statue::getconjurer()
{
	return this->conjurer;
}

std::string Statue::getmodel()
{
	return this->model;
}

int Statue::getyear()
{
	return this->year;
}

std::string Statue::getcolor()
{
	return this->color;
}

std::string to_string(Statue targetObject)
{
	return targetObject.getconjurer() + "," + targetObject.getmodel();
}

std::istream& operator>>(std::istream& reader, Statue& statue) {

    std::string line;
    std::getline(reader, line);
    if (line.empty())
        return reader;
    std::stringstream stream(line);

    std::string conjurer;
    std::string model;
    std::string year;
    std::string color;


    std::getline(stream, conjurer, ',');
    std::getline(stream, model, ',');
    std::getline(stream, year, ',');
    std::getline(stream, color);
    
    statue = Statue(conjurer, model, std::stoi(year), color);
    return reader;
}