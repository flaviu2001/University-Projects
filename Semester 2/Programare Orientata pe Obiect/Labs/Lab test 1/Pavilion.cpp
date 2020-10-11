//
// Created by jack on 5/4/20.
//

#include "Pavilion.h"
#include <sstream>

Pavilion::~Pavilion() = default;

MidWifeChambers::MidWifeChambers(const std::string &name, const std::string &department, int number_of_healers, int number_of_mothers, int number_of_babies,
                                 double average_grade) {
    this->pavilion_name = name;
    this->department_type = department;
    this->number_of_healers = number_of_healers;
    this->number_of_mothers = number_of_mothers;
    this->number_of_newborns = number_of_babies;
    this->average_grade = average_grade;
}

bool MidWifeChambers::is_efficient() {
    return this->average_grade > 8.5 && this->number_of_newborns >= this->number_of_mothers;
}

std::string MidWifeChambers::to_string() {
    std::stringstream ss;
    ss << this->pavilion_name << " " << this->department_type << " " << this->number_of_healers << " " << this->number_of_mothers << " " << this->number_of_newborns << " " << this->average_grade;
    return ss.str();
}

bool SurgeryWing::is_efficient() {
    return this->number_of_patients >= this->number_of_healers*2;
}

std::string SurgeryWing::to_string() {
    std::stringstream ss;
    ss << this->pavilion_name << " " << this->department_type << " " << this->number_of_healers << " " << this->number_of_patients;
    return ss.str();
}

SurgeryWing::SurgeryWing(const std::string &name, const std::string &department, int number_of_healers,
                         int number_of_patients) {
    this->pavilion_name = name;
    this->department_type = department;
    this->number_of_healers = number_of_healers;
    this->number_of_patients = number_of_patients;
}
