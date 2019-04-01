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

JARS = ./libs/AIMA.jar:./libs/Comparticion.jar:./libs/commons-cli-1.4.jar

default: bin classes

bin:
	mkdir -p $(BIN)

classes: $(CLASSES:.java=.class)

S = 1234
N = 20
M = 10
A = HC
I = 3

run:
	$(JVM) -cp $(RCPATHS) Main -s $(S) -n $(N) -m $(M) -a $(A) -i $(I)


clean:
	$(RM) -rf $(BIN)*
