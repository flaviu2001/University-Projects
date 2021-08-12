#include "ModelWrapper.h"

void ModelWrapper::propagate()
{
	for (auto x : mp)
		x.second->propagate();
	for (auto x : mp2)
		x.second->propagate();
}
