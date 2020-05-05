#include "Spaceship.h"
#include <sstream>
#include <vector>
#include <iostream>

using namespace std;

Spaceship::Spaceship(const std::string & _name, const std::string & _captain, const std::string & _type): name{_name}, captain{_captain}, type{_type}
{
}

Spaceship::~Spaceship()
{
	cout << "Spaceship " << this->name << " is being destroyed." << endl;
}

/*
	Tokenizes a string.
	Input:	str - the string to be tokenized.
			delimiter - char - the delimiter used for tokenization
	Output: a vector of strings, containing the tokens
*/
vector<string> tokenize(string str, char delimiter)
{
	vector <string> result;
	stringstream ss(str);
	string token;
	while (getline(ss, token, ','))
		result.push_back(token);

	return result;
}

std::istream & operator>>(std::istream & is, Spaceship & s)
{
	string line;
	getline(is, line);

	vector<string> tokens = tokenize(line, ',');
	if (tokens.size() != 3) // make sure all the starship data was valid
		return is;
	s.name = tokens[0];
	s.captain = tokens[1];
	s.type = tokens[2];

	return is;
}

std::ostream & operator<<(std::ostream & os, const Spaceship & s)
{
	os << s.name << "," << s.captain << "," << s.type << "\n";
	return os;
}
