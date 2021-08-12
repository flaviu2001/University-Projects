#pragma once

#include "repository.h"
#include "domain.h"

class Service
{
private:
	//TODO: Put repositories (with references)
	Repository<Astronomer>& astronomers;
	Repository<Star>& stars;
public:
	Service(Repository<Astronomer>& _astronomers, Repository<Star>& _stars);
	~Service();
	std::vector<Astronomer> get_astronomers();
	std::vector<Star> get_stars();
	std::vector<Star> get_by_name_id(std::string to_search);
	/**
		This function adds a star to the repository of stars.
		If the name of the star is empty an exception is thrown, but also if another star with the name exists or if the diameter is less than or equal to 0.
	*/
	void add_star(std::string name, std::string constellation, int ra, int dec, int diameter);
};

