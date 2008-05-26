replace:
	find ./sources -name '*.java' -exec sh -c 'sed -n "s/$(FROM)/$(INTO)/g; \
		w .tmp" {} && mv -f .tmp {}' \;
stats:
	find . -name \*.java | xargs wc -l
