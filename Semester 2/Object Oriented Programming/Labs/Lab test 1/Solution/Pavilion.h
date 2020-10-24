//
// Created by jack on 5/4/20.
//

#ifndef TESTOOP_PAVILION_H
#define TESTOOP_PAVILION_H

#include <string>

class Pavilion {
protected:
    std::string pavilion_name;
    std::string department_type;
    int number_of_healers;
public:
    virtual ~Pavilion();
    virtual bool is_efficient() = 0;
    virtual std::string to_string() = 0;
};

class MidWifeChambers : public Pavilion {
private:
    int number_of_mothers;
    int number_of_newborns;
public:
    double average_grade;

    MidWifeChambers(const std::string &name, const std::string &department, int number_of_healers, int number_of_mothers, int number_of_babies, double average_grade);
    bool is_efficient() override;
    std::string to_string() override;
};

class SurgeryWing : public Pavilion {
private:
    int number_of_patients;

public:
    SurgeryWing(const std::string &name, const std::string &department, int number_of_healers, int number_of_patients);
    bool is_efficient() override;
    std::string to_string() override;
};

#endif //TESTOOP_PAVILION_H
