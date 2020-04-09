//
// Created by jack on 3/21/20.
//

#include "UI.h"
#include "Exceptions.h"
#include <iostream>
#include <sstream>

UI::UI(Service &the_service): service{the_service}{}

void UI::start() {
    std::string command;
    while (std::getline(std::cin, command)){
        std::stringstream parser(command);
        std::string argument;
        parser >> argument;
        if (argument == "mode"){
            parser >> argument;
            this->service.set_mode(argument);
        }else if (argument == "add"){
            std::string location, size, vision;
            int aura_level = 0, separate_parts = 0;

            /* Location */
            parser >> argument;
            location += argument;
            while (argument.back() != ','){
                parser >> argument;
                location += " " + argument;
            }
            location.pop_back(); //trims the trailing comma

            /* size */
            parser >> argument;
            size += argument;
            while (argument.back() != ','){
                parser >> argument;
                size += " " + argument;
            }
            size.pop_back(); //trims the trailing comma

            /* aura_level */
            parser >> argument;
            bool is_digit = true;
            for (int i = 0; i < argument.size()-1; ++i)
                if (argument[i] > '9' || argument[i] < '0'){
                    is_digit = false;
                    break;
                }
            if (argument.back() != ',')
                is_digit = false;

            if (!is_digit){
                std::cout << "Invalid command\n";
                continue;
            }
            for (int i = 0; i < argument.size()-1; ++i)
                aura_level = aura_level*10 + argument[i]-'0';

            /* separate_parts */
            parser >> argument;
            is_digit = true;
            for (int i = 0; i < argument.size()-1; ++i)
                if (argument[i] > '9' || argument[i] < '0'){
                    is_digit = false;
                    break;
                }
            if (argument.back() != ',')
                is_digit = false;

            if (!is_digit){
                std::cout << "Invalid command\n";
                continue;
            }
            for (int i = 0; i < argument.size()-1; ++i)
                separate_parts = separate_parts*10 + argument[i]-'0';

            /* vision */
            parser >> argument;
            vision += argument;
            while (parser >> argument){
                vision += " " + argument;
            }
            try{
                this->service.add(location, size, aura_level, separate_parts, vision);
                std::cout << "Successfully added the turret\n";
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
            }
        }else if (argument == "update"){
            std::string location, new_size, new_vision;
            int new_aura_level = 0, new_separate_parts = 0;

            /* Location */
            parser >> argument;
            location += argument;
            while (argument.back() != ','){
                parser >> argument;
                location += " " + argument;
            }
            location.pop_back(); //trims the trailing comma

            /* new_size */
            parser >> argument;
            new_size += argument;
            while (argument.back() != ','){
                parser >> argument;
                new_size += " " + argument;
            }
            new_size.pop_back(); //trims the trailing comma

            /* new_aura_level */
            parser >> argument;
            bool is_digit = true;
            for (int i = 0; i < argument.size()-1; ++i)
                if (argument[i] > '9' || argument[i] < '0'){
                    is_digit = false;
                    break;
                }
            if (argument.back() != ',')
                is_digit = false;

            if (!is_digit){
                std::cout << "Invalid command\n";
                continue;
            }
            for (int i = 0; i < argument.size()-1; ++i)
                new_aura_level = new_aura_level * 10 + argument[i] - '0';

            /* new_separate_parts */
            parser >> argument;
            is_digit = true;
            for (int i = 0; i < argument.size()-1; ++i)
                if (argument[i] > '9' || argument[i] < '0'){
                    is_digit = false;
                    break;
                }
            if (argument.back() != ',')
                is_digit = false;

            if (!is_digit){
                std::cout << "Invalid command\n";
                continue;
            }
            for (int i = 0; i < argument.size()-1; ++i)
                new_separate_parts = new_separate_parts * 10 + argument[i] - '0';

            /* new_vision */
            parser >> argument;
            new_vision += argument;
            while (parser >> argument){
                new_vision += " " + argument;
            }
            try{
                this->service.update(location, new_size, new_aura_level, new_separate_parts, new_vision);
                std::cout << "Successfully updated the turret\n";
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
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
                }
                continue;
            }
            std::cout << "There are a total of " << this->service.get_turret_list().size() << " turrets.\n";
            for (const auto &turret : this->service.get_turret_list())
                std::cout << turret.get_location() << " " << turret.get_size() << " " << turret.get_aura_level() << " " << turret.get_separate_parts() << " " << turret.get_vision() << "\n";
        }else if (argument == "next"){
            try {
                Turret turret = this->service.next_turret();
                std::cout << turret.get_location() << " " << turret.get_size() << " " << turret.get_aura_level() << " "
                          << turret.get_separate_parts() << " " << turret.get_vision() << "\n";
            }catch(ValueError &ve){
                std::cout << ve.what() << "\n";
            }
        }else if (argument == "mylist"){
            try{
                auto turret_list = this->service.get_saved_turrets_list();
                std::cout << "There are a total of " << turret_list.size() << " saved turrets.\n";
                for (const auto &turret : turret_list)
                    std::cout << turret.get_location() << " " << turret.get_size() << " " << turret.get_aura_level() << " " << turret.get_separate_parts() << " " << turret.get_vision() << "\n";
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
            }
        }else if (argument == "save"){
            parser >> argument;
            try{
                this->service.save_turret(argument);
            }catch (ValueError &ve){
                std::cout << ve.what() << "\n";
            }
        }else if (argument == "exit")
            break;
        else std::cout << "Improper command.\n";
    }
}
