#pragma once
#include "Service.h"
#include <cassert>

void test__add_star__one_star__star_was_added()
{
	remove("test_star.txt");
	remove("test_astronomers.txt");
	Repository<Star> r1{"test_star.txt"};
	Repository<Astronomer> r2{ "test_astronomers.txt" };
	Service s{ r2, r1 };
	s.add_star("A", "B", 1, 2, 3);
	assert(s.get_stars().size() == 1);
}

void test__add_star__two_stars__same_name()
{
	remove("test_star.txt");
	remove("test_astronomers.txt");
	Repository<Star> r1{ "test_star.txt" };
	Repository<Astronomer> r2{ "test_astronomers.txt" };
	Service s{ r2, r1 };
	s.add_star("A", "B", 1, 2, 3);
	try {
		s.add_star("A", "C", 1, 2, 3);
		assert(false);
	}catch (std::exception& e) {
		assert(true);
	}catch (...) {
		assert(false);
	}
}

void test__add_star__one_stars__empty_name()
{
	remove("test_star.txt");
	remove("test_astronomers.txt");
	Repository<Star> r1{ "test_star.txt" };
	Repository<Astronomer> r2{ "test_astronomers.txt" };
	Service s{ r2, r1 };
	try {
		s.add_star("", "B", 1, 2, 3);
		assert(false);
	}
	catch (std::exception& e) {
		assert(true);
	}
	catch (...) {
		assert(false);
	}
}

void test__add_star__one_stars__0_diameter()
{
	remove("test_star.txt");
	remove("test_astronomers.txt");
	Repository<Star> r1{ "test_star.txt" };
	Repository<Astronomer> r2{ "test_astronomers.txt" };
	Service s{ r2, r1 };
	try {
		s.add_star("A", "B", 1, 2, 0);
		assert(false);
	}
	catch (std::exception& e) {
		assert(true);
	}
	catch (...) {
		assert(false);
	}
}

void test_all()
{
	test__add_star__two_stars__same_name();
	test__add_star__one_star__star_was_added();
	test__add_star__one_stars__0_diameter();
	test__add_star__one_stars__empty_name();
}