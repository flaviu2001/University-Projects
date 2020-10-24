# Find recursively in a directory all ".c" files having more than 500 lines. Stop after finding 2 such files.

for x in $(find *); do
    if echo "$x" | grep -q "\.c$"; then
	if [ $(wc -l $x | cut -d' ' -f1) -gt 500 ]; then
	    times=$((times + 1))
	    if [ $times -gt 2 ]; then
		exit 0
	    fi
	    echo $x
	fi
    fi
done
