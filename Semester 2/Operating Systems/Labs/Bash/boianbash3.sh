# Find recursively in a directory, all the files with the extension ".log" and sort their lines (replace the original file with the sorted content).

if [ $# -ne 1 ]; then
    echo YU STOOPID
    exit 1
fi

if [ ! -d "$1" ]; then
    echo YU DOUBLE STUPYD
    exit 1
fi

bkp=$IFS
IFS=$'\n'
for x in $(find "$1"); do
    if [ -f "$x" ] && echo "$x" | grep -q ".log$"; then
	ans=$(sort "$x" | sed "s/\0//g")
	printf "$ans" > "$x"
    fi
done
IFS=$bkp
