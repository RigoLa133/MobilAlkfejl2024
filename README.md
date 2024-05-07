# Mobil Alkalmazásfejlesztés Projekt
## 2023/24 Tavaszi félév
### Projekt Téma: Szőnyeg Webshop

#### Sidenote

Volt egy olyan problémám a projekt készítése közben, hogy a főmenün a szőnyegek megfelelő képei helyett a drawable mappában lévő ikonoktól kezdve minden féle képet jelenített meg. Ezt leszámítva minden funkció működik csak teljesen elrondítja. Ez elvileg emulátor cache probléma, de ha esetleg peer review közben is jelentkezik, sajnos nem tudok mit csinálni vele, így is mire helyrehoztam/rájöttem több napomba telt.

#### Követelmények

Fordítási hiba nincs - [x]

Futási hiba nincs - [x]

Firebase authentication
- Regisztráció - [x]
- Bejelentkezés - [x]

Adatmodell definiálás - [x]
- models package-ben

Legalább 3 activity/fragmens
- Van sok - [x]

Beviteli mezők megfelelően
- Mindenhol megvan, hacsak valamit nem vettem észre.

ConstraintLayout + másik - [x]

Reszponzív
- Nemtudom mi számít max pontosnak, én nem adtam hozzá semmi extrát, Pixel 7 Pro emulátoron dolgoztam végig, ott ahogy működik és kinéz ugyanúgy működik és néz ki kisebb      kijelzős emulátoron és elforgatva is (kivéve ha valamit nem vettem észre).

Legalább 2 animáció
- MainActivity-n ha a kosárba gomb kattintásra változik
- MainActivity-n a kosár ikonja elfordul ha belerakunk valamit a kosárba

Intentek - [x]

Lifecycle hook
-  MainActivity-ben (79. sor) van egy onRestart, ami beállítja a kosár ikonjánál a piros karikát a kosárban lévő szőnyegek számával. Ha ez nem lenne onRestart-ban, akkor ha    elhagyjuk a főképernyőt és visszatérünk, akkor eltűnne a karika.

Android permission
- Rendelés leadásánál van egy checkbox, amit ha bepipálunk akkor kapunk egy értesítést (amihez api 33-tól kell permission), ha kész a számla. Ez egy timerrel működik, pár     másodpercen belül kapunk értesítést. Ha nem pipáljuk be nincs timer, azonnal létrejön az értesítés.

CRUD
- Create: Felhasználó regisztráció, számla készítés
- Read: Felhasználó adat lekérdezés, szőnyegek listázása, számlák listázása
- Update: Felhasználó adat szerkesztés
- Delete: Számlák törlése

2 Komplex Firestore lekérdezés
- utils package CarpetDbManager 30. sor
- utils package CarpetDbManager 61. sor
