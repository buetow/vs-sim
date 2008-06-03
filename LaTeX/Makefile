FILENAME=diplomarbeit
all:
	rubber -d $(FILENAME).tex
clean:
	ls $(FILENAME).* | \
		grep -v .tcp | \
		grep -v .tex | \
		xargs rm 
