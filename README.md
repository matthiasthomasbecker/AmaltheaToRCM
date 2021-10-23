# Amalthea To RCM and Back

The repository contains three parts.Besides the automated transformation from Amalthea models to RCM models, two case studies are provided as well as expert interviews that have been conducted to evaluate the industrial relevance of the work.

## Automated Transformation

The automated transformation is the realisation of the proposed mapping scheme from Amalthea to RCM.
The software is setup as <a href="https://gradle.org/">Gradle</a> project and can therefore be used as is (or imported as Gradle project in any Eclipse based IDE).

The implementation builds on the available infrastructure of the <a href="https://www.eclipse.org/app4mc/">App4CM</a> project to load, modify and save Amalthea models.

## Case Study

* <b>Breake-by-Wire (BBW):</b>
This case study comprises a BBW application consisting of 11 runnables and tasks as well as 10 labels used for communication.
* <b>WATERS 2017 Industrial Challenge</b>
This case study is the <a href="https://www.ecrts.org/archives/index652a.html?id=ecrts17">2017 WATERS Industrial Challenge</a>. The application consists of more than 1200 runnables that are mapped to 21 tasks, and 10000 labels used for communication.
The WATERS 2017 Amalthea model has been modified as follows:
  * Migrated to version 0.9.7
  * Added the unit "Hz" to the clock domain
  * Removed the feature "accessElement" from the model as it was no longer supported after model migration.
* <b>Democar</b>
This application is distributed with Amalthea and describes a simplified Engine Management System. The application consists of 43 runnables that are mapped to 3 tasks.
Communication is realised via 71 communication labels.
The Democar model has been modified as follows:
  * Created a mapping model that allocates the three tasks to the operating system, as well as the operating system to the processor.
  * Increased the system clock frequency to 600MHz as the initial system has a utilization > 1 using the provided WCETs.
## Expert Interviews

Results of the expert interviews that have been conducted to evaluate the industrial relevance of the work. The interviews were conducted as online survey and consist of 10 questions.
