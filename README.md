
# Rainbow (GNU GPL v2)


### Introduction
The original idea of this software was conceived in 2005 at the University of Padua (Italy) in the course of "Software Engineering", as a supporting tool for the course of *Operating Systems*. The project's name was SGPEMv1 (2005) (in Italian, *Simulatore di Gestione dei Processi in un Elaboratore Multiprogrammato*, translated: *A simulator for computer processes and resources*). One year later, due to faults 
and conceptual inconsistencies the same project was purposed again and was called SGPEMv2. The SGPEMv2 (2006) project was a re-engineering of SGPEMv1. It reused the code of the main GUI (by Fabio Gallonetto) with some edits, but the engine was completely re-designed. Rainbow is the natural second version of SGPEMv2, but due to its history, it is the third version. Rainbow (2006-now) is a personal initiative, unrelated with the University of Padua.


### Minimal requirements
Software requirements: 
- JVM 1.6, 
- Infonode GUI libraries 1.6.1 (included, GPL license),
- JGoodies GUI libraries 2.3.1 (included, BSD license),
- NimRod GUI libraries 1.2 (included, LGPL license),
- Swing-layout GUI libraries 1.0.3 (included, LGPL license),
- Javahelp 2.0 (included, GPL license, manually compiled).

Hardware requirements: 
- Processor: 800 MHz, 
- RAM: 128 MB.


### Compilation
You need JDK 1.6 (see http://java.sun.com/) and Ant. Enter in Rainbow/src.

To compile the source code, type:
``` 
ant jar 
```

To execute Rainbow, type:
```
# On GNU/Linux: 
./rainbow.sh
```
```
# On Windows:
rainbow.bat
```

### Developing
If you wish to develop new features, you can fork the project and then send a pull request. Everyone is welcome, particularly students keen to learn how the core of an operating system works.


### About
The name was changed because SGPEM was an Italian acronym. Rainbow was suggested by the colours of 
running processes in the scheduling function, which is represented in a graphical view of the software.
The initial splash image remembers *the storm before the rainbow*.

