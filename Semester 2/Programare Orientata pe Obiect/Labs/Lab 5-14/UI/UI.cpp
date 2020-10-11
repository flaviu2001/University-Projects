//
// Created by jack on 3/21/20.
//

#include "UI.h"
#include "../Utilities/Utils.h"
#include <iostream>
#include <sstream>
#include <cstdlib>

UI::UI(Service &_service) : service{_service}{}

void UI::start() {
    std::string command;
    while (std::getline(std::cin, command)){
        std::stringstream parser(command);
        std::string argument;
        parser >> argument;
        if (argument == "fileLocation"){
            std::string file_name;
            parser >> file_name;
            while (parser >> argument){
                file_name += " ";
                file_name += argument;
            }
            this->service.set_file_name_main_repository(file_name);
        }else if (argument == "mylistLocation"){
            std::string file_name;
            parser >> file_name;
            while (parser >> argument){
                file_name += " ";
                file_name += argument;
            }
            this->service.set_file_name_saved_turrets_repository(file_name);
        }else if (argument == "mode"){
            parser >> argument;
            this->service.set_mode(argument);
        }else if (argument == "add"){
            Turret turret;
            try{
                parser >> turret;
                this->service.add(turret.get_location(), turret.get_size(), turret.get_aura_level(), turret.get_separate_parts(), turret.get_vision());
                std::cout << "Successfully added the turret\n";
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
            }catch (ModeError &me){
                std::cout << me.what() << "\n";
            }
        }else if (argument == "update"){
            Turret turret;
            try{
                parser >> turret;
                this->service.update(turret.get_location(), turret.get_size(), turret.get_aura_level(), turret.get_separate_parts(), turret.get_vision());
                std::cout << "Successfully updated the turret\n";
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
            }catch (ModeError &me){
                std::cout << me.what() << "\n";
            }
        }else if (argument == "delete"){
            std::string location;
            parser >> argument;
            location += argument;
            while (parser >> argument)
                location += " " + argument;
            try{
                this->service.remove(location);
                std::cout << "Successfully deleted the turret\n";
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
            }catch (ModeError &me){
                std::cout << me.what() << "\n";
            }
        }else if (argument == "list"){
            if (parser >> argument){
                std::string size = argument;
                size.pop_back(); //Removes the trailing comma
                int minimum_parts;
                parser >> minimum_parts;
                try{
                    auto turret_list = this->service.get_turret_list_by_size_by_parts(size, minimum_parts);
                    std::cout << "There are a total of " << turret_list.size() << " turrets with your requirements.\n";
                    for (const auto &turret : turret_list)
                        std::cout << turret.get_location() << " " << turret.get_size() << " " << turret.get_aura_level() << " " << turret.get_separate_parts() << " " << turret.get_vision() << "\n";
                }catch (ValueError &ve){
                    std::cout << ve.what() << "\n";
                }catch (ModeError &me){
                    std::cout << me.what() << "\n";
                }
                continue;
            }
            try{
                std::cout << "There are a total of " << this->service.get_turret_list().size() << " turrets.\n";
                for (const auto &turret : this->service.get_turret_list())
                    std::cout << turret.get_location() << " " << turret.get_size() << " " << turret.get_aura_level() << " " << turret.get_separate_parts() << " " << turret.get_vision() << "\n";
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
            }catch (ModeError &me){
                std::cout << me.what() << "\n";
            }
        }else if (argument == "next"){
            try {
                Turret turret = this->service.next_turret();
                std::cout << turret.get_location() << " " << turret.get_size() << " " << turret.get_aura_level() << " "
                          << turret.get_separate_parts() << " " << turret.get_vision() << "\n";
            }catch(ValueError &ve){
                std::cout << ve.what() << "\n";
            }catch (ModeError &me){
                std::cout << me.what() << "\n";
            }
        }else if (argument == "mylist"){
            try{
                if (this->service.get_mode() == B_MODE){
                    std::string file = this->service.get_file_name_saved_turrets_repository();
                    std::string command_to_execute;
                    if (termination(file) == "html") {
                        command_to_execute = "firefox ";
                        command_to_execute += file;
                        system(command_to_execute.c_str());
                    }else if (termination(file) == "csv") {
                        command_to_execute = "libreoffice --calc ";
                        command_to_execute += file;
                        system(command_to_execute.c_str());
                    }else if (termination(file) == "sql") {
                        auto turret_list = this->service.get_saved_turrets_list();
                        std::cout << "There are " << turret_list.size() << " turrets.\n";
                        for (const auto &turret : turret_list)
                            std::cout << turret << "\n";
                    }else{
                        command_to_execute = "subl ";
                        command_to_execute += file;
                        system(command_to_execute.c_str());
                    }
                }else std::cout << "Incorrect mode.\n";
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
            }catch (ModeError &me){
                std::cout << me.what() << "\n";
            }
        }else if (argument == "save"){
            parser >> argument;
            try{
                this->service.save_turret(argument);
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
            }catch (ModeError &me){
                std::cout << me.what() << "\n";
            }
        }else if (argument == "undo"){
            try{
                this->service.undo();
                std::cout << "The operation was successfully undone.\n";
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
            }
        }else if (argument == "redo"){
            try{
                this->service.redo();
                std::cout << "The operation was successfully redone.\n";
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
            }
        }else if (argument == "exit")
            break;
        else std::cout << "Improper command.\n";
    }
}
