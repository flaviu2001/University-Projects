//
// Created by jack on 5/14/20.
//

#include <fstream>
#include <sstream>
#include <Repositories/RepositoryHTML.h>
#include <Repositories/RepositorySQL.h>
#include "SettingsParser.h"
#include "Utils.h"

Service SettingsParser::get_service() {
    std::ifstream fin ("settings.properties");
    std::string line;
    Repository *main_repo = nullptr, *saved_repo = nullptr;
    while (getline(fin, line)){
        std::stringstream parser(line);
        std::string name, value;
        getline(parser, name, '=');
        if (name == "main"){
            getline(parser, value);
            if (value == "memory")
                main_repo = new RepositoryMemory;
            else if (termination(value) == "sql") {
                main_repo = new RepositorySQL;
                main_repo->set_file_name(value);
            }else if (termination(value) == "html") {
                main_repo = new RepositoryHTML;
                main_repo->set_file_name(value);
            }else{
                main_repo = new RepositoryCSV;
                main_repo->set_file_name(value);
            }
        }else if (name == "mylist"){
            getline(parser, value);
            if (value == "memory")
                saved_repo = new RepositoryMemory;
            else if (termination(value) == "sql") {
                saved_repo = new RepositorySQL;
                saved_repo->set_file_name(value);
            }else if (termination(value) == "html") {
                saved_repo = new RepositoryHTML;
                saved_repo->set_file_name(value);
            }else{
                saved_repo = new RepositoryCSV;
                saved_repo->set_file_name(value);
            }
        }
    }
    fin.close();
//    if (saved_repo != nullptr && saved_repo->is_file() && main_repo != nullptr && !main_repo->is_file())
//        throw ValueError("Can't store main repo in memory and get saved repo from file");
    return Service(main_repo, saved_repo);
}

std::string SettingsParser::get_main() {
    std::ifstream fin ("settings.properties");
    std::string line;
    while (getline(fin, line)){
        std::stringstream parser(line);
        std::string name, value;
        getline(parser, name, '=');
        getline(parser, value);
        if (name == "main") {
            fin.close();
            return value;
        }
    }
    fin.close();
    return "";
}

std::string SettingsParser::get_mylist() {
    std::ifstream fin ("settings.properties");
    std::string line;
    while (getline(fin, line)){
        std::stringstream parser(line);
        std::string name, value;
        getline(parser, name, '=');
        getline(parser, value);
        if (name == "mylist") {
            fin.close();
            return value;
        }
    }
    return "";
}

void SettingsParser::set_settings(Service &service) {
    std::ofstream fout ("settings.properties");
    fout << "main=";
    if (!service.is_repo_file())
        fout << "memory\n";
    else fout << service.get_file_name_main_repository() << "\n";
    fout << "mylist=";
    if (!service.is_saved_file())
        fout << "memory\n";
    else fout << service.get_file_name_saved_turrets_repository() << "\n";
    fout.close();
}
