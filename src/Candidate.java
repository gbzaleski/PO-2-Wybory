public class Candidate
{
    private String name;
    private String surname;
    private int votesCast;
    private int[] trait;

    public Candidate (String name, String surname, int[] trait)
    {
        this.name = name;
        this.surname = surname;
        this.trait = trait;
        votesCast = 0;
    }

    public int getVotes()
    {
        return votesCast;
    }

    public void Voted()
    {
        votesCast++;
    }

    public int getTrait(int i)
    {
        return trait[i];
    }

    @Override
    public String toString()
    {
        return name + " " + surname;
    }

}
