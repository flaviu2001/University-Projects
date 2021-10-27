#pragma once
#include "vector"
#include "Statue.h"
#include "string"
#include "algorithm"



class Repository
{

private:
	std::vector<Statue> Container;

public:
	Repository(std::vector<Statue> Container = std::vector<Statue>());

	std::vector<Statue> getContainer();
	void setContainer(std::vector<Statue> Container);
	void add(Statue newObject = Statue());
};
