#pragma once

#include "TableModel.h"
#include "ListModel.h"
#include <unordered_map>

class ModelWrapper
{
public:
	std::unordered_map<std::string, TableModel*> mp;
	std::unordered_map<std::string, ListModel*> mp2;
	void propagate();
};

