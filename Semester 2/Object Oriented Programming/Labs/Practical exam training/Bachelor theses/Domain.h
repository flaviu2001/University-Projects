#pragma once

#include<string>

class Teacher {
public:
	std::string name;
	friend std::istream& operator>>(std::istream& reader, Teacher& teacher);
};

class Student {
public:
	std::string id;
	std::string name;
	std::string email;
	int year;
	std::string title;
	std::string coordinator;

	friend std::istream& operator>>(std::istream& reader, Student& student);
	std::string to_string();
};