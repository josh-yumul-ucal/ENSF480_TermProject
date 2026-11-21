SQLITE = sqlite-jdbc-3.53.4.0.jar
JAVAFX = javafx-sdk-21.0.13/lib

SOURCE = src
OUT = out/production/FlightReservationSystem

COURSE = ENSF480
GROUP = 1

all:
	# Compile the main application source file which will compile all supporting files
	javac -cp $(SQLITE):$(JAVAFX) -sourcepath $(SOURCE) -d $(OUT) --module-path $(JAVAFX) --add-modules javafx.controls $(SOURCE)/main/FlightReservationApp.java

run:	all
	java -cp $(SQLITE):$(JAVAFX):$(OUT) --module-path $(JAVAFX) --add-modules javafx.controls main.FlightReservationApp

zip:	all
	# Create zip files containing source and class files for submission
	cd $(SOURCE) ; zip -r ../$(COURSE)_G$(GROUP)-sources.zip . -i *.java ; cd ..
	cd $(OUT) ; zip -r ../../../$(COURSE)_G$(GROUP)-classes.zip . -i *.class ; cd ../../..

clean:
	# Clean by emoving source and class zip files and compiled .class files
	rm -f $(COURSE)_G$(GROUP)-sources.zip $(COURSE)_G$(GROUP)-classes.zip
	rm -rf $(OUT)/*/
