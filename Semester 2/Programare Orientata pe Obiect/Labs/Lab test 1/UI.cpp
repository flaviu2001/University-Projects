//
// Created by jack on 5/4/20.
//

#include <iostream>
#include <algorithm>
#include <sstream>
#include "UI.h"

void strip(std::string &string) {
    while (!string.empty() && (string.back() == ' ' || string.back() == '\t' || string.back() == '\n'))
        string.pop_back();
    reverse(string.begin(), string.end());
    while (!string.empty() && (string.back() == ' ' || string.back() == '\t' || string.back() == '\n'))
        string.pop_back();
    reverse(string.begin(), string.end());
}

int string_to_int(const std::string& string) {
    int number = 0;
    for (auto x : string)
        number = number*10 + x-'0';
    return number;
}

void UI::start() {
    std::string line;
    while (std::getline(std::cin, line)){
        std::stringstream parser(line);
        std::string arg;
        parser >> arg;
        if (arg == "add"){
            std::string name, department;
            std::getline(parser, name, ',');
            strip(name);
            std::getline(parser, department, ',');
            strip(department);
            if (department == "Surgery"){
                int a;
                std::getline(parser, arg, ',');
                strip(arg);
                a = string_to_int(arg);
                int b;
                parser >> b;
                Pavilion *pavilion = new SurgeryWing(name, department, a, b);
                this->controller.addPavilion(pavilion);
            }else{
                int a;
                std::getline(parser, arg, ',');
                strip(arg);
                a = string_to_int(arg);
                int b;
                std::getline(parser, arg, ',');
                strip(arg);
                b = string_to_int(arg);
                int c;
                std::getline(parser, arg, ',');
                strip(arg);
                c = string_to_int(arg);
                double d;
                parser >> d;
                Pavilion *pavilion = new MidWifeChambers(name, department, a, b, c, d);
                this->controller.addPavilion(pavilion);
            }
        }else if (arg == "list") {
            std::string eff;
            if (parser >> eff){
                for (auto x : this->controller.get_efficient())
                    std::cout << x->to_string() << std::endl;
            }else{
                if (!this->controller.set)
                    for (auto x : this->controller.get_pavilions())
                        std::cout << x->to_string() << std::endl;
                else{
                    this->controller.write_to_file(this->controller.file_name);
                }
            }
        }else if (arg == "fileLocation"){
            std::string name;
            std::getline(parser, name);
            strip(name);
            this->controller.file_name = name;
            this->controller.set = true;
        }else if (arg == "exit"){
            break;
        }
    }
}
