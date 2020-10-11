//
// Created by jack on 4/22/20.
//

#include "RepositoryHTML.h"
#include "../Utilities/Validator.h"
#include "../Utilities/Utils.h"
#include <fstream>

RepositoryHTML::RepositoryHTML(){
    this->size = 0;
}

RepositoryHTML::~RepositoryHTML(){
    {} //Empty scope to fool the compiler from issuing a warning
}

std::vector<Turret> RepositoryHTML::get_turret_list() {
    Validator::validate_repository_file_existence(this);
    std::vector<Turret> list_to_return;
    std::ifstream fin(this->file_name);
    std::string line;
    for (int i = 1; i <= 14; ++i)
        std::getline(fin, line);
    do{
        std::getline(fin, line);
        strip(line);
        if (line != "<tr>")
            break;

        std::string location, turret_size, aura_level, separate_parts, vision;

        std::getline(fin, location);
        html_strip(location);

        std::getline(fin, turret_size);
        html_strip(turret_size);

        std::getline(fin, aura_level);
        html_strip(aura_level);

        std::getline(fin, separate_parts);
        html_strip(separate_parts);

        std::getline(fin, vision);
        html_strip(vision);

        std::getline(fin, line);

        list_to_return.push_back(Turret(location, turret_size, aura_level, separate_parts, vision)); // NOLINT(modernize-use-emplace)
    }while (true);
    fin.close();
    return list_to_return;
}

void RepositoryHTML::save_to_file(std::vector<Turret> &turret_list) {
    std::ofstream fout(this->file_name);
    fout << "<!DOCTYPE html>\n"
            "<html>\n"
            "\t<head>\n"
            "\t\t<title>Turrets</title>\n"
            "\t</head>\n"
            "\t<body>\n"
            "\t\t<table border=\"1\">\n"
            "\t\t\t<tr>\n"
            "\t\t\t\t<td>Location</td>\n"
            "\t\t\t\t<td>Size</td>\n"
            "\t\t\t\t<td>Aura Level</td>\n"
            "\t\t\t\t<td>Separate parts</td>\n"
            "\t\t\t\t<td>Vision</td>\n"
            "\t\t\t</tr>\n";
    for (const auto& turret : turret_list) {
        fout << "\t\t\t<tr>\n";
        fout << "\t\t\t\t<td>" << turret.get_location() << "</td>\n";
        fout << "\t\t\t\t<td>" << turret.get_size() << "</td>\n";
        fout << "\t\t\t\t<td>" << turret.get_aura_level() << "</td>\n";
        fout << "\t\t\t\t<td>" << turret.get_separate_parts() << "</td>\n";
        fout << "\t\t\t\t<td>" << turret.get_vision() << "</td>\n";
        fout << "\t\t\t</tr>\n";
    }
    fout << "\t\t</table>\n"
            "\t</body>\n"
            "</html>";
    fout.close();
}

void RepositoryHTML::set_file_name(const std::string &file_name_to_set) {
    this->file_name = file_name_to_set;
    std::ifstream fin (this->file_name);
    if (!fin.good()) {
        fin.close();
        std::ofstream fout(this->file_name);
        std::vector<Turret> empty_list;
        this->save_to_file(empty_list);
        fout.close();
        fin.open(this->file_name);
        return;
    }
    Turret turret;
    this->size = this->get_turret_list().size();
    fin.close();
}