#include "ModelWrapper.h"

void ModelWrapper::propagate()
{
	for (auto x : m)
		x.second->propagate();
	for (auto x : m2)
		x.second->propagate();
	cg->build_chart();
}
