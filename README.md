# Test, System integration & Development of large systems exam
Group - TurkeyCrew
- Tobias Linge Jensen - cph-tl233@cphbusiness.dk
- Mathias Brix Drejer - cph-md266@cphbusiness.dk
- Robert Pallesen - cph-rp154@cphbusiness.dk

MTOGO Video Presentation:
https://youtu.be/cWH5bsO42Qw

## Table of Contents
- [Folder Structure](#folder-structure)
- [Project Solution & Argumentation of Choices](#project-solution--argumentation-of-choices)
- [Database Diagram](#database-diagram)
- [Use cases](#use-cases)
- [Scripts to install](#scripts)

## Product
I dette projekt har vi udviklet en food delivery applikation. App'en kan håndtere brugere, kurere og restauranter. Det kan håndtere forskellige forespørgsler fra de respektive bruger-typer.
Projektet er udviklet i en microservice arkitektur, skrevet i Java Boot Spring. Vi har derudover brugt apache kafka som message-broker, til at kommunikere mellem de forskellige services.

I de diagrammer vi har inkluderet, har vi primært valgt at fokusere på order-servicen.
Grunden til det valg, er primært at order-servicen er hele grundlaget for vores applikation og er også den service der har mest kommunikation med andre services.

### Domæne model
![image](https://github.com/SirBobbert/mtogo/assets/76921857/9abfd4db-ad24-4873-830f-76d18d849b10)

### Use Case Diagram
![image](https://github.com/SirBobbert/mtogo/assets/76921857/a6be7cae-cc66-4da5-979d-1c5bc59fb20a)

### Sekvens Diagram
![image](https://github.com/SirBobbert/mtogo/assets/76921857/c8af88dc-0a12-4e84-9453-f2fefa96bc6f)

### Klasse Diagram
![image](https://github.com/SirBobbert/mtogo/assets/76921857/09c823b1-a426-4e4c-93cd-25301d94c955)

### Arkitektur Diagram
![image](https://github.com/SirBobbert/mtogo/assets/76921857/81f775b8-02e8-42a3-9dbe-c1b8b22c1e5d)

## Installs
OpenJDK v. 20
Apache Maven v. 3.9.5  
