//
// Created by jack on 5/14/20.
//

#ifndef IMRE5_SETTINGSPARSER_H
#define IMRE5_SETTINGSPARSER_H

#include "Service/Service.h"

class SettingsParser {
public:
    static Service get_service();
    static std::string get_main();
    static std::string get_mylist();
    static void set_settings(Service &service);
};


#endif //IMRE5_SETTINGSPARSER_H
