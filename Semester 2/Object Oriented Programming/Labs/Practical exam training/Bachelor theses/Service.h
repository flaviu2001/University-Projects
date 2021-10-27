#pragma once
#include <vector>
#include "Domain.h"
#include <string>

class Service
{
private:
	std::vector<Teacher> teachers;
	std::vector<Student> students;
public:
	Service(std::string teacher_file, std::string student_file);
	std::vector<Student> get_students();
	std::vector<Teacher> get_teachers();
	/**
		This function returns a vector of all students whose name or id contain to_search as a substring
	*/
	std::vector<Student> get_by_name_id(std::string to_search);
	/**
		this function sets the coordinator of a student to the given teacher
	*/
	void add_coordinator(std::string student, std::string teacher);
	void update_email(std::string name, std::string email);
	void update_thesis(std::string name, std::string title);
};

