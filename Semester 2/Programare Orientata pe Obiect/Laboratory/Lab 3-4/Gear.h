#ifndef IMRE_3_GEAR_H
#define IMRE_3_GEAR_H

typedef struct{
    int catalogue_number;
    char *state, *type;
    int value;
} Gear;

Gear create_gear(int catalogue_number, char* state, char* type, int value);
int get_catalogue_number(Gear gear);
char* get_state(Gear gear);
char* get_type(Gear gear);
int get_value(Gear gear);
void set_state(Gear *gear, char* new_state);
void set_type(Gear *gear, char* new_type);
void set_value(Gear *gear, int new_value);
Gear gear_copy(Gear gear);
void destroy_gear(Gear gear);

#endif //IMRE_3_GEAR_H
