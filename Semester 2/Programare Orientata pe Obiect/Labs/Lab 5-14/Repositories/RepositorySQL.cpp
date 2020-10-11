//
// Created by jack on 4/23/20.
//

#include <sqlite3.h>
#include "RepositorySQL.h"
#include "../Utilities/Utils.h"

RepositorySQL::RepositorySQL() {
    this->size = 0;
}

RepositorySQL::~RepositorySQL() {
    {} //Empty scope to fool the compiler from issuing a warning
}

void RepositorySQL::save_to_file(std::vector<Turret> &turret_list) {
    sqlite3* database;
    char *error_message;
    int exit_code = sqlite3_open(this->file_name.c_str(), &database);
    if (exit_code)
        throw RepositoryError("Error opening the database.");
    std::string command = "DELETE FROM turrets;";
    exit_code = sqlite3_exec(database, command.c_str(), nullptr, nullptr, &error_message);
    if (exit_code != SQLITE_OK)
        throw RepositoryError("Error executing command inside the database.");
    for (const auto &turret : turret_list){
        command = "INSERT INTO turrets VALUES ('";
        command += turret.get_location() + "','";
        command += turret.get_size() + "',";
        command += std::to_string(turret.get_aura_level()) + ",";
        command += std::to_string(turret.get_separate_parts()) + ",'";
        command += turret.get_vision() + "');";
        exit_code = sqlite3_exec(database, command.c_str(), nullptr, nullptr, &error_message);
        if (exit_code != SQLITE_OK)
            throw RepositoryError("Error executing command inside the database.");
    }
    sqlite3_close(database);
}

static int get_turret_callback(void *list, int count, char **data, char **columns)
{
    auto *turret_list = (std::vector<Turret> *)list;
    std::string strings[5];
    for (int i = 0; i < 5; ++i)
        strings[i] = std::string(data[i]);
    turret_list->push_back(Turret(strings[0], strings[1], strings[2], strings[3], strings[4]));
    return 0;
}

std::vector<Turret> RepositorySQL::get_turret_list() {
    sqlite3* database;
    char *error_message;
    int exit_code = sqlite3_open(this->file_name.c_str(), &database);
    if (exit_code)
        throw RepositoryError("Error opening the database.");
    std::string command = "SELECT * FROM turrets;";
    std::vector<Turret> turret_list;
    exit_code = sqlite3_exec(database, command.c_str(), get_turret_callback, &turret_list, &error_message);
    if (exit_code != SQLITE_OK)
        throw RepositoryError("Error executing command inside the database.");
    sqlite3_close(database);
    return turret_list;
}

void RepositorySQL::set_file_name(const std::string &file_name_to_set) {
    this->file_name = file_name_to_set;
    sqlite3* database;
    char *error_message;
    int exit_code = sqlite3_open(this->file_name.c_str(), &database);
    if (exit_code)
        throw RepositoryError("Error opening the database.");
    std::string command = "CREATE TABLE IF NOT EXISTS turrets (location VARCHAR(100) PRIMARY KEY,"
                                                              "size VARCHAR(100),"
                                                              "aura_level INTEGER,"
                                                              "separate_parts INTEGER,"
                                                              "vision VARCHAR(100)"
                                                              ");";
    exit_code = sqlite3_exec(database, command.c_str(), nullptr, nullptr, &error_message);
    if (exit_code != SQLITE_OK)
        throw RepositoryError("Error executing command inside the database.");
    sqlite3_close(database);
}

void RepositorySQL::add(const Turret &turret) {
    Repository::add(turret); // NOLINT(bugprone-parent-virtual-call)
    sqlite3* database;
    char *error_message;
    int exit_code = sqlite3_open(this->file_name.c_str(), &database);
    if (exit_code)
        throw RepositoryError("Error opening the database.");
    std::string command = "INSERT INTO turrets VALUES ('";
    command += turret.get_location() + "','";
    command += turret.get_size() + "',";
    command += std::to_string(turret.get_aura_level()) + ",";
    command += std::to_string(turret.get_separate_parts()) + ",'";
    command += turret.get_vision() + "');";
    exit_code = sqlite3_exec(database, command.c_str(), nullptr, nullptr, &error_message);
    if (exit_code != SQLITE_OK)
        throw RepositoryError("Error executing command inside the database.");
    sqlite3_close(database);
}

void RepositorySQL::remove(const std::string &location) {
    Repository::remove(location); // NOLINT(bugprone-parent-virtual-call)
    sqlite3* database;
    char *error_message;
    int exit_code = sqlite3_open(this->file_name.c_str(), &database);
    if (exit_code)
        throw RepositoryError("Error opening the database.");
    std::string command = "DELETE FROM turrets WHERE location='";
    command += location+"';";
    exit_code = sqlite3_exec(database, command.c_str(), nullptr, nullptr, &error_message);
    if (exit_code != SQLITE_OK)
        throw RepositoryError("Error executing command inside the database.");
    sqlite3_close(database);
}

void RepositorySQL::update(const std::string &location, const std::string &new_size, const int &new_aura_level,
                           const int &new_separate_parts, const std::string &new_vision) {
    Repository::update(location, new_size, new_aura_level, new_separate_parts, new_vision); // NOLINT(bugprone-parent-virtual-call)
    sqlite3* database;
    char *error_message;
    int exit_code = sqlite3_open(this->file_name.c_str(), &database);
    if (exit_code)
        throw RepositoryError("Error opening the database.");
    std::string command = "UPDATE turrets SET ";
    command = command + "size='" + new_size + "',";
    command = command + "aura_level=" + std::to_string(new_aura_level) + ",";
    command = command + "separate_parts=" + std::to_string(new_separate_parts) + ",";
    command = command + "vision='" + new_vision + "' WHERE location='" + location + "';";
    exit_code = sqlite3_exec(database, command.c_str(), nullptr, nullptr, &error_message);
    if (exit_code != SQLITE_OK)
        throw RepositoryError("Error executing command inside the database.");
    sqlite3_close(database);
}

bool RepositorySQL::has_turret(const std::string &location) {
    sqlite3* database;
    char *error_message;
    int exit_code = sqlite3_open(this->file_name.c_str(), &database);
    if (exit_code)
        throw RepositoryError("Error opening the database.");
    std::string command = "SELECT * FROM turrets WHERE location='";
    command += location + "';";
    std::vector<Turret> turret_list;
    exit_code = sqlite3_exec(database, command.c_str(), get_turret_callback, &turret_list, &error_message);
    if (exit_code != SQLITE_OK)
        throw RepositoryError("Error executing command inside the database.");
    sqlite3_close(database);
    return !turret_list.empty();
}

Turret RepositorySQL::find_turret(const std::string &location) {
    sqlite3 *database;
    char *error_message;
    int exit_code = sqlite3_open(this->file_name.c_str(), &database);
    if (exit_code)
        throw RepositoryError("Error opening the database.");
    std::string command = "SELECT * FROM turrets WHERE location='";
    command += location + "';";
    std::vector<Turret> turret_list;
    exit_code = sqlite3_exec(database, command.c_str(), get_turret_callback, &turret_list, &error_message);
    if (exit_code != SQLITE_OK)
        throw RepositoryError("Error executing command inside the database.");
    sqlite3_close(database);
    if (turret_list.empty())
        throw ValueError("Turret not found.");
    return turret_list.front();
}
