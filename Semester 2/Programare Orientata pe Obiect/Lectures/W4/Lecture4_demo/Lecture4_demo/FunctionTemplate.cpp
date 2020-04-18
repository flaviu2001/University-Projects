#include <Windows.h>
#include <iostream>
#include <string>

using namespace std;

template <typename T> 
T add(T a, T b)
{
	return a + b;
}

class Vector2D
{
private:
	double xCoordinate;
	double yCoordinate;

public:
	Vector2D(double xCoord = 0, double yCoord = 0) : xCoordinate(xCoord), yCoordinate(yCoord) {}
	double getXCoordinate() const { return xCoordinate; }
	double getYCoordinate() const { return yCoordinate; }
	Vector2D operator+(const Vector2D& a) { return Vector2D{ xCoordinate + a.xCoordinate, yCoordinate + a.yCoordinate }; }
};

int main()
{
	system("color f4");

	int resInt = add<int>(3, 4);
	cout << "Result for sum of integeres 3 and 4 is: " << resInt << endl;

	double resDouble = add<double>(-1.5, 2.6);
	cout << "Result for sum of doubles -1.2 and 3.6 is: " << resDouble << endl;

	string resString = add<string>("Hello ", "world!");
	cout << "Result for sum of strings \"Hello \" and \"world!\" is: " << resString << endl;

	Vector2D resVector2D = add<Vector2D>(Vector2D{ 1, 2 }, Vector2D{ -1, 2 });
	cout << "Result for sum of 2D vectors (1, 2) and (-1, 2) is: (" << resVector2D.getXCoordinate() <<", "<<resVector2D.getYCoordinate()<<")"<< endl;

	system("pause");

	return 0;
}