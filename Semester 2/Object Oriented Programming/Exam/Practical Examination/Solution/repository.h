#pragma once

#include <vector>
#include <string>
#include <fstream>
#include <algorithm>

template<typename T>
class Repository
{
private:
	std::vector<T> v;
	std::string file;

public:
	Repository(std::string _file) {
		file = _file;
		std::ifstream fin(this->file);
		T t;
		while (fin >> t)
			this->v.push_back(t);
		fin.close();
	}
	void save() {
		auto copy = v;
		sort(copy.begin(), copy.end(), [this](T t1, T t2) {
			return t1.constellation < t2.constellation;
		});
		std::ofstream fout(this->file);
		for (auto x : copy)
			fout << x.to_string() << "\n";
		fout.close();
	}
	void add(T obj) {
		v.push_back(obj);
	}
	bool remove(std::string name) {
		for (int i = 0; i < v.size(); ++i)
			if (v[i].name == name) {
				v.erase(v.begin() + i);
				return true;
			}
		return false;
	}
	bool update(T old_obj, T new_obj) {
		for (int i = 0; i < v.size(); ++i)
			if (v[i].name == old_obj.name) {
				v[i] = new_obj;
				return true;
			}
		return false;
	}
	std::vector<T> get_list() {
		return v;
	}
};
