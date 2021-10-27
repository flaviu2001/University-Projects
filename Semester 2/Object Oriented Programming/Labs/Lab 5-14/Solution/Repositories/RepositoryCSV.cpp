//
// Created by jack on 4/11/20.
//

#include "RepositoryCSV.h"
#include "../Utilities/Validator.h"
#include <fstream>

RepositoryCSV::RepositoryCSV(){
    this->size = 0;
}

RepositoryCSV::~RepositoryCSV(){
    {} //Empty scope to fool the compiler from issuing a warning
}

std::vector<Turret> RepositoryCSV::get_turret_list() {
    Validator::validate_repository_file_existence(this);
    std::vector<Turret> list_to_return;
    std::ifstream fin(this->file_name);
    Turret turret;
    while (fin >> turret)
        list_to_return.push_back(turret);
    fin.close();
    return list_to_return;
}

void RepositoryCSV::save_to_file(std::vector<Turret> &turret_list) {
    std::ofstream fout(this->file_name);
    for (const auto& turret : turret_list)
        fout << turret << std::endl;
    fout.close();
}

void RepositoryCSV::set_file_name(const std::string &file_name_to_set) {
    this->file_name = file_name_to_set;
    std::ifstream fin (this->file_name);
    if (!fin.good()) {
        fin.close();
        std::ofstream fout(this->file_name);
        fout.close();
        fin.open(this->file_name);
        return;
    }
    Turret turret;
    this->size = 0;
    while (fin >> turret)
        ++this->size;
    fin.close();
}
