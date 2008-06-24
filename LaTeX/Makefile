FILENAME=diplomarbeit
all:
	svn info | grep 'Changed Rev' | awk '{ print $$4 }' > VERSION
	rubber -d $(FILENAME).tex 
clean:
	ls $(FILENAME).* | \
		grep -v .tcp | \
		grep -v .tex | \
		xargs rm 
	rm -f images/*.pdf
spell:
	find ./chapters -name \*.tex -exec sh -c 'aspell -l de_DE -t -c {}' \;
