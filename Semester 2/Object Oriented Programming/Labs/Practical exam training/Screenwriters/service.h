//
// Created by jack on 6/13/20.
//

#ifndef SCREENWRITERS_SERVICE_H
#define SCREENWRITERS_SERVICE_H


#include <vector>
#include "domain.h"

class Service {
private:
    std::vector<Screenwriter> writers;
    std::vector<Idea> ideas;
public:
    Service(const std::string& writer_file, const std::string& idea_file);
    std::vector<Screenwriter> get_writers();
    std::vector<Idea>& get_ideas();
    /**
     * this function adds an idea to the list of ideas.
     * @param idea: Idea
     */
    void add_idea(const Idea& idea);
    /**
     * This function updates the status field of the idea object
     * @param idea: Idea
     * @param new_status: string
     */
    static void update_idea(Idea& idea, std::string new_status);
};


#endif //SCREENWRITERS_SERVICE_H
