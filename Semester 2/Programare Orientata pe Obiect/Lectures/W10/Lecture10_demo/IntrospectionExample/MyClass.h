#pragma once
#include <QObject>

class MyClass : public QObject
{
	Q_OBJECT
public:
	Q_INVOKABLE MyClass(QObject *parent = 0) {}

public:
	Q_INVOKABLE void myMethod(int x) {}
};
