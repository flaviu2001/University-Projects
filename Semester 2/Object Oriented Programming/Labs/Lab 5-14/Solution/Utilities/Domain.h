//
// Created by jack on 3/21/20.
//

#ifndef IMRE5_DOMAIN_H
#define IMRE5_DOMAIN_H

#include <string>

class Turret {
    private:
        std::string location;
        std::string size;
        int aura_level{}, separate_parts{};
        std::string vision;
    public:
        Turret();
        Turret(const std::string &location, const std::string &size, const int &aura_level, const int &separate_parts, const std::string &vision);
        Turret(const std::string &location, const std::string &size, const std::string &aura_level, const std::string &separate_parts, const std::string &vision);
        [[nodiscard]] std::string get_location()const;
        [[nodiscard]] std::string get_size()const;
        [[nodiscard]] int get_aura_level()const;
        [[nodiscard]] int get_separate_parts()const;
        [[nodiscard]] std::string get_vision()const;
        void set_size(const std::string &new_size);
        void set_aura_level(const int &new_aura_level);
        void set_separate_parts(const int &new_separate_parts);
        void set_vision(const std::string &new_vision);
        Turret& operator=(const Turret &new_turret);
        bool operator==(const Turret &other_turret)const;
        friend std::istream& operator>>(std::istream& reader, Turret &turret);
        friend std::ostream& operator<<(std::ostream& writer, const Turret &turret);
};

#endif //IMRE5_DOMAIN_H
