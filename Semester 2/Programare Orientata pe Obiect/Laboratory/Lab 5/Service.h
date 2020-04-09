//
// Created by jack on 3/21/20.
//

#ifndef IMRE5_SERVICE_H
#define IMRE5_SERVICE_H

#include "Repository.h"
#define GOD_MODE "A"
#define B_MODE "B"

class Service {
    private:
        Repository main_repository, saved_turrets_repository;
        std::string mode;
        int iterator;

        /**
         * Resets the iterator used in the next_turret() function.
         */
        void reset_iterator();
    public:
        explicit Service(Repository &repository);

        /**
         * Sets the mode of the service, A has all access to operations.
         * @param new_mode: string - new_mode
         */
        void set_mode(const std::string &new_mode);

        /**
         * Adds a turret to the list of turrets from the main_repository
         * If such a turret already exists a ValueError is thrown
         * If mode is not "A" a ValueError is thrown
         * @param location: string - location of a turret
         * @param size: string - size of a turret
         * @param aura_level: int - level aura of a turret
         * @param separate_parts: int - number of separate parts of a turret
         * @param vision: string - vision of a turret
         */
        void add(const std::string &location,
                 const std::string &size,
                 const int &aura_level,
                 const int &separate_parts,
                 const std::string &vision);

        /**
         * Removes a turret from the list of turrets from the main_repository
         * If such a turret doesn't exist a ValueError is thrown
         * If mode is not "A" a ValueError is thrown
         * @param location: string - location of a turret to be removed
         */
        void remove(const std::string &location);

        /**
         * Updates a turret from the list of turrets in the main_repository based on its location
         * If such a turret doesn't exist a ValueError is thrown
         * If mode is not "A" a ValueError is thrown
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
         * Sets an iterator towards the first turret of the list if it's the first time calling the function, or
         * an add or remove have been called before, and upon each call of the function the turret returned will be
         * the next in the list until the last one is reached, after which it loops back to the first one.
         * @return: a turret
         */
        Turret next_turret();

        /**
         * Saves the turret to the list from saved_turrets_repository.
         * @param location: location of the turret from main_repository to add.
         */
        void save_turret(const std::string &location);

        /**
         * Returns the list of turrets from the main_repository in a DynamicArray form
         */
        DynamicArray<Turret>& get_turret_list();

        /**
         * Returns a list of turrets from the main_repository filtered by a certain size and minimum number of parts.
         * @param size: string representing the wanted size
         * @param parts: int representing the number of minimum parts
         * @return: A DynamicArray of turrets
         */
        DynamicArray<Turret> get_turret_list_by_size_by_parts(const std::string &size, int parts);

        /**
         * Returns the list of turrets from the saved_turrets_repository.
         */
        DynamicArray<Turret>& get_saved_turrets_list();
};


#endif //IMRE5_SERVICE_H
