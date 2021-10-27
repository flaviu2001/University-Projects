#5. Write a script that extracts from all the C source files given as command line arguments the included libraries and saves them in a file.

#!/bin/bash

ans=""

for filename in $@; do
	if file $filename | grep -q "C source"; then
		ans="$ans"$(grep "#include" $filename)$'\n'
	fi
done

echo "$ans"
echo "$ans" | grep -v "^$" | sort | uniq > libs
