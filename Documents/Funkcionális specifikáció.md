
# Funkcionális Specifikáció 
## 1.) Áttekintés
Az alkalmazás célja, helymeghatározás beltéren. Az alkalmazás rendelkezik Android felülettel, ahol elérhetőek a felhasználói funkciók. A mért adatok egy grafikon formájában is megtekinthetőek. A helymeghatározás alapja a WIFI jelerősség, mely alapján meghatározza az applikáció a felhasználó pontos tartózkodási helyét.

## 2.) Jelenlegi helyzet
Ennek alkalmazás célja a beltéri helymeghatározás. Biztosítja azt, hogy beltéren megtudjuk határozni a telefonunk valós helyzetét különböző szenzorok fúziójával. Az alkalmazás Android felületen fog futni. Az adatokat és azoknak fúzióját egy Python alkalmazás segítségével fogjuk grafikonokon megjeleníteni.

## 3.) Követelménylista
- Mérések: A felhasználó tudjon méréseket végezni az Android applikációval.
- Vizualizálás: A felhasználó a mért adatokat meg tudja jeleníteni grafikonokon egy Python alkalmazás segítségével.
- Automatikus adatátvitel: A felhasználónak könnyen kell tudnia exportálni a mért adatokat az Android applikációból és importálni ezeket a Python alkalmazásba.
  
## 4.) Jelenlegi üzleti folyamatok modellje

Üzleti szereplők: 	
-	felhasználó

Üzleti munkatárs: 	
-	karbantartó

A mai világban szinte mindenkinek van mobiltelefonja, ezáltal rengeteg dologra használhatjuk, anélkül, hogy különböző eszközöket vásárolnánk. A mobiltelefonokra például lehetne egy olyan alkalmazás, amely képes meghatározni a felhasználó helyzetét egy épületen belül. A GPS elterjedt a mai világban, viszont beltéri pontos helyzet meghatározására is szükség lehet.

## 5.) Igényelt üzleti folyamatok
Néhány gombnyomással meghatározható a felhasználó beltéri helyzete. Mérés kezdete gomb: Megnyomástól számítva tárolja a szenzorok értékeit. Mérés vége gomb: Megnyomásig tárolja a szenzorok értékeit.

## 6.) Használati esetek

## 7.) Megfeleltetés, hogyan fedik le a használati esetek a követelményeket
A felhasználó adatai biztonságosan van eltárolva, úgy hogy csak a rendszergazda és a felhasználó férjen csak hozzá. Az érzékelők által begyűjtött adatok szintén ilyen módszer biztonságos helyen lesznek eltárolva. Ezeket a felhasználó szükség esetén le bírja kérdezni bármikor. 
A felület egyszerű és felhasználóbarát, így könnyen lehet majd kezelni és egyben látványos lesz. 

## 8.) Képernyőtervek

### Android App:
 - Main Activity: 
 
 ![MainActivity](https://raw.githubusercontent.com/kissolga0904/InsertNameHere_AFP2/main/Documents/Screen%20Plans/AndroidApp/MainActivity.png) 
 - Bias Calibration Activity:
 
 ![BiasCalibrationActivity](https://raw.githubusercontent.com/kissolga0904/InsertNameHere_AFP2/main/Documents/Screen%20Plans/AndroidApp/BiasCalibrationActivity.png)
 - Data Gatherer Activity:
 
 ![DataGathererActivity](https://raw.githubusercontent.com/kissolga0904/InsertNameHere_AFP2/main/Documents/Screen%20Plans/AndroidApp/DataGathererActivity.png)
 - Graph Selector Activity:
 
 ![GraphSelectorActivity](https://raw.githubusercontent.com/kissolga0904/InsertNameHere_AFP2/main/Documents/Screen%20Plans/AndroidApp/GraphSelectorActivity.png)
 - Graph Activity:
 
 ![GraphActivity](https://raw.githubusercontent.com/kissolga0904/InsertNameHere_AFP2/main/Documents/Screen%20Plans/AndroidApp/GraphActivity.png)

## 9.) Funkció-követelmény megfeleltetés
### Funkcionális követelmények:
- Bővíthetőség: A kezelt adatok mennyiségének, valamint a felhasználók számának bővíthetősége és utólag újabb funkciók hozzáadásának lehetőségének biztosítása.

### Nem Funkcionális követelmények:
- Egyszerű, pontosan értelmezhető, letisztult, felhasználóbarát felület
- Egyszerűen használható kezelőfelület
- Felhasználók adatainak biztonsága

## 10.) Forgatókönyv
A mobil alkalmazásunk különböző telefonon megtalálható érzékelők segítségével mérések alapján begyűjtött adatokat és azokat egy grafikonon megjeleníteni. Az adatokat eltároljuk, hogy később azokat feltudjuk használni. 
## 11.) Fogalomszótár
- **GPS:** (GPS, Global Positioning System, Globális Helymeghatározó Rendszer) Az Amerikai Egyesült Államok Védelmi Minisztériuma (Department of Defense) által (elsődlegesen katonai célokra) kifejlesztett és üzemeltetett – a Föld bármely pontján, a nap 24 órájában működő – globális műholdas navigációs rendszer (GNSS).
 - **Érzékelő:** Az érzékelő vagy szenzor olyan elem, amely egy mérendő tulajdonságtól függő jelet szolgáltat. A mérendő tulajdonság és a jel egyaránt lehet fizikai, kémiai, biológiai stb. jellegű. Fontos, hogy a mérendő tulajdonság, és az érzékelő által szolgáltatott jel egymásnak kölcsönösen egyértelmű függvényei legyenek.
 - **Android:** Az Android Linux kernelt használó mobil operációs rendszer, elsősorban érintőképernyős mobil eszközökre (okostelefon, táblagép) tervezve. Fejlesztését az Android, Inc. kezdte el, amelyet 2005-ben a Google felvásárolt, majd az Open Handset Alliance folytatta. A fejlesztők Java nyelven írhatnak rá menedzselt kódot, az eszközt a Google által fejlesztett Java programkönyvtárakon keresztül vezérelve. Az első androidos telefon 2008 októberében jelent meg.
 - **Python:** A Python egy általános célú, nagyon magas szintű programozási nyelv, melyet Guido van Rossum holland programozó kezdett el fejleszteni 1989 végén, majd hozott nyilvánosságra 1991-ben. A nyelv tervezési filozófiája az olvashatóságot és a programozói munka megkönnyítését helyezi előtérbe a futási sebességgel szemben.
 - **Node.JS:** A Node.js egy szoftverrendszer, melyet skálázható internetes alkalmazások, mégpedig webszerverek készítésére hoztak létre.
 - **WebSocket:** A WebSocket internetes technológia, ami kétirányú, duplex kommunikációs csatornák kiépítését teszi lehetővé egyetlen TCP protokollon keresztül. Bár a fő cél a webböngészőkben és webszervereken való implementáció, más kliens-szerver megoldásokban is használható.
