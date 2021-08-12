#include "Service.h"
#include <exception>

using namespace std;

Service::Service(Repository<Astronomer>& _astronomers, Repository<Star>& _stars) : astronomers{_astronomers}, stars{_stars}
{
}

Service::~Service()
{
	stars.save();
}

vector<Astronomer> Service::get_astronomers()
{
	return astronomers.get_list();
}

vector<Star> Service::get_stars()
{
	return stars.get_list();
}

std::vector<Star> Service::get_by_name_id(std::string to_search)
{
	std::vector<Star> s;
	for (auto x : this->stars.get_list())
		if (x.name.find(to_search) != string::npos)
			s.push_back(x);
	return s;
}

void Service::add_star(std::string name, std::string constellation, int ra, int dec, int diameter)
{
	Star s;
	s.name = name;
	s.constellation = constellation;
	s.ra = ra;
	s.dec = dec;
	s.diameter = diameter;
	if (name.empty())
		throw exception{ "The name of the star cannot be empty." };
	for (auto x : stars.get_list())
		if (x.name == name)
			throw exception{ "A star with the same name already exists." };
	if (diameter <= 0)
		throw exception{ "The diameter of the star cannot be less or equal to 0." };
	stars.add(s);
}
