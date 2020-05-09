#include "IntrospectionExample.h"
#include <QtWidgets/QApplication>
#include "MyClass.h"
#include <qmetaobject.h>
#include <QDebug>

int main(int argc, char *argv[])
{
	MyClass* obj = new MyClass{};

	qDebug() << "The methods for this class are: ";
	for (int i = 0; i < obj->metaObject()->methodCount(); i++)
	{
		QMetaMethod method = obj->metaObject()->method(i);
		qDebug() << method.methodSignature();
	}
}
