//
// Created by jack on 4/11/20.
//

#include "RepositoryFile.h"
#include "Exceptions.h"
#include <fstream>

RepositoryFile::RepositoryFile(): size{0}{}

void RepositoryFile::add(const Turret &turret) {
    if (this->file_name.empty())
        throw ValueError("The file name cannot be empty. (Perhaps you forgot to initialize it)");
    std::ofstream fout(this->file_name, std::ios::app);
    fout << turret;
    fout.close();
    ++this->size;
}

void RepositoryFile::remove(const std::string &location) {
    std::vector<Turret> turret_list = this->get_turret_list();
    for (auto turret = turret_list.begin(); turret != turret_list.end(); ++turret)
        if (turret->get_location() == location){
            turret_list.erase(turret);
            --this->size;
            break;
        }
    this->save_to_file(turret_list);
}

void RepositoryFile::update(const std::string &location, const std::string &new_size, const int &new_aura_level,
                            const int &new_separate_parts, const std::string &new_vision) {
    std::vector<Turret> turret_list = this->get_turret_list();
    for (auto &element : turret_list)
        if (element.get_location() == location){
            element.set_size(new_size);
            element.set_aura_level(new_aura_level);
            element.set_separate_parts(new_separate_parts);
            element.set_vision(new_vision);
            break;
        }
    this->save_to_file(turret_list);
}

std::vector<Turret> RepositoryFile::get_turret_list() {
    if (this->file_name.empty())
        throw ValueError("The file name cannot be empty. (Perhaps you forgot to initialize it)");
    std::vector<Turret> list_to_return;
    std::ifstream fin(this->file_name);
    Turret turret;
    while (fin >> turret)
        list_to_return.push_back(turret);
    fin.close();
    return list_to_return;
}

void RepositoryFile::save_to_file(std::vector<Turret> &turret_list) {
    std::ofstream fout(this->file_name);
    for (const auto& turret : turret_list)
        fout << turret;
    fout.close();
}

void RepositoryFile::set_file_name(const std::string &file_name_to_set) {
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

bool RepositoryFile::has_turret(const std::string &location) {
    std::vector<Turret> turret_list = this->get_turret_list();
    for (const auto &element : turret_list)
        if (element.get_location() == location)
            return true;
    return false;
}

Turret RepositoryFile::find_turret(const std::string &location) {
    std::vector<Turret> turret_list = this->get_turret_list();
    for (const auto &element: turret_list)
        if (element.get_location() == location)
            return element;
    throw ValueError("Turret not found.");
}

int RepositoryFile::length() {
    return this->size;
}

Turret RepositoryFile::operator[](const int position) {
    if (this->file_name.empty())
        throw ValueError("The file name cannot be empty. (Perhaps you forgot to initialize it)");
    if (position < 0 || position >= this->size)
        throw ValueError("Invalid position.");
    std::ifstream fin (this->file_name);
    for (int i = 0; i < position; ++i){
        std::string fodder;
        for (int _ = 0; _ < 5; ++_)
            std::getline(fin, fodder);
    }
    std::string location;
    std::string turret_size;
    int aura_level;
    int separate_parts;
    std::string vision;
    std::getline(fin, location);
    std::getline(fin, turret_size);
    std::string number;
    std::getline(fin, number);
    aura_level = 0;
    for (auto character : number)
        aura_level = aura_level*10 + character-'0';
    std::getline(fin, number);
    separate_parts = 0;
    for (auto character : number)
        separate_parts = separate_parts*10 + character-'0';
    std::getline(fin, vision);
    return Turret(location, turret_size, aura_level, separate_parts, vision);
}

