// Klasa reprezentująca okręg wyborczy.
public class District
{
    // Wyborcy w okręgu.
    private Voter[] voter;
    // Liczba wyborców.
    private int people;
    // Kandydaci w okręgu.
    private Candidate[][] candidate;
    // Liczba partii w okręgu.
    private int parties;
    // Liczba głósów która otrzymała każda partia.
    private int[] partyResults;

    public District(int people, int parties)
    {
        this.people = people;
        voter = new Voter[people + 1];
        candidate = new Candidate[parties + 1][people/10 + 1];
        this.parties = parties;
        partyResults = new int[parties + 1];
    }

    public int getPopulation()
    {
        return people;
    }

    public int getPlurality()
    {
        return parties;
    }

    public void setCandidate(int party, int pos, Candidate candidate)
    {
        this.candidate[party][pos] = candidate;
    }

    // Oddanie głosu na danego kandydata.
    public void applyVote(int party, int pos)
    {
        candidate[party][pos].Voted();
    }

    public Candidate getCandidate(int party, int pos)
    {
        return candidate[party][pos];
    }

    public void setVoter(int pos, Voter _voter)
    {
        voter[pos] = _voter;
    }

    // Użycie działań kampanijnych na wyborców w tym okręgu.
    public void applyCampaign(int[] shiftTrait)
    {
        for (int v = 1; v <= people; ++v)
            voter[v].getInfleuced(shiftTrait);
    }

    // Obliczenie wpływu działań kampanijnych na wyborców w tym okręgu.
    public int getDiffImpact(int[] shiftTrait)
    {
        int impactMade = 0;
        for (int v = 1; v <= people; ++v)
           impactMade += voter[v].getImpactDiff(shiftTrait);
        return impactMade;
    }

    // Każdy wyborca w okręgu oddaje głos.
    public void castEveryVote()
    {
        for (int v = 1; v <= people; ++v)
            voter[v].castVote(this, partyResults);
    }

    public int getPartyResults(int i)
    {
        return partyResults[i];
    }

    public Voter getVoter(int v)
    {
        return voter[v];
    }
}
