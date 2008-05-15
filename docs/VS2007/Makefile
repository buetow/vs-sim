all:
	rubber -d verteilte-systeme.tex
touch:
	touch verteilte-systeme.tex
	rubber -d verteilte-systeme.tex
pdf: all
	xpdf verteilte-systeme.pdf	
clean:
	rm -f *.{pdf,aux,log,tec}
