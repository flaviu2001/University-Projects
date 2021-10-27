#pragma once
#include "Repository.h"
#include "string"
#include "fstream"

class Service
{

private:
	Repository repository;

public:
	Service(Repository _repository);

	void add(std::string conjurer = std::string(), std::string model = std::string(), std::string year = std::string(), std::string color = std::string());
	
	/**
		Returns the number of statues stored in the repository having conjurer equal to the parameter of the function
	*/
	int nrStatues(std::string conjurer);
	
	static void test_nrStatues();
	std::vector<Statue> list();
	void readFromFile(std::string FilePath = std::string());
};
