NAME=vortrag
all: folien 
folien:
	rubber -d $(NAME).tex
touch:
	touch $(NAME).tex
	rubber -d $(NAME).tex
pdf: all
	xpdf $(NAME).pdf	
clean:
	for i in toc out aux log tec nav snm; do rm -f $(NAME).$$i; done
	rm -f client-server.pdf vererbungen.pdf
gfx:
	for i in `ls *.eps`; \
		do epstopdf $$i; \
		done
