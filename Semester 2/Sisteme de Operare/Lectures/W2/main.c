#include <stdio.h>
#include <string.h>
#include <unistd.h>

void without_sys(){
	char s[64];
	int i;
	char* p;
	while(1){
		p = fgets(s, 64, stdin);
		if (p == NULL)
			break;
		for (i = 0; i < strlen(s); ++i)
			if(s[i] != '\n' && s[i] != ' ')
				s[i] = '*';
		fputs(s, stdout);
	}
}

void with_sys(){
	char s[64];
	int i;
	int k;
	while(1){
		k = read(0, s, 64);
		if (k <= 0)
			break;
		for (i = 0; i < k; ++i)
			if(s[i] != '\n' && s[i] != ' ')
				s[i] = '*';
		write(1, s, k);
	}
}

int main(){
	with_sys();
	return 0;
}
