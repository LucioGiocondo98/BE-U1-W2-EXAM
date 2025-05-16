import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class Archivio {
    private List<Catalogo> elementi;

    public Archivio() {
        this.elementi = new ArrayList<>();
    }

    public void aggiungiElemento(Catalogo elemento) throws IllegalArgumentException {
        boolean esiste = elementi.stream()
                .anyMatch(e -> e.getIsbn() == elemento.getIsbn());
        if (esiste) {
            throw new IllegalArgumentException("Elemento con ISBN: " + elemento.getIsbn() + " già presente");
        }
        elementi.add(elemento);
    }

    public Catalogo cercaPerIsbn(int isbn) throws ElementoNonTrovatoException {
        return elementi.stream()
                .filter(e -> e.getIsbn() == isbn)
                .findFirst()
                .orElseThrow(() -> new ElementoNonTrovatoException("Elemento con ISBN: " + isbn + " non trovato"));
    }

    public boolean rimuoviPerIsbn(int isbn) {
        return elementi.removeIf(e -> e.getIsbn() == isbn);
    }

    public List<Catalogo> cercaPerAnno(int anno) {
        return elementi.stream()
                .filter(e -> e.getAnnoPubblicazione() == anno)
                .collect(Collectors.toList());
    }

    public List<Libro> cercaPerAutore(String autore) {
        return elementi.stream()
                .filter(e -> e instanceof Libro)
                .map(e -> (Libro) e)
                .filter(a -> a.getAutore().equalsIgnoreCase(autore))
                .collect(Collectors.toList());
    }

    public void aggiornaElemento(int isbn, Catalogo nuovoElemento) throws ElementoNonTrovatoException {
        for (int i = 0; i < elementi.size(); i++) {
            if (elementi.get(i).getIsbn() == isbn) {
                elementi.set(i, nuovoElemento);
                return;
            }
        }
        throw new ElementoNonTrovatoException("Elemento con ISBN: " + isbn + " non trovato");
    }


    public Catalogo elementoConPagineMassime() {
        return elementi.stream()
                .max(Comparator.comparingInt(Catalogo::getNumeroPagine))
                .orElse(null);
    }


    public void stampaStatistiche() {
        long totaleLibri = elementi.stream()
                .filter(e -> e instanceof Libro)
                .count();

        long totaleRiviste = elementi.stream()
                .filter(e -> e instanceof Rivista)
                .count();

        System.out.println("Numero totale libri: " + totaleLibri);
        System.out.println("Numero totale riviste: " + totaleRiviste);

        Catalogo maxPagineElemento = elementoConPagineMassime();
        if (maxPagineElemento != null) {
            System.out.println("Elemento con più pagine: " + maxPagineElemento);
        }

        IntSummaryStatistics stats = elementi.stream()
                .mapToInt(Catalogo::getNumeroPagine)
                .summaryStatistics();

        if (stats.getCount() > 0) {
            System.out.printf("Media pagine: %.2f%n", stats.getAverage());

        }
    }
}
