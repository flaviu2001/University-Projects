#include "Service.h"
#include <cassert>

Service::Service(Repository _repository) : repository(_repository)
{
}

void Service::add(std::string conjurer, std::string model, std::string year, std::string color)
{
	Statue newObject(conjurer, model, std::stoi(year), color);
	this->repository.add(newObject);
}

int Service::nrStatues(std::string conjurer)
{
	auto repo = list();
	int cnt = 0;
	for (auto x : repo)
		if (x.getconjurer() == conjurer)
			++cnt;
	return cnt;
}

void Service::test_nrStatues()
{
	Repository r;
	Statue s{"a", "b", 2, "red"};
	Statue s2{ "b", "b", 2, "red" };
	Statue s3{ "b", "b", 2, "red" };
	r.add(s);
	r.add(s2);
	r.add(s3);
	Service serv{ r };
	assert(serv.nrStatues("b") == 2);
	assert(serv.nrStatues("a") == 1);
	assert(serv.nrStatues("non existant") == 0);
}

std::vector<Statue> Service::list()
{
	return repository.getContainer();
}

void Service::readFromFile(std::string FilePath)
{
	std::ifstream fin(FilePath);
	Statue statue;
	while (fin >> statue)
		repository.add(statue);
	fin.close();
}