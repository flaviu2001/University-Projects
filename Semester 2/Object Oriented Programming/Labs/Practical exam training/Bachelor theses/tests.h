#pragma once
#include "Service.h"
#include <cassert>
#include <algorithm>

void test__service_add_coordinator()
{
	Service s("teachers.txt", "students.txt");
	s.add_coordinator("Jackie", "Suzy");
	bool any = false;
	for (auto x : s.get_students())
		if (x.name == "Jackie" && x.coordinator == "Suzy")
			any = true;
	assert(any);
}

void test__service_service_get_by_name_id()
{
	Service s("teachers.txt", "students.txt");
	auto v = s.get_by_name_id("ck");
	std::vector<std::string> v2;
	for (auto x : v)
		v2.push_back(x.name);
	sort(v2.begin(), v2.end());
	assert(v2 == std::vector<std::string>({ "Jackie", "Rocky" }));
}