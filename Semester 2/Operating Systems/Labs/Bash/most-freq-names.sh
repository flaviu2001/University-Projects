#!/bin/bash

place="/mnt/Main Stuff/Uni Stuff/Semester 2/Sisteme de Operare/Laboratory/Lab 2&3/Useful files/passwd"

awk -F: '{print $5}' "$place" | \
cut -d ' ' -f2- | \
sed "s/[ -]/\n/g" | \
tr '[A-Z]' '[a-z]' | \
grep -v "\.\|^.$" | \
sort | \
uniq -c | \
sort -n | \
cat
