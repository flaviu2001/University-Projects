#include "Gene.h"
#include <sstream>

std::string Gene::toString()
{
	std::stringstream s;
	s << this->organismName << " | " << this->name << " | " << this->sequence<<std::endl;
	return std::string(s.str());
}