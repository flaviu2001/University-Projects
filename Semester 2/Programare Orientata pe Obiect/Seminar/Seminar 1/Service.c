#include "Service.h"
#include <stdlib.h>

Service* createService(Repository* repo)
{
	Service* service = (Service*)malloc(sizeof(Service));
	if (service == NULL)
		return NULL;
	service->repo = repo;
	return service;
}

void destroyService(Service* service)
{
	destroyRepository(service->repo); // responsibility? 
									// who is in charge of destroying it?
	free(service);
	//service = 0; // !!!
}

int addPlanetService(Service* service, char* name, char* type, double distance) {
	Planet p = createPlanet(name, type, distance);
	return addPlanet(service->repo, p);
}