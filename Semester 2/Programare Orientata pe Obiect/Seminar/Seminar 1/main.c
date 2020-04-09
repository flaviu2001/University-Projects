#include "Planet.h"
#include "Repository.h"
#include <crtdbg.h>

int main()
{
	testPlanet();
	testRepository();

	_CrtDumpMemoryLeaks();

	return 0;
}