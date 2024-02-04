# Appwish Ollama's Make Script

CODE_GENERATOR_PATH:=cg/CodeGenerator/CodeGenerator
APPWISH_OLLAMA_PATH:=AppWish/AppWish

# Install and Start operations

code-generator:
	@echo "***Installing Code Generator***"
	cd $(CODE_GENERATOR_PATH) && mvn clean install
appwish:code-generator
	@echo "***Installing App Wish***"
	cd $(APPWISH_OLLAMA_PATH) && mvn clean install
run:appwish
	@echo "*** Starting AppWish Ollama ***"
	cd $(APPWISH_OLLAMA_PATH) && mvn javafx:run

# Clean project operations

clean-code-generator:
	@echo "*** Cleaning Code Generator***"
	cd $(CODE_GENERATOR_PATH) && mvn clean
clean-appwish:clean-code-generator
	@echo "*** Cleaning AppWish ***"
	cd $(APPWISH_OLLAMA_PATH) && mvn clean
clean-project:clean-appwish
	@echo "*** Cleaned the Appwish Ollama Project  ***"

# Artifact operations

package-code-generator:
	@echo "*** Creating an Artifcat for code-generator***"
	cd $(CODE_GENERATOR_PATH) && mvn clean package
package-appwish:package-code-generator
	@echo "*** Creating an AppWishOllama Artifact***"
	cd $(APPWISH_OLLAMA_PATH) && mvn clean package
package-project:package-appwish
	@echo "*** Created Artifacts for the Appwish Ollama Project ***"
