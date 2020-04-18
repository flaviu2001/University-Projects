//#include "DynamicVector.h"
//#include "Pair.h"
//#include <Windows.h>
//#include <string>
//#include <iostream>
//
//using namespace std;
//
//int main()
//{
//	system("color f4");
//
//	// DynamicVector
//	DynamicVector<std::string> vString;
//	vString.add("Hello,");
//	vString.add("you");
//	vString.add("amazing");
//	vString.add("students!");
//	vString[2] = "brilliant";
//	for (int i = 0; i < vString.getSize(); i++)
//		cout << vString[i] << " ";
//	cout << endl;
//
//	DynamicVector<int> vInt;
//	vInt.add(1);
//	vInt.add(2);
//	vInt.add(3);
//	vInt[1] = 20;
//	for (int i = 0; i < vInt.getSize(); i++)
//		cout << vInt[i] << " ";
//	cout << endl << endl;
//
//	// Pair
//	Pair<int, int> x(10, 2);
//	cout << "Pair of integers: (" << x.getFirst() << ", " << x.getSecond() << ")." << endl;
//	Pair<int, string> y(3, "Hello!");
//	cout << "Pair of integer and string: (" << y.getFirst() << ", " << y.getSecond() << ")." << endl;
//	Pair<string, DynamicVector<int>> z("Hello", vInt);
//	cout << "Pair of string and Dynamic Vector: "<< endl;
//	cout << "\t First component: " << z.getFirst() << endl;
//	cout << "\t Second component: " << endl;
//	for (int i = 0; i < vString.getSize(); i++)
//	{
//		if (i == 0)
//			cout << "\t\t" << vString[i] << " ";
//		else
//			cout << vString[i] << " ";
//	}
//	cout << endl;
//
//	system("pause");
//	return 0;
//}