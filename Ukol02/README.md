# Ukol 02

Nejnovější vydaná verze k dnešnímu dni 18.03.2023 je JDK 19.

## Vybrané novinky

- [Record Patterns](https://openjdk.org/jeps/405) (Preview)
- [Virtual Threads](https://openjdk.org/jeps/425) (Preview)
- [Structured Concurrency](https://openjdk.org/jeps/428) (Incubator)

### Record Patterns
Výtažek (podrobně v [`Record Patterns`](https://openjdk.org/jeps/405)):

Jedná se o rozšíření vzorů pro jednodušší dekonstrukci struktur dat. 
Cílem je rozšířit porovnávacích a sofistikovanějších dotazů a nezměnit syntaxi 
ani sémantiku. Např. pro "nested" zanořené objekty.

Obecně řečeno jde o zjednodušení práce s daty strukturovaných po objektech. 
Nemusíme se dotazovat po objektech "nižších" úrovní, 
ale můžeme si je nechat dekonstruovat přímo v `isntance of`.

```
static void printColorOfUpperLeftPoint(Rectangle r) {
    if (r instanceof Rectangle(ColoredPoint(Point p, Color c), ColoredPoint lr)) {
        System.out.println(c);
    }
}
```

Obdobně lze pracovat s generickými typy. 
### Virtual Threads
Výtažek (podrobně v [`Virtual Threads`](https://openjdk.org/jeps/425)):

Virtuální vlákna jsou odlehčená vlákna, která výrazně snižují nároky na psaní, 
údržbu a sledování vysoce výkonných souběžných aplikací.

Cílem umožnit serverovým aplikacím požadavek škálovat s téměř optimálním využitím hardwaru. 
Umožnit stávajícímu kódu přijmout virtuální vlákna s minimálními změnami. 

Virtuální vlákna nejsou rychlejší než stávající ale mají vyšší propustnost. V pořádku je 10 000 Virtuálních vláken. 

Využívají se pro API na straně serveru. Příklad pro 10 000 virtuálních vláken, která 1s spí.

```
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 10_000).forEach(i -> {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return i;
        });
    });
}
```

Hlavní rozdíl oproti normálním vláknům je plánovač a zpracování vláken.
Klasická vlákna plánuje a přiděluje OS, ale virt vlákna mají vlastní planovač. 
Ten je přiřazuje klasickým platformním vláknům. Ty jsou dále plánována jako obvykle.

Virtuální vlákna neočekávají předání aplikačního kodu zpět plánovači. Tedy jsou nekooperativní.
Tedy program nesmí spoléhat na to kdy a jak jsou vlákna předána procesoru ke zpracování.

Zásobníky virtuálních vláken jsou uloženy v garbage collectoru,
dynamicky s zvětšují a zmenšují aby byly pamětově nenáročné.

### Structured Concurrency
Výtažek (podrobně v dokumentaci):

Zjednodušení vícevláknového programování zavedením rozhraní API pro strukturovanou souběžnost.
Strukturovaná souběžnost považuje více úloh běžících v různých vláknech za jednu
pracovní jednotku, čímž se zjednodušuje zpracování chyb a rušení, 
zvyšuje spolehlivost a zlepšuje pozorovatelnost. Jedná se o inkubační rozhraní API.

```
Response handle() throws ExecutionException, InterruptedException {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        Future<String>  user  = scope.fork(() -> findUser());
        Future<Integer> order = scope.fork(() -> fetchOrder());

        scope.join();           // Join both forks
        scope.throwIfFailed();  // ... and propagate errors

        // Here, both forks have succeeded, so compose their results
        return new Response(user.resultNow(), order.resultNow());
    }
}
```