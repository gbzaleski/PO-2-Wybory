/***********************************
*** Grzegorz B. Zaleski (418494) ***
***********************************/

// Wczytanie używanych modułów/struktur/funkcji.
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main
{
    // Struktury danych wykorzystywane przez program:
    // Odpowiedno liczba: okręgów, partii, możliwych działań kampinijnych, cech kandydata.
    private static int districts;
    private static int parties;
    private static int actions;
    private static int traits;
    // Wszystkie partie.
    private static Party[] allParties;
    // Wszyskie okręgi.
    private static District[] allDistricts;
    // Wszystkie możliwe działania kampanijne.
    private static int[][] campaigns;

    // Głowna funkcja - odpowiada za przekazanie realizacji każdej części zadania odpowiedniej funkcji.
    public static void main(String[] args) throws Exception
    {
        // Zakładamy że program dostaje ścieżke do pliku z danymi.
	    readData(args[0]);
	    launchAllCampaings();
	    castEveryVote();
	    dhontResults();
	    sainteLagueResults();
	    hareNiemeyerResults();
    }

    // Policzenie i wypisane wyników metodą Hare’a-Niemeyera.
    private static void hareNiemeyerResults()
    {
        System.out.println("Metoda Hare’a-Niemeyera:");
        int[] finalSeatsResults = new int[parties + 1];
        for (int d = 1; d <= districts; ++d)
        {
            if (allDistricts[d] != null)
            {
                showDiscrictInfo(d);
                PartyToken[] partyTokens = new PartyToken[parties];
                for (int p = 1; p <= parties; ++p)
                {
                    partyTokens[p-1] = new PartyToken(p, allDistricts[d].getPartyResults(p), 0);
                }

                HareNiemeyerMethod hareNiemeyerMethod = new HareNiemeyerMethod();
                int[] districtSeatsResults = hareNiemeyerMethod.calculateSeats(partyTokens, allDistricts[d].getPopulation()/10, allDistricts[d].getPopulation());
                for (int p = 1; p <= parties; ++p)
                {
                    finalSeatsResults[p] += districtSeatsResults[p];
                }
                showSeats(districtSeatsResults);
            }
        }

        System.out.println("W sumie:");
        showSeats(finalSeatsResults);
    }

    // Policzenie i wypisane wyników metodą Sainte-Laguë'a.
    private static void sainteLagueResults()
    {
        System.out.println("Metoda Sainte-Laguë: ");
        int[] finalSeatsResults = new int[parties + 1];
        for (int d = 1; d <= districts; ++d)
        {
            if (allDistricts[d] != null)
            {
                showDiscrictInfo(d);
                PartyToken[] partyTokens = new PartyToken[parties];
                for (int p = 1; p <= parties; ++p)
                {
                    partyTokens[p-1] = new PartyToken(p, allDistricts[d].getPartyResults(p), 0);
                }

                SainteLagueMethod sainteLagueMethod = new SainteLagueMethod();
                int[] districtSeatsResults = sainteLagueMethod.calculateSeats(partyTokens, allDistricts[d].getPopulation()/10);
                for (int p = 1; p <= parties; ++p)
                {
                    finalSeatsResults[p] += districtSeatsResults[p];
                }
                showSeats(districtSeatsResults);
            }
        }

        System.out.println("W sumie:");
        showSeats(finalSeatsResults);
    }

    // Policzenie i wypisane wyników metodą D'Hondta.
    private static void dhontResults()
    {
        System.out.println("Metoda D'Hondta:");
        int[] finalSeatsResults = new int[parties + 1];
        for (int d = 1; d <= districts; ++d)
        {
            if (allDistricts[d] != null)
            {
                showDiscrictInfo(d);
                PartyToken[] partyTokens = new PartyToken[parties];
                for (int p = 1; p <= parties; ++p)
                {
                    partyTokens[p-1] = new PartyToken(p, allDistricts[d].getPartyResults(p), 0);
                }

                DHondtMethod dHondtMethod = new DHondtMethod();
                int[] districtSeatsResults = dHondtMethod.calculateSeats(partyTokens, allDistricts[d].getPopulation()/10);
                for (int p = 1; p <= parties; ++p)
                {
                    finalSeatsResults[p] += districtSeatsResults[p];
                }
                showSeats(districtSeatsResults);
            }
        }

        System.out.println("W sumie:");
        showSeats(finalSeatsResults);
    }

    // Wyświetla informacje dot. liczby mandatów.
    private static void showSeats(int[] seats)
    {
        for (int p = 1; p <= parties; ++p)
        {
            System.out.println(allParties[p] + " - l. mandatów: " + seats[p] );
        }
    }

    // Wypisane na kogos głosował każdy wyborca i ile głosów otrzymał każdy kandydat w danym okręgu.
    private static void showDiscrictInfo(int d)
    {
        System.out.println("Okręg nr " + d + ":");
        for (int v = 1; v <= allDistricts[d].getPopulation(); ++v)
        {
            var curVoter = allDistricts[d].getVoter(v);
            System.out.println(curVoter.toString() + " -> " + curVoter.getCandidate().toString());
        }

        for (int c = 1; c <= allDistricts[d].getPopulation()/10; ++c)
        {
            for (int p = 1; p <= parties; ++p)
            {
                var curCandidate = allDistricts[d].getCandidate(p, c);
                System.out.println(curCandidate.toString() + " " + allParties[p] + " " + c + " - l. głosów: " + curCandidate.getVotes());
            }
        }
    }

    // Kazdy wyborca oddaje głos.
    private static void castEveryVote()
    {
        for (int d = 1; d <= districts; ++d)
        {
            if (allDistricts[d] != null)
                allDistricts[d].castEveryVote();
        }
    }

    // Każda partia odpala swoją kampanię.
    private static void launchAllCampaings()
    {
        for (int p = 1; p <= parties; ++p)
        {
            allParties[p].launchCampaign(districts, parties, traits, actions, allDistricts, campaigns);
        }
    }

    // Wczytanie danych z pliku do programu.
    private static void readData(String filepath) throws Exception
    {
        // Stworzenie struktur na podane dane.
        File file = new File(filepath);
        BufferedReader data = new BufferedReader(new FileReader(file));
        Parser parser = new Parser(data);
        
        // Wczytanie głównych parametrów.
        String[] lineValues = data.readLine().split(" ");
        districts = Integer.parseInt(lineValues[0]);
        parties = Integer.parseInt(lineValues[1]);
        actions = Integer.parseInt(lineValues[2]);
        traits = Integer.parseInt(lineValues[3]);
                
        // Wczytanie inputu za pomocą metody w klasie Parser.
        parser.readData(districts, parties, actions, traits);
        
        // Zczytanie danych.
        allDistricts = parser.getDistricts();
        allParties = parser.getParties();
        campaigns = parser.getCampaigns();
    }
}

