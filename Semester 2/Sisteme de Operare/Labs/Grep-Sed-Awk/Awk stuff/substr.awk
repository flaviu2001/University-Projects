
substr($6, 23, 5) ~ /gr[0-9][0-9][0-9]/ {
	v[substr($6, 23, 5)]++
	#print substr($6, 23, 5)
}

END{
	for (ceva in v)
		print ceva, v[ceva]
}
