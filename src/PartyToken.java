// Pomocznika klasa do obliczania mandatów,
// publiczne artybuty dla zwiększenia czytelności programu.
public class PartyToken implements Comparable
{
    // Numer identyfikatora partii.
    public int partyID;
    // Liczba głosów która partia otrzymała.
    public int votesCast;
    // Liczba mandatów które partia otrzymała.
    public int seatsGotten;

    public PartyToken(int partyID, int votesCast, int seatsGotten)
    {
        this.partyID = partyID;
        this.votesCast = votesCast;
        this.seatsGotten = seatsGotten;
    }

    // Modyfikacja comperatora, żeby można było sortować te klasy.
    @Override
    public int compareTo(Object second)
    {
        return ((PartyToken)second).votesCast - this.votesCast;
    }
}
