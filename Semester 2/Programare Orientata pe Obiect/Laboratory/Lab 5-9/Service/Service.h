//
// Created by jack on 3/21/20.
//

#ifndef IMRE5_SERVICE_H
#define IMRE5_SERVICE_H

#include "../Repositories/RepositoryMemory.h"
#include "../Repositories/RepositoryCSV.h"
class Service;
#include "../Utilities/Validator.h"

#define A_MODE "A"
#define B_MODE "B"

class Service {
    private:
        friend class Validator;
        Repository *main_repository;
        Repository *saved_turrets_repository;
        std::string mode;
        int iterator;

        /**
         * Resets the iterator used in the next_turret() function.
         */
        void reset_iterator();
    public:
        explicit Service(Repository *repository);

        explicit Service(Repository *repository, Repository *repository_saved);

        ~Service();

        /**
         * Returns the mode of the service
         */
        std::string get_mode();

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
        std::vector<Turret> get_turret_list();

        /**
         * Returns a list of turrets from the main_repository filtered by a certain size and minimum number of parts.
         * @param size: string representing the wanted size
         * @param parts: int representing the number of minimum parts
         * @return: A DynamicArray of turrets
         */
        std::vector<Turret> get_turret_list_by_size_by_parts(const std::string &size, int parts);

        /**
         * Returns the list of turrets from the saved_turrets_repository.
         */
        std::vector<Turret> get_saved_turrets_list();

        /**
         * Sets the file name of the main repository.
         */
        void set_file_name_main_repository(const std::string &file_name_to_set);

        /**
         * Sets the file name of the saved turrets repository.
         */
        void set_file_name_saved_turrets_repository(const std::string &file_name_to_set);

        /**
         * Returns the file name of the saved turrets repository.
         */
        std::string get_file_name_saved_turrets_repository();
};


#endif //IMRE5_SERVICE_H
