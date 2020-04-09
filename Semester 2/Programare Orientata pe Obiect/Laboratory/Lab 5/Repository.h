//
// Created by jack on 3/21/20.
//

#ifndef IMRE5_REPOSITORY_H
#define IMRE5_REPOSITORY_H

#include "DynamicArray.h"
#include "Domain.h"
#include <string>

class Repository {
    private:
        DynamicArray<Turret> turret_list;
    public:
        Repository();

        /**
         * Adds a turret to the main_repository
         * @param turret: a Turret object
         */
        void add(const Turret &turret);

        /**
         * Removes the first apparition(should be only one) of the turret with given location from the main_repository
         * @param location: location of the turret to be removed
         */
        void remove(const std::string &location);

        /**
         * Updates the turret from main_repository with the given location.
         * @param location: string - new location of the turret
         * @param new_size: string - new size of the turret
         * @param new_aura_level: int - new level aura of the turret
         * @param new_separate_parts: int - new number of separate parts of the turret
         * @param new_vision: string - new vision of the turret
         */
        void update(const std::string &location,
                    const std::string &new_size,
                    const int &new_aura_level,
                    const int &new_separate_parts,
                    const std::string &new_vision);
        /**
         * Returns true if the turret with the given location exists.
         * @param location: location after which to search for the turret
         */
        bool has_turret(const std::string &location);

        /**
         * Returns the turret with the given location from the repsitory if it exists.
         * @param location: location after which to search for the turret
         */
        Turret find_turret(const std::string &location);

        /**
         * Returns the list of turrets in a DynamicArray object from the repository.
         */
        DynamicArray<Turret>& get_turret_list();
};


#endif //IMRE5_REPOSITORY_H
