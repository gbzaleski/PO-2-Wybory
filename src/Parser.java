import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import static java.lang.Math.abs;

// Klasa dokonująca wczytania danych z pliku i wstępnej obróbki ze sprawdzeniem poprawności.
public class Parser 
{
    // Pomocnicze struktury do analizy pliku
    BufferedReader data;
    int[] toMerge;
    int[] pushPos;
    HashMap<String, Integer> partyIndex;

    // Wszyskie partie
    private  Party[] allParties;
    // Wszyskie okręgi.
    private  District[] allDistricts;
    // Wszystkie możliwe działania kampanijne.
    private int[][] campaigns;
    
    public Parser (BufferedReader data)
    {
        this.data = data;
    }
    
    public void readData(int districts, int parties, int actions, int traits) throws IOException
    {
        // Assert na poprawny input głownych parametrów.
        assert(districts > 0 && parties > 0 && actions > 0  && traits > 0);
        
        // Pomocnicza tablice do łączenia okregów.
        toMerge = new int[districts + 1];
        pushPos = new int[districts + 1];

        readMainInfo(districts, parties);
        readAllCandidates(districts, parties, traits);
        readAllVoters(districts, traits);
        readAllCampaigns(actions, traits);

        // Koniec inputu.
        assert (data.readLine() == null);
    }

    // Wczytanie możliwych działań kampanijnych.
    private void readAllCampaigns(int actions, int traits) throws IOException
    {
        campaigns = new int[actions + 1][traits];
        for (int a = 1; a <= actions; ++a)
        {
            String line = data.readLine();
            String[] lineValues = line.split(" ");
            int cost = 0;

            for (int t = 0; t < traits; ++t)
            {
                // Przykładowy test zaczyna się od spacji na poczatku linii z wektorami zmiany wag.
                campaigns[a][t] = Integer.parseInt(lineValues[t + Boolean.compare(lineValues[0].equals(""), false)]);
                cost += abs(campaigns[a][t]);
            }
            assert(cost > 0 && lineValues.length == traits + Boolean.compare(lineValues[0].equals(""), false));
        }
    }

    // Wczytanie każdego wyborcy.
    private void readAllVoters(int districts, int traits) throws IOException
    {
        for (int d = 1; d <= districts; ++d)
        {
            if (allDistricts[d] == null)
                continue;

            for (int v = 1; v <= allDistricts[d].getPopulation(); ++v)
            {
                String line = data.readLine();
                String[] lineValues = line.split(" ");
                String name = lineValues[0];
                String surname = lineValues[1];
                int voterType = Integer.parseInt(lineValues[3]);

                // Zelazny wyborca partii.
                if (voterType == 1)
                {
                    int favParty = partyIndex.get(lineValues[4]);
                    allDistricts[d].setVoter(v, new SimpleVoter(name, surname, favParty, 0, d));
                    assert(lineValues.length == 5);
                }
                // Zelazny wyborca kandydata.
                else if (voterType == 2)
                {
                    int favParty = partyIndex.get(lineValues[4]);
                    int favCandidate = Integer.parseInt(lineValues[5]) + pushPos[d];
                    allDistricts[d].setVoter(v, new SimpleVoter(name, surname, favParty, favCandidate, d));
                    assert(lineValues.length == 6);
                }
                // Wyborca minimalizujący jednocechowy wybierający spośród wszystkich partii.
                else if (voterType == 3)
                {
                    int whichTrait = Integer.parseInt(lineValues[4]) - 1;
                    allDistricts[d].setVoter(v, new MinTraitVoter(name, surname, 0, whichTrait, d));
                    assert(lineValues.length == 5);
                }
                // Wyborca maksymalizujący jednocechowy wybierający spośród wszystkich partii.
                else if (voterType == 4)
                {
                    int whichTrait = Integer.parseInt(lineValues[4]) - 1;
                    allDistricts[d].setVoter(v, new MaxTraitVoter(name, surname, 0, whichTrait, d));
                    assert(lineValues.length == 5);
                }
                // Wyborca wszechstronny wybierający spośród wszystkich partii
                else if (voterType == 5)
                {
                    int[] weights = new int[traits];
                    for (int i = 0; i < traits; ++i)
                        weights[i] = Integer.parseInt(lineValues[i + 4]);
                    allDistricts[d].setVoter(v, new WeightedVoter(name, surname, 0, weights, d));
                    assert(lineValues.length == 4 + traits);
                }
                // Wyborca minimalizujący jednocechowy wybierający spośród jednej partii.
                else if (voterType == 6)
                {
                    int whichTrait = Integer.parseInt(lineValues[4]) - 1;
                    int favParty = partyIndex.get(lineValues[5]);
                    allDistricts[d].setVoter(v, new MinTraitVoter(name, surname, favParty, whichTrait, d));
                    assert(lineValues.length == 6);
                }
                // Wyborca maksymalizujący jednocechowy wybierający spośród jednej partii.
                else if (voterType == 7)
                {
                    int whichTrait = Integer.parseInt(lineValues[4]) - 1;
                    int favParty = partyIndex.get(lineValues[5]);
                    allDistricts[d].setVoter(v, new MaxTraitVoter(name, surname, favParty, whichTrait, d));
                    assert(lineValues.length == 6);
                }
                // Wyborca minimalizujący jednocechowy wybierający spośród jednej partii.
                else if (voterType == 8)
                {
                    int[] weights = new int[traits];
                    for (int i = 0; i < traits; ++i)
                        weights[i] = Integer.parseInt(lineValues[i + 4]);
                    int favParty = partyIndex.get(lineValues[traits + 4]);
                    allDistricts[d].setVoter(v, new WeightedVoter(name, surname, favParty, weights, d));
                    assert(lineValues.length == 5 + traits);
                }
                else assert false;
            }
        }
    }

