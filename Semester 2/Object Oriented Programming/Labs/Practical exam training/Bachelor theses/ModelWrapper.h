#pragma once
#include <unordered_map>
#include <string>
#include "StudentsModel.h"
#include "SearchModel.h"
#include "ChartGUI.h"

class ModelWrapper
{
public:
	std::unordered_map<std::string, StudentsModel*> m;
	std::unordered_map<std::string, SearchModel*> m2;
	ChartGUI* cg;
	void propagate();
};

