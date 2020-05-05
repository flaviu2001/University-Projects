#pragma once
#include <iostream>

using namespace std;

template <typename T>
class SmartPointerTemplate
{
private:
	T* data;

public:
	SmartPointerTemplate(T* ptr);
	~SmartPointerTemplate();

	// overload pointer operators
	T& operator*();
	T* operator->();

	// other necesary functions!
};

template <typename T>
SmartPointerTemplate<T>::SmartPointerTemplate(T* ptr) : data{ ptr }
{
	cout << "Resource was allocated.\n";
}

template <typename T>
SmartPointerTemplate<T>::~SmartPointerTemplate()
{
	delete this->data;
	cout << "Resource was deallocated.\n";
}

template <typename T>
T& SmartPointerTemplate<T>::operator*()
{
	return *this->data;
}

template <typename T>
T* SmartPointerTemplate<T>::operator->()
{
	return this->data;
}