    // Wczytanie każdego kandydata.
    private void readAllCandidates(int districts, int parties, int traits) throws IOException
    {
        for (int d = 1; d <= districts; ++d)
        {
            if (allDistricts[d] == null)
                continue;
            for (int p = 1; p <= parties; ++p)
            {
                for (int i = 1; i <= allDistricts[d].getPopulation() / 10; ++i)
                {
                    String line = data.readLine();
                    String[] lineValues = line.split(" ");
                    int whichDistrict = Integer.parseInt(lineValues[2]);
                    int whichParty = partyIndex.get(lineValues[3]);
                    int posOnList = Integer.parseInt(lineValues[4]) + pushPos[whichDistrict];
                    if (toMerge[whichDistrict] != 0)
                        whichDistrict = toMerge[whichDistrict];
                    int[] candidatesTraits = new int[traits];
                    for (int t = 0; t < traits; ++t)
                        candidatesTraits[t] = Integer.parseInt(lineValues[5 + t]);

                    var currentCandidate = new Candidate(lineValues[0], lineValues[1], candidatesTraits);
                    allDistricts[whichDistrict].setCandidate(whichParty, posOnList, currentCandidate);
                }
            }
        }
    }

    // Wczytanie głownych informacji.
    private void readMainInfo(int districts, int parties) throws IOException
    {
        // Wczytanie okręgów do scalenia.
        String line = data.readLine();
        String[] lineValues = line.replace(","," ").replace("(", "").replace(")", "").split(" ");
        for (int i = 0; i < 2*Integer.parseInt(lineValues[0]); i = i + 2)
        {
            int a = Integer.parseInt(lineValues[i+1]);
            int b = Integer.parseInt(lineValues[i+2]);
            if (b > a)
            {
                var temp = a;
                a = b;
                b = temp;
            }
            toMerge[a] = b;
            assert(b + 1 == a);
        }

        // Wczytanie nazwy partii.
        line = data.readLine();
        String[] partyNames = line.split(" ");

        // Wczytanie budżetów partii.
        line = data.readLine();
        String[] partyBudgets = line.split(" ");

        // Wczytanie rodzai strategii podjętej przez daną partie.
        line = data.readLine();
        lineValues = line.split(" ");
        allParties = new Party[parties + 1];

        // Mapowanie nazwy partii na jej identyfikator liczbowy.
        partyIndex = new HashMap<String, Integer>();

        for (int i = 0; i < parties; ++i)
        {
            partyIndex.put(partyNames[i], i + 1);
            if (lineValues[i].equals("W"))
            {
                allParties[i+1] = new MyOwnParty(partyNames[i], Integer.parseInt(partyBudgets[i]));
            }
            else if (lineValues[i].equals("R"))
            {
                allParties[i+1] = new LuxuriousParty(partyNames[i], Integer.parseInt(partyBudgets[i]));
            }
            else if (lineValues[i].equals("S"))
            {
                allParties[i+1] = new ModestParty(partyNames[i], Integer.parseInt(partyBudgets[i]));
            }
            else if (lineValues[i].equals("Z"))
            {
                allParties[i+1] = new AlgorithmParty(partyNames[i], Integer.parseInt(partyBudgets[i]));
            }
            // Jeśli dane są poprawne to to się nigdy nie wykona.
            else assert false;
        }

        // Wczytanie populacji każdego okręgu.
        line = data.readLine();
        lineValues = line.split(" ");
        int[] districtPop = new int[districts + 1];

        // Scalanie okręgów.
        for (int i = 0; i < districts; ++i)
        {
            districtPop[i+1] = Integer.parseInt(lineValues[i]);
            if (toMerge[i+1] != 0)
            {
                pushPos[i+1] = districtPop[toMerge[i+1]]/10;
                districtPop[toMerge[i+1]] += districtPop[i+1];
                districtPop[i+1] = 0;
            }
        }

        allDistricts = new District[districts + 1];
        for (int i = 1; i <= districts; ++i)
        {
            if (districtPop[i] == 0)
            {
                allDistricts[i] = null;
            }
            else
            {
                allDistricts[i] = new District(districtPop[i], parties);
            }
        }
    }

    public District[] getDistricts()
    {
        return allDistricts;
    }

    public Party[] getParties() 
    {
        return allParties;
    }

    public int[][] getCampaigns()
    {
        return campaigns;
    }
}
