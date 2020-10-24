//
// Created by jack on 4/22/20.
//

#ifndef IMRE5_REPOSITORY_H
#define IMRE5_REPOSITORY_H


#include <vector>
#include "../Utilities/Domain.h"
#include "../Utilities/Exceptions.h"

class Repository {
    public:
        virtual void add(const Turret&);
        virtual void remove(const std::string&);
        virtual void update(const std::string&, const std::string&, const int&, const int&, const std::string&);
        virtual std::vector<Turret> get_turret_list() = 0;

        /**
         * Returns true if the turret with the given location exists.
         * @param location: location after which to search for the turret
         */
        virtual bool has_turret(const std::string &location);

        /**
         * Returns the turret with the given location from the repository if it exists.
         * @param location: location after which to search for the turret
         */
        virtual Turret find_turret(const std::string &location);

        virtual int length() = 0;
        virtual std::string get_file_name();
        virtual void set_file_name(const std::string &file_name_to_set);
        virtual bool is_file();
        virtual ~Repository()= default;
};


#endif //IMRE5_REPOSITORY_H
