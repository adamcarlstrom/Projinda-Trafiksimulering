# ProjInda
--- 
Projekt för ProjInda24
### Projekt Beskrivning
---
Det här projektet handlar om att skapa en trafiksimulering för en korsning. Här finns det vägar från 4 olika håll med en fil i varje riktning där bilar kan komma och åka till varje fil. Man ska kunna kontrollera variabler kring hur många bilar som kommer från vilket håll och analysera hur trafiken påverkas. Dessutom implementerade vi olika algoritmer för hur trafikljusen kopplade till korsningen skulle fungera så att vi kunde se hur deras logik kunde påverka trafiken under olika förhållanden.

Det här projektet visar simulationen grafiskt med hjälp av Java Swing. Detta hjälper med att rita ut bilar och skapa element man kan interagera med. För att köra projektet behöver man alltså se till att Java Swing fungerar på sitt system. Detta kan man göra genom att följa denna guide, Option 1 (https://phoenixnap.com/kb/install-java-ubuntu), då kan man köra projektet från terminalen i ubuntu. Det år annars även bra att köra projektet via vscode om man har de nödvändiga Java Extensions.

För att få simuleringen att kunna köra medans man vill ändra på saker har vi använt oss av Java Runnable. Denna import tillåter oss att kunna utföra olika funktioner parallellt med varandra. Alltså har det hjälpt med att se till att man kan köra simuleringen och ändra på variabler samtidigt.

### Trafiklogik
Vid kontrollpanelen finns radioknappar för vilken typ av logik som trafikljusen ska använda sig av. Här finns det tre alternativ och det sätts automatiskt till den första.
1. Den första logiken är ett slumpmässigt val av vilket trafikljus som ska visa grönt en gång i taget. Detta är väldigt ineffektivt och kan leda till att många bilar får vänta länge.
2. Den andra logiken handlar om att ha igång två trafikljus på motsatt håll igång samtidigt. Sedan alterneras det vilka två trafikljus som ska vara igång hela tiden. Detta fungerar mycket mer effektivt men kan vara sämre ifall all trafik kommer från ett håll då köer kan bildas.
3. Den tredje logiken bygger på storleken på köerna i varje håll, därifrån sätts de två trafikljus med motsatta håll med fläst bilar vid säg igång. Detta ser till att stora köer alldrig bildas, men kan få enstaka bilar i en fil att vänta oädnligt. 

### Trafik situationer
Trafiksituationer finns för att demonstrera i vilka sammanhang som logikerna kan fungera bra. Ifall ingen logik är igångsatt så skickas bilar in slumpmässigt från olika håll. 
1. Första situationen är direkt kopplad till logik 2 då den visar en situation där logik 1 hade haft stora problem. Här skickas bilar in från alla håll jämt.
2. Andra situationen är mer kopplad till att visa en situation där logik 3 krävs istället för logik 2. Här kommer bilar främst ifrån öst och väst, detta leder till att man i princip endast vill att det ska vara grönt vid öst och väst och inte för norr och syd eftersom köer skapas annars.

### Arbetarna
---
Detta projekt skapades av:      <br>
Adam Carlström                  <br>
Arvid Willhemlsson              <br>

### Hur arbetet är uppdelat
---
För att göra arbetet mer effektivt har arbetet delats upp mellan oss. Här har Adam främst arbetat med de grafiska komponenterna för att se till att själva trafiksituationen visas på rätt sätt. Arvid har arbetat främst med trafiklogiken och hur trafikljusen ska fungera samt i vilka fall som bilar tillåts att få köra.