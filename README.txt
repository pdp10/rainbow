Rainbow - A simulator of processes and resources in a multitasking computer.
Copyright (C) 2006 - 2015 
E-mail: piero.dallepezze@gmail.com

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.



================================================================

Software                      : Rainbow
Version			      : 3.2.12
Current designer/programmers   : Piero Dalle Pezze (Rainbow)
Previous designer/programmers : Stefano Bertolin (SGPEMv2), 
				Piero Dalle Pezze (SGPEMv2),
				Michele Perin (SGPEMv2),
				Pier Giorgio Marin (SGPEMv2), 
				Carlo Sarto (SGPEMv2),
				Fabio Gallonetto (SGPEMv1).
Date			      : 16 March 2015
E-Mail		              : piero.dallepezze@gmail.com


================================================================

BRIEF HISTORY

The original idea of this software was conceived in 2005 at the University of Padua, 
Italy, during the course of "Software Engineering" as a support to the course of 
"Operating Systems". The project's name was SGPEMv1 (in Italian, "Simulatore 
di Gestione dei Processi in un Elaboratore Multiprogrammato", translated: simulator of 
the management of processes in a multitasking computer). One year later, due to faults 
and conceptual inconsistencies the same project was purposed again and was called SGPEMv2. 
The SGPEMv2 project was a re-engineering of SGPEMv1. It reuses the code of the main GUI 
(by Fabio Gallonetto) with some adaptions, but the engine was completely designed and 
implemented again. Rainbow is the natural second version of SGPEMv2, but due to its history, 
it is the third version. Rainbow is a personal initiative, outside the University of Padua.


================================================================

MINIMAL REQUIREMENTS

Software requirements: JVM 1.6, 
                       Infonode GUI libraries 1.6.1 (included, GPL license),
                       JGoodies GUI libraries 2.3.1 (included, BSD license),
		       NimRod GUI libraries 1.2 (included, LGPL license),
		       Swing-layout GUI libraries 1.0.3 (included, LGPL license),
		       Javahelp 2.0 (included, GPL license, manually compiled).

Hardware requirements: Processor: 800 MHz, RAM: 128 MB.


================================================================

CHANGING

The aim of Rainbow is to be an useful, powerful and stable software 
for all people that want to learn, understand and study how a 
conceptual operating systems works, in particular from the point of 
view of the scheduler of processes. 
The main changes are reported below:
 
Iced-features
- Fix bug in Ant build.xml to create distribution
- Link MANIFEST.MF to build.xml
- Add junit tests
- Simulation of a multi-core system
- Simulation of Linux / Unix system
- Adding a view for comparing the different Scheduling policies
- Generalise this software for simulating business processes and resources (and not just computer processes and resources)
- other ideas? :)

Trunk
 - Re-packaging of classes using org.rainbow
 - Use of Ant instead of Make.
 - Use of Git for version control.

3.2.14
 - Moved the source code to github 
3.2.12
 - Bug correction. Added minimal support for future implementation of multi-core process simulation.
3.2.10
 - Modularisation of the GUI. Separated Toolbar, Menubar, and Views from RainbowMainGUI. This improves 
 readability and code extension.
3.2.8
 - Simplified the multilanguage management by integrating the language configuration in 1 place. 
3.2.6
 - Removed automated advancement and replaced with a multi-step mechanism of advancement
3.2.4 
 - Redisign of the Scheduler package in order to enable the development 
 of alternative Schedulers. This aims to generalise this Simulator to a 
 scenary outside Operating Systems.
Before v3.2: 
 - Support of internationalization;
 - New design and implementation of the modules:
   Scheduler, SimulatedProcess, AssignmentPolicy, Resource, DataInput;
 - Support of the mechanism ICPP (Immediate Ceiling Priority Protocol);
 - Modular and easy configuration of the Rainbow system;
 - New scheduling policies: 
        Multilevel Feedback, Preemptive Multilevel Feedback,
        Multilevel Feedback with Dynamic Quantum,
	Preemptive Multilevel Feedback with Dynamic quantum.
 - New assignment policy :
        Highest Priority First
 - Export the configuration in html files;
 - Maximum layout;
 - English translation of all the source code, Help and About menu;
 - GNU license v.2;
 - Java 1.6 compatible;
 - New icons Nuvola;
 - Improvement of the Look 'n' Feel;
 - Added themes menu and status bar;
 - Makefile generated by mmake;
 - XML configuration files.

================================================================

COMPILATION

You need JDK 1.6 (see http://java.sun.com/) and GNU/Make.
Enter in Rainbow/src.

- GNU/Linux
-----------

You can simply type:    make; make jar 

It’s also possible to run make with one of the following targets: 
- 'make doc': runs javadoc on the source files. 
- 'make clean': removes class files and other temporary files. 
- 'make jar': creates a jar file with all class files. 
- 'make srcjar' creates a jar file with all java files. 
- 'make bundle' creates a Mac OS X Application Bundle with all the jar file.
- 'make install' will install a jar file, app bundle, class files and any shell wrappers.
- 'make uninstall' to remove installed files. 
- 'make help', shows a help text with available targets. 
- 'make tags' will generate a tag file for Emacs. 
- 'make depend' creates a dependency graph for the class files. (The dependency graph 
   will be put in a file called makefile.dep, which is included in the Makefile)

- Windows
---------

Click or run compile-on-win.bat

================================================================

EXECUTE

You must enter inside the Rainbow folder. Then execute:

Under GNU/Linux: ./rainbow.sh
Under Windows  : rainbow.bat


================================================================

ABOUT

The name was changed because SGPEM was an Italian acronym.
Rainbow was suggested by the colors of running processes 
in the scheduling function, which is represented in a graphical 
view of the software.
The initial splash image remembers "the storm before the rainbow".



