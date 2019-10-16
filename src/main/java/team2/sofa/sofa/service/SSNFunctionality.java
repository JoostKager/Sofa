package team2.sofa.sofa.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
/**
 * Functionality omdat het zowel een BNS checker als BSN generator is.
 * De static final LOW en HIGH, zijn nodig om een random getal te genereren tussen de 10.000.000 en 99.999.999
 * welke kan zorgen voor een geldig BSN nummer
 */
public class SSNFunctionality {
    private static final int LOW = 10000000;
    private static final int HIGH = 100000000;


    public SSNFunctionality(){
    }

    /**
     * checkt een ingevoerd nummer (kan bijvoorbeeld ook input uit form zijn of het volgens de richtlijnen,
     * zoals beschreven in het artikel in wikipedia over het BSN: https://nl.wikipedia.org/wiki/Burgerservicenummer#11-proef
     * @param ssn een mogelijk geldig BSN
     * @return false als de invoer de proef niet doorstaat en true bij technisch geldig BSN.
     * er is geen controle op daadwerkelijk actief en uitgegeven BSN, alleen of het technisch een BSN kan zijn.
     */
    public static boolean ssnCheck(String ssn){
        int sum = 0;
        int multiply = 9;
        for (int i = 0; i < ssn.length()-1; i++) {
            int number = Character.getNumericValue(ssn.charAt(i));
            sum += number * multiply;
            multiply--;
        }
        sum += Character.getNumericValue(ssn.charAt(8)) * -1;
        return sum % 11 == 0;
    }

    /**
     * Generator die zorgt voor een random string met lengte van 9 tekens die kan worden gebruikt in de BSNcheck.
     * @return een nog niet geveriefeerde string die 9 tekens lang is.
     */
    public static String bsnGenerator() {
        Random r = new Random();
        int result = r.nextInt(HIGH-LOW) + LOW;
        return String.format("%09d", result);
    }

    /**
     * helper methode voor het gemakkelijk aanmaken van een stack willekeurige BSN nummers
     * @param count
     * @return
     */
    public static Deque<String> bsnStack(int count){
        Deque<String> ssnStack = new ArrayDeque<>();
        String bsn;
        for (int i = 0; i < count ; i++) {
            do {bsn = bsnGenerator();
            } while (!ssnCheck(bsn));
            if (!ssnStack.contains(bsn)){
                ssnStack.push(bsn);
            }
        }
        return ssnStack;
    }
}
