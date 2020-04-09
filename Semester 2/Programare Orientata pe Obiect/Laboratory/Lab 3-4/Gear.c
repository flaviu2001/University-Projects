#include "Gear.h"
#include <stdlib.h>
#include <string.h>

Gear create_gear(int catalogue_number, char* state, char* type, int value)
{
    Gear gear;
    gear.catalogue_number = catalogue_number;
    gear.state = (char*)malloc((strlen(state)+1)*sizeof(char));
    strcpy(gear.state, state);
    gear.type = (char*)malloc((strlen(type)+1)*sizeof(char));
    strcpy(gear.type, type);
    gear.value = value;
    return gear;
}

int get_catalogue_number(Gear gear)
{
    return gear.catalogue_number;
}

char* get_state(Gear gear)
{
    return gear.state;
}

char* get_type(Gear gear)
{
    return gear.type;
}

int get_value(Gear gear)
{
    return gear.value;
}

void set_state(Gear *gear, char* new_state)
{
    free(gear->state);
    gear->state = (char*)malloc((strlen(new_state)+1)*sizeof(char));
    strcpy(gear->state, new_state);
}

void set_type(Gear *gear, char* new_type)
{
    free(gear->type);
    gear->type = (char*)malloc((strlen(new_type)+1)*sizeof(char));
    strcpy(gear->type, new_type);
}

void set_value(Gear *gear, int new_value)
{
    gear->value = new_value;
}

Gear gear_copy(Gear gear) {
    Gear copy;
    copy.catalogue_number = gear.catalogue_number;
    copy.value = gear.value;
    copy.state = (char*)malloc((strlen(gear.state)+1)*sizeof(char));
    strcpy(copy.state, gear.state);
    copy.type = (char*)malloc((strlen(gear.type)+1)*sizeof(char));
    strcpy(copy.type, gear.type);
    return copy;
}

void destroy_gear(Gear gear)
{
    free(gear.state);
    free(gear.type);
}
