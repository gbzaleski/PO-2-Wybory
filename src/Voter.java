// Wspólna nadklasa dla wszystkich wyborców.
abstract public class Voter
{
    // Imie wyborcy.
    protected String name;
    // Nazwiwsko wyborcy.
    protected String surname;
    // Partia z której wybiera kandydata, 0 - jeśli wybiera ze wszystkich.
    protected int favParty;
    // Numer okręgu w którym wyborca głosuje.
    protected int district;
    // Kandydat na którego wyborca głosuje.
    protected Candidate chosenCandidate;

    public Voter(String name, String surname, int favParty, int district)
    {
        this.name = name;
        this.surname = surname;
        this.favParty = favParty;
        this.district = district;
    }

    // Oddanie głosu.
    abstract public void castVote(District where, int[] voteGotten);

    // Reakcja na kampanie - (potencjalna) zmiana wag.
    abstract public void getInfleuced(int[] shiftTrait);

    // Obliczenie podatności na kampanię.
    public abstract int getImpactDiff(int[] shiftTrait);

    @Override
    public String toString()
    {
        return name + " " + surname;
    }

    public Candidate getCandidate()
    {
        return chosenCandidate;
    }
}
