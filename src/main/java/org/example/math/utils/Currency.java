package org.example.math.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.HashMap;

public class Currency {
    private static boolean success = false;
    private static boolean trying = false;
    private static HashMap<String, Double> rates = new HashMap<>();
    private static HashMap<String, String> names = new HashMap<>();

    private final String id;
    private final String name;
    private final Double price;

    private Currency(String id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Currency getCurrency(String id) throws IllegalCurrencyException {
        if(!rates.containsKey(id)) throw new IllegalCurrencyException("Id \"" + id + "\" not found");
        String name = id;
        if(names.containsKey(id)) name = names.get(id);

        return new Currency(id, name, rates.get(id));
    }

    public static Currency addCurrency(String id, String name, Double pricePerEUR) throws IllegalCurrencyException {
        if(rates.containsKey(id)) throw new IllegalCurrencyException("Id \"" + id + "\" already in use");
        rates.put(id, pricePerEUR);
        names.put(id, name);
        return new Currency(id, name, rates.get(id));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPriceInEUR() {
        return price;
    }

    public static void init() {
        if(rates.isEmpty()) {
            getRates();
        }
    }

    public static void initWait() {
        if(rates.isEmpty()) {
            getRates();
        }
        while(trying) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void getRates() {
        trying = true;
        new Thread(new Runnable() {
            @Override public void run() {

                rates.clear();
                rates.put("BEF", 40.3399);
                rates.put("BGN", 1.98853);
                rates.put("DEM", 1.98853);
                rates.put("EEK", 15.6466);
                rates.put("FIM", 5.94573);
                rates.put("FRF", 6.55957);
                rates.put("GRD", 340.750);
                rates.put("IEP", .787564);
                rates.put("ITL", 1936.27);
                rates.put("HRK", 7.53450);
                rates.put("LVL", 0.702804);
                rates.put("LTL", 3.45280);
                rates.put("LUF", 40.3399);
                rates.put("MTL", .429300);
                rates.put("NLG", 2.20371);
                rates.put("ATS", 13.7603);
                rates.put("PTE", 200.482);
                rates.put("SKK", 30.1260);
                rates.put("SIT", 239.640);
                rates.put("ESP", 166.386);
                rates.put("CYP", .585274);

                try {
                    String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(new URL(url).openStream());
                    Node root = doc.getDocumentElement();
                    for(int i = 0; i < root.getChildNodes().getLength(); i++) {
                        Node cube1 = root.getChildNodes().item(i);

                        if(cube1.getNodeName().equals("Cube")) {
                            for(int i2 = 0; i2 < cube1.getChildNodes().getLength(); i2++) {
                                Node cube2 = cube1.getChildNodes().item(i2);
                                if("Cube".equals(cube2.getNodeName())) {
                                    for (int j = 0; j < cube2.getChildNodes().getLength(); j++) {
                                        Node curr = cube2.getChildNodes().item(j);
                                        if("Cube".equals(curr.getNodeName())) {
                                            rates.put(
                                                    curr.getAttributes().getNamedItem("currency").getNodeValue(),
                                                    Double.parseDouble(curr.getAttributes().getNamedItem("rate").getNodeValue()));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    double sdr = .37379; // EUR
                    sdr += .57813/rates.get("USD");
                    sdr += 13.452/rates.get("JPY");
                    sdr += .08087/rates.get("GBP");
                    sdr += 1.0993/rates.get("CNY");
                    rates.put("SDR", sdr);
                    //System.err.println("SDR="+sdr);


                    rates.put("EUR", 1.);
                    names.put("EUR","Euro");

                    names.put("BEF","Belgische Franken");
                    names.put("BGN","Bulgarische Lew");
                    names.put("DEM","Deutsche Mark");
                    names.put("EEK","Estnische Kronen");
                    names.put("FIM","Finnische Mark");
                    names.put("FRF", "Französische Francs");
                    names.put("GRD","Griechische Drachmen");
                    names.put("IEP","Irische Pfund");
                    names.put("ITL","Italienische Lire");
                    names.put("HRK","Kroatische Kuna");
                    names.put("LVL","Lettische Lats");
                    names.put("LTL","Litauische Litas");
                    names.put("LUF","Luxemburgische Francs");
                    names.put("MTL","Maltesische Lire");
                    names.put("NLG","Niederländische Gulden");
                    names.put("ATS","Österreichische Schilling");
                    names.put("PTE","Portugiesische Escudos");
                    names.put("SKK","Slowakische Kronen");
                    names.put("SIT","Slowenische Tolar");
                    names.put("ESP","Spanische Peseten");
                    names.put("CYP","Zypriotische Pfund");

                    names.put("USD","US-Dollar");
                    names.put("JPY","Japanische Yen");
                    names.put("CZK","Tschechische Kronen");
                    names.put("DKK","Dänische Kronen");
                    names.put("GBP","Britische Pfund");
                    names.put("HUF","Ungarischer Forint");
                    names.put("PLN","Polnischer Złoty");
                    names.put("RON","Rumänischer Leu");
                    names.put("SEK","Schwedische Kronen");
                    names.put("CHF","Schweizer Franken");
                    names.put("ISK","Isländische Kronen");
                    names.put("NOK","Norwegische Kronen");
                    names.put("TRY","Türkische Lire");
                    names.put("AUD","Australische Dollar");
                    names.put("BRL","Brasilianischer Real");
                    names.put("CAD","Kanadische Dollar");
                    names.put("CNY","Chinesischer Yuan");
                    names.put("HKD","Hongkong-Dollar");
                    names.put("IDR","Indonesische Rupie");
                    names.put("ILS","Israelischer Schekel");
                    names.put("INR","Indische Rupie");
                    names.put("KRW","Südkoreanischer Won");
                    names.put("MXN","Mexikanischer Peso");
                    names.put("MYR","Malaysischer Ringgit");
                    names.put("NZD","Neuseeländische Dollar");
                    names.put("PHP","Philippinischer Peso");
                    names.put("SGD","Singapur-Dollar");
                    names.put("THB","Thailändischer Baht");
                    names.put("ZAR","Südafrikanische Rand");
                    names.put("SDR","Sonderziehungsrechte");

                    addPeggedCurrency("ZAR", "NAD", "Namibia-Dollar");

                    addPeggedCurrency("DKK", "FOK", "Färöische Krone");

                    addPeggedCurrency("GBP", "IMP", "Isle-of-Man-Pfund");
                    addPeggedCurrency("GBP", "JEP", "Jersey-Pfund");
                    addPeggedCurrency("GBP", "GGP", "Guernsey-Pfund");
                    addPeggedCurrency("GBP", "GIP", "Gibraltar-Pfund");
                    addPeggedCurrency("GBP", "SHP", "St.-Helena-Pfund");
                    addPeggedCurrency("GBP", "FKP", "Falkland-Pfund");

                    names.put("XOF", "CFA-Franc");
                    rates.put("XOF", rates.get("FRF") * 100);
                    names.put("XPF", "CFP-Franc");
                    //rates.put("XPF", 119.33);
                    rates.put("XPF", rates.get("FRF") * 1000 / 55);
                    addPeggedCurrency("XOF", "XAF", "CFA-Franc");

                    success = true;
                } catch (Exception e) {
                    System.err.println("Unable to get exchange rates: " + e.getMessage());
                }

                trying = false;
            }
        }).start();
    }

    private static void addPeggedCurrency(String baseId, String newId, String newName) {
        rates.put(newId, rates.get(baseId));
        names.put(newId, newName);
    }

    public static class IllegalCurrencyException extends Exception {
        public IllegalCurrencyException(String message) {
            super(message);
        }
    }
}
