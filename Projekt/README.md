# Meteo aplikace
- Projek na předmět PPJ. Meteo aplikace zpracovávající údaje meteorologických dat
- Více info v souboru se [zadáním](zadani.pdf)
- Zdrojové kódy aplikace [zde](meteo/)
- NOTE: V hlavičce webové stránky zůstala chybně záložka API

## Vývojářský režim
- Pro vývojový režim je k dispozici [.devcontainer](.devcontainer) s docker-compose
    - Složený z aplikace na Java 17, Maria DB a pro přístup k DB je Adminer
    - Samotná aplikace je dále z [zde](meteo/) namapována dovnitře konteineru
    - Nastavení aplikace, databáze i admineru je uvnitř .devcontainer v [.env](.env)
    - Může být spuštěno například z VS Code

## Produkce
- Pro "produkční" prostředí
    - Běžne by se nasazovalo skrze k8s, ale pro zjednodušení je připraven image s aplikací, kde doško k překopírování vybuildované .jar aplikace
    - V docker-compose se k němu potom přidá Maria DB
    - Image je sestaven z Dockerfile
    - Nastavení prostředí má svůj [.env](.env)

Bližší informace ke zdrojovým kódům aplikace je v [README.md](meteo/README.md) ve složce meteo.
