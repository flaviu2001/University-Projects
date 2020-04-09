#pragma once
#include "Planet.h"

#define MAX_DIM 100

typedef Planet TElem;

typedef struct
{
	TElem elems[MAX_DIM];
	int length;
} Repository;

Repository* createRepository();
void destroyRepository(Repository* repo);

/*
Searches for a planet in the repository (by planet name).
Input: repo - pointer to Repository
	   p - Planet to search
Output: 1 - if the planet was found
        0 - otherwise
*/
int findPlanet(Repository* repo, TElem p);

/*
Adds a planet to the repository.
Input:  repo - pointer to Repository
		p - Planet
Output: 1 - if the planet was not added (it already exists)
		0 - otherwise
*/
int addPlanet(Repository* repo, TElem p);

void testRepository();