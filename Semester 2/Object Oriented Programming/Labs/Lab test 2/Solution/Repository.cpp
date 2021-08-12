#include "Repository.h"

Repository::Repository(std::vector<Statue> Container)
	: Container(Container)
{

}

std::vector<Statue> Repository::getContainer()
{
	return this->Container;
}

void Repository::setContainer(std::vector<Statue> Container)
{
	this->Container = Container;
}

void Repository::add(Statue newObject)
{
	this->Container.push_back(newObject);
}