#include "SmartPointer.h"
#include <iostream>

using namespace std;

SmartPointer::SmartPointer(int* ptr): data(ptr)
{
	cout << "Resource was allocated.\n";
}

SmartPointer::~SmartPointer()
{
	delete this->data;
	cout << "Resource was deallocated.\n";
}

int& SmartPointer::operator*()
{
	return *this->data;
}
