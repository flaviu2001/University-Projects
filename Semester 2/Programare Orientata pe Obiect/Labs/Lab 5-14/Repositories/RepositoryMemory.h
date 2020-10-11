//
// Created by jack on 3/21/20.
//

#ifndef IMRE5_REPOSITORYMEMORY_H
#define IMRE5_REPOSITORYMEMORY_H

#include "../Utilities/DynamicArray.h"
#include "../Utilities/Domain.h"
#include "Repository.h"
#include <string>
#include <vector>

class RepositoryMemory : public Repository{
    private:
        std::vector<Turret> turret_list;
    public:
        RepositoryMemory();
        ~RepositoryMemory() override;

        /**
         * Adds a turret to the main_repository
         * @param turret: a Turret object
         */
        void add(const Turret &turret) override;

        /**
         * Removes the first apparition(should be only one) of the turret with given location from the main_repository
         * @param location: location of the turret to be removed
         */
        void remove(const std::string &location) override;

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
                    const std::string &new_vision) override;

        /**
         * Returns the list of turrets in a DynamicArray object from the repository.
         */
        std::vector<Turret> get_turret_list() override;

        /**
         * Returns the number of elements in the repository
         */
        int length() override;
};


#endif //IMRE5_REPOSITORYMEMORY_H
