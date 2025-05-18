import java.util.List;
import java.util.Scanner;

public class Prova {
    public static void main(String[] args) {
        Archivio archivio = new Archivio();
        Scanner scanner = new Scanner(System.in);
        boolean continua = true;

        while (continua) {
            System.out.println("\n---- MENU ARCHIVIO ----");
            System.out.println("1. Aggiungi elemento");
            System.out.println("2. Cerca per ISBN");
            System.out.println("3. Rimuovi per ISBN");
            System.out.println("4. Cerca per anno");
            System.out.println("5. Cerca libro per autore");
            System.out.println("6. Aggiorna elemento");
            System.out.println("7. Mostra statistiche");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");
            String scelta = scanner.nextLine().trim();

            switch (scelta) {
                case "1" -> aggiungiElemento(scanner, archivio);
                case "2" -> cercaPerISBN(scanner, archivio);
                case "3" -> rimuoviPerISBN(scanner, archivio);
                case "4" -> cercaPerAnno(scanner, archivio);
                case "5" -> cercaPerAutore(scanner, archivio);
                case "6" -> aggiornaElemento(scanner, archivio);
                case "7" -> archivio.stampaStatistiche();
                case "0" -> {
                    continua = false;
                    System.out.println("Uscita dal programma.");
                }
                default -> System.out.println("Scelta non valida.");
            }
        }

        scanner.close();
    }

    private static void aggiungiElemento(Scanner scanner, Archivio archivio) {
        try {
            String tipo = "";
            while (true) {
                System.out.print("Tipo (L = Libro, R = Rivista): ");
                tipo = scanner.nextLine().trim().toUpperCase();
                if (tipo.equals("L") || tipo.equals("R")) {
                    break; // input valido, esci dal ciclo
                } else {
                    System.out.println("Input non valido. Inserisci solo 'L' per Libro o 'R' per Rivista.");
                }
            }

            System.out.print("Inserisci ISBN: ");
            int isbn = Integer.parseInt(scanner.nextLine());

            System.out.print("Inserisci titolo: ");
            String titolo = scanner.nextLine();

            System.out.print("Inserisci anno di pubblicazione: ");
            int anno = Integer.parseInt(scanner.nextLine());

            System.out.print("Inserisci numero di pagine: ");
            int pagine = Integer.parseInt(scanner.nextLine());

            if (tipo.equals("L")) {
                System.out.print("Inserisci autore: ");
                String autore = scanner.nextLine();

                System.out.print("Inserisci genere: ");
                String genere = scanner.nextLine();

                Libro libro = new Libro(isbn, titolo, anno, pagine, autore, genere);
                archivio.aggiungiElemento(libro);

            } else {
                System.out.print("Inserisci periodicità (SETTIMANALE, MENSILE, SEMESTRALE): ");
                String periodicita = scanner.nextLine().trim().toUpperCase();

                Rivista rivista = new Rivista(isbn, titolo, anno, pagine, Periodicita.valueOf(periodicita));
                archivio.aggiungiElemento(rivista);
            }

            System.out.println("Elemento aggiunto con successo.");

        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }



    private static void cercaPerISBN(Scanner scanner, Archivio archivio) {
        try {
            System.out.print("ISBN da cercare: ");
            int isbn = Integer.parseInt(scanner.nextLine());
            Catalogo trovato = archivio.cercaPerIsbn(isbn);
            System.out.println("Trovato: " + trovato);
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private static void rimuoviPerISBN(Scanner scanner, Archivio archivio) {
        System.out.print("ISBN da rimuovere: ");
        int isbn = Integer.parseInt(scanner.nextLine());
        boolean rimosso = archivio.rimuoviPerIsbn(isbn);
        System.out.println(rimosso ? "Elemento rimosso." : "Elemento non trovato.");
    }

    private static void cercaPerAnno(Scanner scanner, Archivio archivio) {
        System.out.print("Anno di pubblicazione: ");
        int anno = Integer.parseInt(scanner.nextLine());
        List<Catalogo> risultati = archivio.cercaPerAnno(anno);
        if (risultati.isEmpty()) {
            System.out.println("Nessun elemento trovato.");
        } else {
            risultati.forEach(System.out::println);
        }
    }

    private static void cercaPerAutore(Scanner scanner, Archivio archivio) {
        System.out.print("Nome autore: ");
        String autore = scanner.nextLine();
        List<Libro> libri = archivio.cercaPerAutore(autore);
        if (libri.isEmpty()) {
            System.out.println("Nessun libro trovato.");
        } else {
            libri.forEach(System.out::println);
        }
    }

    private static void aggiornaElemento(Scanner scanner, Archivio archivio) {
        try {
            System.out.print("ISBN dell'elemento da aggiornare: ");
            int isbn = Integer.parseInt(scanner.nextLine());

            Catalogo vecchio = archivio.cercaPerIsbn(isbn);
            if (vecchio == null) {
                System.out.println("Elemento con ISBN " + isbn + " non trovato.");
                return;
            }

            System.out.print("Nuovo titolo: ");
            String titolo = scanner.nextLine();

            System.out.print("Nuovo anno di pubblicazione: ");
            int anno = Integer.parseInt(scanner.nextLine());

            System.out.print("Nuove pagine: ");
            int pagine = Integer.parseInt(scanner.nextLine());

            Catalogo nuovo;

            if (vecchio instanceof Libro) {
                System.out.print("Nuovo autore: ");
                String autore = scanner.nextLine();

                System.out.print("Nuovo genere: ");
                String genere = scanner.nextLine();

                nuovo = new Libro(isbn, titolo, anno, pagine, autore, genere);

            } else if (vecchio instanceof Rivista) {
                System.out.print("Nuova periodicità (SETTIMANALE, MENSILE, SEMESTRALE): ");
                String periodicita = scanner.nextLine().trim().toUpperCase();

                nuovo = new Rivista(isbn, titolo, anno, pagine, Periodicita.valueOf(periodicita));

            } else {
                System.out.println("Tipo di elemento non riconosciuto.");
                return;
            }

            archivio.aggiornaElemento(isbn, nuovo);
            System.out.println("Elemento aggiornato con successo.");

        } catch (ElementoNonTrovatoException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

}
