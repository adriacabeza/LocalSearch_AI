JFLAGS = -g
JC = javac
#JC = /usr/java/jdk1.8.0_45/bin/javac
JVM= java

BIN = ./exe/

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java -cp $(CCPATHS) -encoding iso-8859-1 -d $(BIN)

CCPATHS = ./src:$(JARS)
RCPATHS = $(BIN):$(JARS)

CLASSES = \
	src/*.java

JARS = ./libs/AIMA.jar:./libs/Comparticion.jar

default: bin classes

bin:
	mkdir -p $(BIN)

classes: $(CLASSES:.java=.class)

run:
	$(JVM) -cp $(RCPATHS) Main


clean:
	$(RM) -rf $(BIN)*
