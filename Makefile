code-generator:
	@echo "***Installing Code Generator***"
	cd cg/CodeGenerator/CodeGenerator && mvn clean install
appwish:code-generator
	@echo "***Installing App Wish***"
	cd AppWish/AppWish && mvn clean install
run:appwish
	@echo "*** Starting App Wish ***"
	cd AppWish/AppWish && mvn javafx:run
