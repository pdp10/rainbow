
# Rainbow

[![Build Status](https://travis-ci.org/pdp10/rainbow.svg?branch=master)](https://travis-ci.org/pdp10/rainbow)

[![License: GPL v2](https://img.shields.io/badge/License-GPL%20v2-blue.svg)](https://img.shields.io/badge/License-GPL%20v2-blue.svg)

Rainbow is an open source software simulator of an abstract operating system. It allows users to simulate several scheduling and assignment policies of computer processes and resources, and visualise results as plots and statistics. It is available in several languages. This software is release under the GNU General Public License v2.


### Introduction
The original idea of this software was conceived in 2005 at the Department of Pure and Applied Mathematics, University of Padua (Italy) in the course of "Software Engineering". It was meant to be a supporting tool for the course of *Operating Systems*. The project name was SGPEMv1 (2005) (in Italian, *Simulatore di Gestione dei Processi in un Elaboratore Multiprogrammato*. One year later, the same project was purposed again in order to improve its design, and was called SGPEMv2. The SGPEMv2 (2006) project was a re-engineering of SGPEMv1. It reused the code of the main GUI (by Fabio Gallonetto) with a few edits, but the engine was completely re-designed. Rainbow is the natural second version of SGPEMv2, but for versioning reasons, it is the third version. Rainbow (2006-now) is a personal initiative, unrelated with the University of Padova.


### Minimal requirements
Software requirements: 
- JVM 1.7,
- Infonode GUI libraries 1.6.1 (included, GPL license),
- JGoodies GUI libraries 2.3.1 (included, BSD license),
- Swing-layout GUI libraries 1.0.3 (included, LGPL license).

Hardware requirements: 
- Processor: 800 MHz, 
- RAM: 128 MB.


### Compilation / Execution
To compile the source code, you need JDK 1.7 and Maven. 

To compile the source code, type:
``` 
mvn package 
```

To execute Rainbow, type:
```
# On GNU/Linux: 
./rainbow.sh
# On Windows:
rainbow.bat
```

### Developing
If you wish to develop new features, you can fork the project and then send a pull request. Everyone is welcome, particularly students keen to learn how the core of an operating system works. For details, please see `doc/developer_manual/developer_manual.md`.


### About
The name was changed because SGPEM was an Italian acronym. Rainbow was suggested by the colours of running processes in the scheduling function.

