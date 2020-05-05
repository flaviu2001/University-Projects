#pragma once

class SmartPointer
{
private:
	int* data;

public:
	SmartPointer(int* ptr);
	~SmartPointer();

	// overload pointer operator
	int& operator*();

	// other necesary functions!
};

