#include "Service.h"
#include <fstream>
#include <algorithm>
using namespace std;

Service::Service(std::string teacher_file, std::string student_file)
{
	ifstream fin(teacher_file);
	Teacher t;
	while (fin >> t)
		this->teachers.push_back(t);
	fin.close();
	ifstream fin2(student_file);
	Student t2;
	while (fin2 >> t2)
		this->students.push_back(t2);
	fin2.close();
}

std::vector<Student> Service::get_students()
{
	std::vector<Student> v = this->students;
	sort(v.begin(), v.end(), [](Student& a, Student& b) {return a.year < b.year; });
	auto it = v.begin();
	while (it != v.end() && it->year == 2020)
		++it;
	reverse(it, v.end());
	return v;
}

std::vector<Teacher> Service::get_teachers()
{
	return this->teachers;
}

std::vector<Student> Service::get_by_name_id(std::string to_search)
{
	std::vector<Student> v;
	for (auto x : this->students)
		if ((x.name.find(to_search) != std::string::npos) || (x.id.find(to_search) != std::string::npos))
			v.push_back(x);
	return v;
}

void Service::add_coordinator(std::string student, std::string teacher)
{
	for (auto& x : this->students)
		if (x.name == student)
			x.coordinator = teacher;
}

void Service::update_email(std::string name, std::string email)
{
	for (auto& x : this->students)
		if (x.name == name)
			x.email = email;
}

void Service::update_thesis(std::string name, std::string title)
{
	for (auto& x : this->students)
		if (x.name == name)
			x.title = title;
}
