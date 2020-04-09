#include "Repository.h"
#include <stdlib.h>
#include <string.h>
#include <assert.h>

Repository* createRepository() 
{
	Repository* repo = (Repository*)malloc(sizeof(Repository));
	if (repo == NULL)
		return NULL;
	repo->length = 0;
	return repo;
}

void destroyRepository(Repository* repo) 
{
	free(repo);
}

int findPlanet(Repository* repo, TElem p)
{
	for (int i = 0; i < repo->length; i++)
		if (strcmp(getName(&repo->elems[i]), getName(&p)) == 0)
			return 1;
	return 0;
}

int addPlanet(Repository* repo, TElem p)
{
	if (findPlanet(repo, p))
		return 1;
	repo->elems[repo->length++] = p;
	return 0;
}

void testRepository()
{
	Repository* repo = createRepository();
	Planet p = createPlanet("Earth", "Terrestrial", 0);
	assert(addPlanet(repo, p) == 0);
	assert(addPlanet(repo, p) == 1);

	destroyRepository(repo);
}