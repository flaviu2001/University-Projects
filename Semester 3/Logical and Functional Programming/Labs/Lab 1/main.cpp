#include <bits/stdc++.h>
#include "lista.h"

using namespace std;

int gcd(int a, int b)
{
	if (b == 0)
		return a;
	return gcd(b, a%b);
}

int gcd_list(Lista l)
{
	if (l._prim == nullptr)
		return 0;
	return gcd(l._prim->e, gcd_list(creare(l._prim->urm)));
}

Lista add_rec(Lista l, int pos_to_add, int val, int curr_pos, Lista init_list)
{
	if (pos_to_add == 0) {
		Lista l2;
		l2._prim = new Nod();
		l2._prim->e = val;
		l2._prim->urm = l._prim;
		return l2;
	}else if (curr_pos+1 < pos_to_add) {
		return add_rec(creare(l._prim->urm), pos_to_add, val, curr_pos+1, init_list);
	}else {
		PNod l3 = l._prim->urm;
		PNod new_node = new Nod();
		new_node->e = val;
		new_node->urm = l3;
		l._prim->urm = new_node;
		return init_list;
	}
}

Lista add(Lista &l, int pos_to_add, int val)
{
	return add_rec(l, pos_to_add, val, 0, l);
}

int main() {
	Lista l;
	l = creare();
	cout << "GCD is " << gcd_list(l) << "\n";
	int x, n;
	cout << "x=";
	cin >> x;
	cout << "n=";
	cin >> n;
	l = add(l, n, x);
	tipar(l);
	distruge(l);
	return 0;
} 
