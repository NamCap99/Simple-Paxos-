JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  Acceptor.java \
		  AcceptorHandler.java \
		  Constants.java \
		  Councillor.java \
		  GenerateId.java \
		  Main.java \
		  Proposer.java \
		  ProposerHandler.java \
		  ReceiveMessage.java \
		  Request.java \
		  Response.java \
		  SendMessage.java \
		  Status.java \
		  Type.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
	
run:
	java Main
